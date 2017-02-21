package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    }
    public void doNothing() {
    	Robot.io.climbMotorA.set(0);
    	Robot.io.climbMotorB.set(0);
    }
    public void climb() {
    	Robot.io.climbMotorA.set(RobotSettings.climberSpeed);
    	Robot.io.climbMotorB.set(RobotSettings.climberSpeed);
    }
    public void climbJoysticks() {
    	Robot.io.climbMotorA.set(Robot.io.operatorStick.getY());
    	Robot.io.climbMotorB.set(-Robot.io.operatorStick.getY());
    }
}

