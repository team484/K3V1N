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
    //private double lastX = 0.0;
    private boolean strideMode = true;
    private boolean rotMode = false;
    private boolean isInit = true;
    private boolean isRotInit = true;
    private int countIter = 0;

    public AutoCenterRobot() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        
        transPid = new PIDController(RobotSettings.visionTransKP, RobotSettings.visionTransKI, RobotSettings.visionTransKD, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                // Distance from centerpoint
                return VisionInterface.getDriveDirection(RobotSettings.cameraWidth / 2);
            }

        }, (d) -> {
        	//positive d goes right
            Robot.driveTrain.driveWithValues(90.0, d, 0.0);
        });
        
        rotPid = new PIDController(RobotSettings.visionRotKP, RobotSettings.visionRotKI, RobotSettings.visionRotKD, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                return VisionInterface.getLookDirection(RobotSettings.cameraWidth / 2);
            }

        }, (d) -> {
            Robot.driveTrain.driveWithValues(90.0, d, 0.0);
        }); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	rotPid.setSetpoint(0.0);
    	transPid.setSetpoint(0.0);
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(strideMode){
        	if(isInit){
        		isInit = false;
        		transPid.reset();
        		transPid.enable();
        	}else if(VisionInterface.getDriveDirection(RobotSettings.cameraWidth / 2.0) > -20 && VisionInterface.getDriveDirection(RobotSettings.cameraWidth / 2.0) < 20){
        		strideMode = false;
        		transPid.reset();
        		transPid.disable();
        		rotMode = true;
        		isRotInit = true;
        		countIter++;
        	}else{
        		//code
        	}
        } if(rotMode){
        	if(isRotInit){
        		isRotInit = false;
        		rotPid.reset();
        		rotPid.enable();
        	}else if(VisionInterface.getLookDirection(RobotSettings.cameraWidth / 2) > -20 && VisionInterface.getLookDirection(RobotSettings.cameraWidth / 2) < 20){
        		rotMode = false;
        		strideMode = true;
        		isInit = true;
        		rotPid.reset();
        		rotPid.disable();
        	}else {
        		//code
        	}
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return countIter >= 3;
    }
    protected void end() {}
    protected void interrupted() {}
    
    /*
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
        
    } */
}
