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
	public static int frontLeftEncA;
	public static int frontRightEncA;
	public static int rearleftEncA;
	public static int rearRightEncA;
	public static int frontLeftEncB;
	public static int frontRightEncB;
	public static int rearleftEncB;
	public static int rearRightEncB;
	
	public static int frontLeftTransMotor;
	public static int frontRightTransMotor;
	public static int rearleftTransMotor;
	public static int rearRightTransMotor;
	
	public static int frontLeftRotationalMotor;
	public static int frontRightRotationalMotor;
	public static int rearleftRotationalMotor;
	public static int rearRightRotationalMotor;
	
	public static int driveStick;
	public static int operatorStick;
	
	public static int infraredSensor;


}
