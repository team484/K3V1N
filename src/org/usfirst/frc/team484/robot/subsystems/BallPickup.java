package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;
import org.usfirst.frc.team484.robot.commands.BallPickupDoNothing;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallPickup extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new BallPickupDoNothing());
    }
    public void pickup() {
    	Robot.io.pickupMotor.set(RobotSettings.pickupSpeed);
    }
    public void doNothing() {
    	Robot.io.pickupMotor.set(0.0);
    }
    public void reverse() {
    	Robot.io.pickupMotor.set(-1.0);
    }
}

