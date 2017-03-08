package org.usfirst.frc.team484.robot.commands;

import org.usfirst.frc.team484.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetRobotFront extends Command {
	double ang;
	public enum Side {
		FRONT,
		RIGHT,
		LEFT,
		REAR;
	}
    public SetRobotFront(double ang) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	this.ang = ang;
    }
    public SetRobotFront(Side side){
    	requires(Robot.driveTrain);
    	switch(side) {
    	case FRONT:
    		this.ang = 0.0;
    		break;
    	case RIGHT:
    		this.ang = 90.0;
    		break;
    	case LEFT:
    		this.ang = 270.0;
    		break;
    	case REAR:
    		this.ang = 180.0;
    		break;
    	}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.setAngle(ang);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
