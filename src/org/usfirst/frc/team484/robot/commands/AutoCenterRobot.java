package org.usfirst.frc.team484.robot.commands;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/*
 * 
 */
public class AutoCenterRobot extends Command {
    
    PIDController transPid;
 

    public AutoCenterRobot() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        
        transPid = new PIDController(RobotSettings.visionTransKP, RobotSettings.visionTransKI, RobotSettings.visionTransKD, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                // Distance from centerpoint
            	return Robot.gearResults.inchesX;
            }

        }, new PIDOutput() {
   
			@Override
			public void pidWrite(double output) {
				// TODO Auto-generated method stub
				Robot.driveTrain.driveWithValues(0.0, output, 0.0);
			}
		});
        
        
    }
    // Called just before this Command runs the first time
    protected void initialize() {
    	transPid.setSetpoint(0.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(Robot.gearResults.inchesX == Double.NaN) return false;
    	else return RobotSettings.errThresh > Robot.gearResults.inchesX && Robot.gearResults.inchesX > -RobotSettings.errThresh;
    }
    protected void end() {
    	Robot.driveTrain.doNothing();
    }
    protected void interrupted() {
    	end();
    }
    
}
