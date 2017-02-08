package org.usfirst.frc.team484.robot;

public class RobotSettings {

	public static double kP = 0.02;
	public static double kI = 0.0;
	public static double kD = 0.0;
	
	public static double shooterKP = 0.0008;
	public static double shooterKI = 0.0001;
	public static double shooterKD = 0.0;
	
	public static double rotateKP = 0.02;
	public static double rotateKI = 0.0;
	public static double rotateKD = 0.0;
	
	public static double visionRotKP;
	public static double visionRotKI;
	public static double visionRotKD;

	public static double visionTransKP;
	public static double visionTransKI;
	public static double visionTransKD;

	
	public static double wheelEncDistancePerPulse = 0.86694762;
	public static double shooterEncDistancePerPulse = 0.05;
	
	public static double wheelBaseX = 27.0;
	public static double wheelBaseY = 17.5;
	
	public static double pickupSpeed = 1.0; //Between -1.0 and 1.0
	public static double shooterSpeed = 0;
	public static double climberSpeed = 1.0;
	
	public static double deadzone = .05;
	
	public static double startAngle = 0.0;
	
	// FIXME
	public static double visionCenter = 0.0;
	
	public static final double EPSILON = 0.05; //"Follow your way of naming the settings?" "yes"
	public static final double cameraWidth = 1920;
} 

