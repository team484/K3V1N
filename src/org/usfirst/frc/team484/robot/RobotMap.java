package org.usfirst.frc.team484.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static final int frontLeftEncA = 0;
	public static final int frontRightEncA = 4;
	public static final int rearleftEncA = 2 ; 
	public static final int rearRightEncA = 6;
	public static final int frontLeftEncB = 1;
	public static final int frontRightEncB = 5;
	public static final int rearleftEncB = 3;
	public static final int rearRightEncB = 7;
	
	public static final int frontLeftTransMotor = 1;
	public static final int frontRightTransMotor = 5;
	public static final int rearleftTransMotor = 3;
	public static final int rearRightTransMotor = 7;
	
	public static final int frontLeftRotationalMotor = 2;
	public static final int frontRightRotationalMotor = 6;
	public static final int rearleftRotationalMotor = 4;
	public static final int rearRightRotationalMotor = 8;
	
	public static final int agitatorMotor = 2;
	
	public static final int shooterMotor = 9;
	public static final int pickupMotor = 3;
	
	public static final int climbMotorA = 0;
	public static final int climbMotorB = 1;
	
	public static final int shooterEncA = 8;
	public static final int shooterEncB = 9;
	
	public static final int driveStick = 0;
	public static final int operatorStick = 1;
	
	public static final int infraredSensor = 2;
	
	public static final int topGyro = 0;
	public static final int bottomGyro = 1;
	
	public static final String cam0 = "cam0";
	public static final String cam1 = "cam3";
	


}

