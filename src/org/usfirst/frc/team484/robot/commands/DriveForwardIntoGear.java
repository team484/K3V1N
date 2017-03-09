package org.usfirst.frc.team484.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Auto to drive into gear peg,
 *  requires the robot to be lined up perfectly and
 *  for the wheels to spin at the same rate.
 */
public class DriveForwardIntoGear extends CommandGroup {

    public DriveForwardIntoGear() {
    	
		addSequential(new PointWheels(270.0));
		addSequential(new AutoDriveAngle(270.0, .3));
    }
}
