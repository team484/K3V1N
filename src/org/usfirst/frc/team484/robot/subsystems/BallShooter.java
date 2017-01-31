package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.commands.ShooterDoNothing;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallShooter extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ShooterDoNothing());
;    }
    public void setShooterSpeed(double speed) {
    	Robot.shooterMotor.set(speed);
    }
    public void doNothing() {
    	Robot.shooterMotor.set(0.0);
    }
}

