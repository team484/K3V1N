package org.usfirst.frc.team484.robot.commands;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import vision.CameraSettings;
import vision.Contour;
import vision.calculators.GearCalculator;

/**
 *TODO: How accurate is uing two gyros?
 *Can we replace a large portion of this code by using a gyro?
 */
public class AutoCenterRobot extends Command {
    
    PIDController transPid;
    //private double lastX = 0.0;
 

    public AutoCenterRobot() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        
        transPid = new PIDController(RobotSettings.visionTransKP, RobotSettings.visionTransKI, RobotSettings.visionTransKD, new PIDSource() {
            
            @Override public void setPIDSourceType(PIDSourceType pidSource) {}
            @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }
            
            @Override
            public double pidGet() {
                // Distance from centerpoint
            	return GearCalculator.run(Contour.getContoursFromTable(Robot.io.itab), RobotSettings.camSettings).inchesX;
            }

        }, new PIDOutput() {
        	
			
			@Override
			public void pidWrite(double output) {
				// TODO Auto-generated method stub
				Robot.driveTrain.driveWithValues(90.0, output, 0.0);
				
			}
		});
        /*
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
	*/
    }
    // Called just before this Command runs the first time
    protected void initialize() {
    	transPid.setSetpoint(0.0);
    	//transPid.setSetpoint(0.0);
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /*
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
        */
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return GearCalculator.run(Contour.getContoursFromTable(Robot.io.itab), RobotSettings.camSettings).inchesX < RobotSettings.errThresh && GearCalculator.run(Contour.getContoursFromTable(Robot.io.itab), RobotSettings.camSettings).inchesX > -RobotSettings.errThresh;
    }
    protected void end() {
    	Robot.driveTrain.doNothing();
    }
    protected void interrupted() {
    	end();
    }
    
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
