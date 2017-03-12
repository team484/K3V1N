package org.usfirst.frc.team484.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoLeftPeg extends CommandGroup {

    public AutoLeftPeg() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	addSequential(new AutoDriveAngle(270, .65), 1.5);
    	addSequential(new AutoRotate(60), 1.5);
    	addSequential(new ZeroGyro(), 0.2);
		addSequential(new PointWheels(270), 0.3);
		addSequential(new AutoDriveAngleVision(270, 0.35), 0.5);
    	addSequential(new AutoDriveAngleVision(270, .31), 3.5); //90?
    	addSequential(new AutoDriveAngle(90, 0.4), 0.2);
    }
}
