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
	public static int frontLeftEncA = 0;
	public static int frontRightEncA = 4;
	public static int rearleftEncA = 2 ;
	public static int rearRightEncA = 6;
	public static int frontLeftEncB = 1;
	public static int frontRightEncB = 5;
	public static int rearleftEncB = 3;
	public static int rearRightEncB = 8;
	
	public static int frontLeftTransMotor = 0;
	public static int frontRightTransMotor = 2;
	public static int rearleftTransMotor = 1;
	public static int rearRightTransMotor = 3;
	
	public static int frontLeftRotationalMotor = 4;
	public static int frontRightRotationalMotor = 6;
	public static int rearleftRotationalMotor = 5;
	public static int rearRightRotationalMotor = 7;
	
	public static int driveStick = 0;
	public static int operatorStick = 1;
	
	public static int infraredSensor = 0;


}

