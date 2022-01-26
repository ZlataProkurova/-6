
import java.util.Scanner;

class Player {
	
	private static final int EPSILON = 5;
	private static final int MAX_VERTICAL_SPEED = 40;
	private static final int MAX_HORIZONTAL_SPEED = 20;
	
	private static final double GRAVITY = 3.711;
	private static final int SECURITY_DISTANCE_FROM_FLAT_GROUND = 50;
    
	public static void main(String args[]) {
    	
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // кол-во точек, используемых для рисования поверхности марса
        
        int flatGroundLeftX = -1;
        int flatGroundRightX = -1;
        int flatGroundY = -1;
        
        int previousPointX = -1;
        int previousPointY = -1;
        for (int i = 0; i < N; i++) {
            int LAND_X = in.nextInt(); // X координата точки поверхности (от 0 до 6999)
            int LAND_Y = in.nextInt(); // Y координата точки поверхности. Последовательно соединяя точки вместе, мы формируем поверхность Марса.
            if (previousPointY == LAND_Y) {
            	flatGroundLeftX = previousPointX;
            	flatGroundRightX = LAND_X;
            	flatGroundY = LAND_Y;
            } else {
            	previousPointX = LAND_X;
            	previousPointY = LAND_Y;
            }
        }
        
        // игровой цикл
        while (true) {
        	
            int X = in.nextInt();
            int Y = in.nextInt();
            int HS = in.nextInt(); // горизонтальная скорость  (в м/с), может быть отрицательной
            int VS = in.nextInt(); // вертикальная скорость (в м/с) , может быть отрицательной
            int F = in.nextInt(); // кол-во сотавшегося топлива в литрах.
            int R = in.nextInt(); // угол поворота в градусах (от -90 до 90).
            int P = in.nextInt(); // сила тяги (от 0 до 4).

            if (isMarsLanderFlyingOverFlatGround(X, flatGroundLeftX, flatGroundRightX)) {
            	
            	if (isMarsLanderAboutToLand(Y, flatGroundY)) {
            		R = 0;
            		P = 3;
            	} else if (areMarsLanderSpeedLimitsSatisfied(HS, VS)) {
            		R = 0;
            		P = 2;
            	} else {
            		R = calculateRotationToSlowDownMarsLander(HS, VS);
            		P = 4;
            	}
            
            } else {
            	
            	if (isMarsLanderFlyingInTheWrongDirection(X, HS, flatGroundLeftX, flatGroundRightX) || isMarsLanderFlyingTooFastTowardsFlatGround(HS)) {
            		R = calculateRotationToSlowDownMarsLander(HS, VS);
            		P = 4;
            	} else if (isMarsLanderFlyingTooSlowTowardsFlatGround(HS)) {
            		R = calculateRotationToSpeedUpMarsLanderTowardsFlatGround(X, flatGroundLeftX, flatGroundRightX);
            		P = 4;
            	} else {
            		R = 0;
            		P = calculateThrustPowerToFlyTowardsFlatGround(VS);
            	}
            	
            }
            
            System.out.println(R + " " + P); // R P. R желаемый угол поворота. P желаемая мощность тяги.
        }
        
    }

	private static boolean isMarsLanderFlyingOverFlatGround(int marsLanderX, int flatGroundLeftX, int flatGroundRightX) {
		return marsLanderX >= flatGroundLeftX && marsLanderX <= flatGroundRightX;
	}
	
	private static boolean isMarsLanderAboutToLand(int marsLanderY, int flatGroundY) {
		return marsLanderY < flatGroundY + SECURITY_DISTANCE_FROM_FLAT_GROUND;
	}
	
	private static boolean areMarsLanderSpeedLimitsSatisfied(int marsLanderHorizontalSpeed, int marsLanderVerticalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) <= (MAX_HORIZONTAL_SPEED - EPSILON) && Math.abs(marsLanderVerticalSpeed) <= (MAX_VERTICAL_SPEED - EPSILON);
	}
	
	private static int calculateRotationToSlowDownMarsLander(int horizontalSpeed, int verticalSpeed) {
		double speed = Math.sqrt(Math.pow(horizontalSpeed, 2) + Math.pow(verticalSpeed, 2));
		double rotationAsRadians = Math.asin((double) horizontalSpeed / speed);
		return (int) Math.toDegrees(rotationAsRadians);
	}
	
	private static boolean isMarsLanderFlyingInTheWrongDirection(int marsLanderX, int marsLanderHorizontalSpeed, int flatGroundLeftX, int flatGroundRightX) {
		
		if (marsLanderX < flatGroundLeftX && marsLanderHorizontalSpeed < 0) {
			return true;
		}
		
		if (marsLanderX > flatGroundRightX && marsLanderHorizontalSpeed > 0) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isMarsLanderFlyingTooFastTowardsFlatGround(int marsLanderHorizontalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) > (MAX_HORIZONTAL_SPEED * 4);
	}
	
	private static boolean isMarsLanderFlyingTooSlowTowardsFlatGround(int marsLanderHorizontalSpeed) {
		return Math.abs(marsLanderHorizontalSpeed) < (MAX_HORIZONTAL_SPEED * 2);
	}
	
	private static int calculateRotationToSpeedUpMarsLanderTowardsFlatGround(int marsLanderX, int flatGroundLeftX, int flatGroundRightX) {
		
		if (marsLanderX < flatGroundLeftX) {
			return - (int) Math.toDegrees(Math.acos(GRAVITY / 4.0)); 
		}
		
		if (marsLanderX > flatGroundRightX) {
			return + (int) Math.toDegrees(Math.acos(GRAVITY / 4.0)); 
		}
		
		return 0;
	}
	
	private static int calculateThrustPowerToFlyTowardsFlatGround(int marsLanderVerticalSpeed) {
		return (marsLanderVerticalSpeed >= 0) ? 3 : 4;
	}
	
}


