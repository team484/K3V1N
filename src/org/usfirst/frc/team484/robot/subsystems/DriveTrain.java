package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;
import org.usfirst.frc.team484.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;
import vision.VisionInterface;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	//TODO: Add method to rotate wheels into a defensive position to avoid being pushed by enemy robots.
	
	//private double prevTwist = 0.0;
	private double startAngle = 0.0;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DriveWithJoystick());
    }
    
    public void driveWithJoystick() {
    	/*
    	double rotation = Robot.driveStick.getDirectionDegrees();
    	double magnitude = Robot.driveStick.getMagnitude();
    	double twist = -Math.pow(Robot.driveStick.getTwist(), 3) / Math.abs(Robot.driveStick.getTwist());
    	
    	// TODO: what is the domain of magnitude? Is it always > 0?
    	double fixedMag = Math.abs(magnitude) > RobotSettings.EPSILON ? magnitude : 0.0;
    	double fixedTwist = Math.abs(twist) > RobotSettings.EPSILON ? prevTwist = twist : prevTwist;
    	
    	
    	Robot.swerve.drive(rotation, fixedMag, fixedTwist);
    	*/
    	
    	Robot.swerve.drive(Robot.IO.driveStick.getDirectionDegrees(), Robot.IO.driveStick.getMagnitude(), -Math.pow(Robot.IO.driveStick.getTwist(), 3)/ Math.abs(Robot.IO.driveStick.getTwist()));
    	
    }
    
    public void doNothing(){
    	Robot.swerve.drive(0,0,0);
    }
    
    public void driveWithValues(double deg, double mag, double rot){
    	Robot.swerve.drive(deg, mag, rot);
    }
    
    public void resetMotors(){
    	Robot.swerve.setupWeels();
    }
    public double getRobotAngle() {
    	return (Robot.IO.topGyro.getAngle() - Robot.IO.bottomGyro.getAngle()) / 2.0;
    }
    public void driveWithGyro() {
    	 Robot.swerve.drive(Robot.IO.driveStick.getDirectionDegrees() + (startAngle - getRobotAngle()), Robot.IO.driveStick.getMagnitude(), -Math.pow(Robot.IO.driveStick.getTwist(), 3) / Math.abs(Robot.IO.driveStick.getTwist()));
    }
    public void setAngle(double ang) {
    	startAngle = ang;
    }
    
}

