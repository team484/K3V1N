package org.usfirst.frc.team484.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCrossLineReturn extends CommandGroup {
	private static final double SPEED = 0.4;
	public enum PathType {
		RIGHT,
		LEFT,
		FORWARDS;
	}

    public AutoCrossLineReturn(PathType path) {
    	
    	switch(path) {
    	case RIGHT:
    		addSequential(new AutoDriveAngle(0, SPEED), 2);
    		addSequential(new AutoDriveAngle(90, SPEED), 2);
    		addSequential(new AutoDriveAngle(0, SPEED), 4);
    		addSequential(new AutoDriveAngle(0, -SPEED), 2);
    		// TODO: Line up with peg?
    		break;
    	case LEFT:
    		addSequential(new AutoDriveAngle(0, SPEED), 2);
    		addSequential(new AutoDriveAngle(90, -SPEED), 2);
    		addSequential(new AutoDriveAngle(0, SPEED), 4);
    		addSequential(new AutoDriveAngle(0, -SPEED), 2);
    		break;
    	case FORWARDS:
    		addSequential(new AutoDriveAngle(0, SPEED), 4);
    		addSequential(new AutoDriveAngle(0, SPEED), 2);
    		break;
    	}
    	
    }
}
