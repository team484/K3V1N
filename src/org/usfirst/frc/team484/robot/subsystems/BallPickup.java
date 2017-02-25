package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;
import org.usfirst.frc.team484.robot.commands.BallPickupDoNothing;

import edu.wpi.first.wpilibj.Relay;
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
    int i = 0;
    public void pickup() {
    	if (i < 10) {
        	Robot.io.pickupLED.set(Relay.Value.kForward);
        	i++;
    	} else if (i < 20) {
    		Robot.io.pickupLED.set(Relay.Value.kOff);
    		i++;
    	} else {
    		i = 0;
    	}
    	Robot.io.pickupMotor.set(RobotSettings.pickupSpeed);
    }
    public void doNothing() {
    	Robot.io.pickupMotor.set(0.0);
    	Robot.io.pickupLED.set(Relay.Value.kForward);
    }
    public void reverse() {
    	Robot.io.pickupMotor.set(1.0);
    	Robot.io.pickupLED.set(Relay.Value.kOff);
    }
}

