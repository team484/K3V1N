package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void driveWithJoystick() {
    	Robot.swerve.drive(Robot.driveStick.getDirectionDegrees(), Robot.driveStick.getMagnitude(), Robot.driveStick.getTwist());
    }
    public void doNothing(){
    	
    }
    public void driveWithValues(double deg, double mag, double rot){
    		Robot.swerve.drive(deg, mag, rot);
    }
    
    
}

