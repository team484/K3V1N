package org.usfirst.frc.team484.robot.commands;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDriveAngleVision extends Command {

	double deg;
	double mag;
	PIDController rotPID;
	PIDController transPID;
    public JoystickDriveAngleVision(double deg, double mag) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	Robot.io.topGyro.reset();
    	Robot.io.bottomGyro.reset();
    	this.deg = deg;
    	this.mag = mag;
    	requires(Robot.driveTrain);
    	transPID = new PIDController(0.04, 0, 0.015, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {}
			
			@Override
			public double pidGet() {
				try {
				if (Robot.gearResults != null) {
				return Robot.gearResults.inchesX;
				}
				return 0;
				} catch (Exception e) {
					System.out.println("No Offset");
					return 0;
				}
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				// TODO Auto-generated method stub
				return PIDSourceType.kDisplacement;
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
		    	Robot.driveTrain.transX = -output;
			}
		});
    	rotPID = new PIDController(RobotSettings.rotateKP, RobotSettings.rotateKI, RobotSettings.kD, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {}
			
			@Override
			public double pidGet() {
				return Robot.driveTrain.getRobotAngle();
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				// TODO Auto-generated method stub
				return PIDSourceType.kDisplacement;
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
		    	Robot.driveTrain.rot = output;
			}
		});
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	rotPID.reset();
    	rotPID.setSetpoint(-Robot.driveTrain.getRobotAngle());
    	rotPID.enable();
    	transPID.reset();
    	transPID.setSetpoint(0);
    	transPID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.driveWithAssignedValues(-Robot.io.driveStick.getY());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	rotPID.disable();
    	transPID.disable();
    	Robot.driveTrain.rot = 0;
    	Robot.driveTrain.transX = 0;
    	Robot.driveTrain.doNothing();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
