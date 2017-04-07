package org.usfirst.frc.team484.robot.subsystems;

import org.usfirst.frc.team484.robot.Robot;
import org.usfirst.frc.team484.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
// TODO: Creep mode -- Need multiplier
public class DriveTrain extends Subsystem {
	public double rot = 0.0;
	public double transX = 0.0;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
		
	//private double prevTwist = 0.0;
	private double startAngle = 0.0;
	private double forwardAngle = 0.0;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DriveWithJoystick());
    }
    
    public void driveWithJoystick() {
    	double ang = Robot.io.driveStick.getDirectionDegrees();
    	//if(ang + forwardAngle >= 360){
    		//ang = (ang + forwardAngle) - (Math.floor((int)(forwardAngle + ang) % (int)360.0) * 360);
    	//}else ang += forwardAngle;
    	//System.out.println(ang);
    	Robot.swerve.drive(ang + forwardAngle, Math.pow(Robot.io.driveStick.getMagnitude(),2) * Math.signum(Robot.io.driveStick.getMagnitude()), 0.5 * -Math.pow(Robot.io.driveStick.getTwist(), 3)/ Math.abs(Robot.io.driveStick.getTwist()));
    }
    public void doNothing(){
    	Robot.swerve.drive(0,0,0);
    }
    public void valuesDriveWithGyro(double rot, double speed, double angle) {
    	Robot.swerve.drive(angle - getRobotAngle(), speed, rot);
    }
    public void driveWithValues(double deg, double mag, double rot){
    	Robot.swerve.drive(deg, mag, rot);
    } 
    public void resetMotors(){
    	Robot.swerve.setupWeels();
    }
    public double getRobotAngle() {
    	return (Robot.io.topGyro.getAngle() - Robot.io.bottomGyro.getAngle()) / 2.0;
    }
    public void driveWithGyro() {
    	 Robot.swerve.drive(Robot.io.driveStick.getDirectionDegrees() + (startAngle - getRobotAngle()) + forwardAngle, Robot.io.driveStick.getMagnitude(), -Math.pow(Robot.io.driveStick.getTwist(), 3) / Math.abs(Robot.io.driveStick.getTwist()));
    }
    public void pointWheels(double angle) {
    	Robot.swerve.pointAllWheels(angle);
    }
    public void setAngle(double ang) {
    	startAngle = ang;
    }
    public void driveArc(double centerX, double centerY, double velocity){
    	Robot.swerve.driveRadially(centerX, centerY, velocity);
    }
    public void setFrontAngle(double ang) {
    	forwardAngle = ang;
    }
    public void driveWithAssignedValues(double speed) {
    	double angle = Math.toDegrees(Math.atan2(-speed,transX));
    	driveWithValues(angle, Math.sqrt(speed * speed + transX * transX), -rot);
    }
    public double getFrontAngle() {
    	return forwardAngle;
    }
}

