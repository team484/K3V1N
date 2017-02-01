package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	//TODO: Add method to rotate wheels into a defensive position to avoid being pushed by enemy robots.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DriveWithJoystick());
    }
    public void driveWithJoystick() {
    	Robot.swerve.drive(Robot.driveStick.getDirectionDegrees(), Robot.driveStick.getMagnitude(), -Math.pow(Robot.driveStick.getTwist(), 3) / Math.abs(Robot.driveStick.getTwist()));
    	
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
    
    
    
}

