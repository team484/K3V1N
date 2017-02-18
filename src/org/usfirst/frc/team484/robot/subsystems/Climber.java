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
    	Robot.IO.climbMotorA.set(0);
    	Robot.IO.climbMotorB.set(0);
    }
    public void climb() {
    	Robot.IO.climbMotorA.set(RobotSettings.climberSpeed);
    	Robot.IO.climbMotorB.set(RobotSettings.climberSpeed);
    }
}

