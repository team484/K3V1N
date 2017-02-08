package org.usfirst.frc.team484.robot.commands;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import vision.DriveDirection;
import vision.VisionInterface;

/**
 *
 */
public class AutoCenterRobot extends Command {
    
    PIDController transPid;
    PIDController rotPid;
    private double lastX = 0.0;
    private boolean strideMode = false;

    public AutoCenterRobot() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        
        transPid = new PIDController(0, 0, 0, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                // Distance from centerpoint
                return VisionInterface.;
            }

        }, (d) -> {
            //
        });
        
        rotPid = new PIDController(0, 0, 0, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                //
                return 0;
            }

        }, (d) -> {
            //
        }); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }
    protected void end() {}
    protected void interrupted() {}
    
    private void step() {
        if(strideMode) {
            double mx = 0;
            double center = 0.0;
            
            // If we have the point in this time frame
            if(mx < center && lastX > center) {
                // Switch modes
                strideMode = false;
            } else {
                // Either 90 or 270, depending on which side we errored to
                
                double deg = 90;
//                this.driveWithValues(deg, 0, 0);
            }
            
            lastX = mx;
        } else {
            switch(VisionInterface.getLookDirection()) {
                case Center:
                    break;
                case Left:
//                    this.driveWithValues(0, 0, 270);
                    break;
                case Right:
//                    this.driveWithValues(0, 0, 90);
                    break;
                default:
                    break;
                
            }
        }
    }
}
