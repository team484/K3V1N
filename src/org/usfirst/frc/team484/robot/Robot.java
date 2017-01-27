
package org.usfirst.frc.team484.robot;

import org.usfirst.frc.team484.robot.commands.resetWheels;
import org.usfirst.frc.team484.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory. 
 */
public class Robot extends IterativeRobot {
	
	 
	public static OI oi;
	public static Talon frontLeftTransMotor = new Talon(RobotMap.frontLeftTransMotor);
	public static Talon frontRightTransMotor = new Talon(RobotMap.frontRightTransMotor);
	public static Talon rearLeftTransMotor = new Talon(RobotMap.rearleftTransMotor);
	public static Talon rearRightTransMotor = new Talon(RobotMap.rearRightTransMotor);
	
	public static Talon frontLeftRotationalMotor = new Talon(RobotMap.frontLeftRotationalMotor);
	public static Talon frontRightRotationalMotor = new Talon(RobotMap.frontRightRotationalMotor);
	public static Talon rearLeftRotationalMotor = new Talon(RobotMap.rearleftRotationalMotor);
	public static Talon rearRightRotationalMotor = new Talon(RobotMap.rearRightRotationalMotor);
	
	public static Encoder frontLeftEnc = new Encoder(RobotMap.frontLeftEncA, RobotMap.frontLeftEncB);
	public static Encoder frontRightEnc = new Encoder(RobotMap.frontRightEncA, RobotMap.frontRightEncB);
	public static Encoder rearLeftEnc = new Encoder(RobotMap.rearleftEncA, RobotMap.rearleftEncB);
	public static Encoder rearRightEnc = new Encoder(RobotMap.rearRightEncA, RobotMap.rearRightEncB);
	
	public static Joystick driveStick = new Joystick(RobotMap.driveStick);
	public static Joystick operatorStick = new Joystick(RobotMap.operatorStick);
	public static SwerveDrive swerve = new SwerveDrive(RobotSettings.kP, RobotSettings.kI, RobotSettings.kD, frontLeftEnc, rearLeftEnc, frontRightEnc, rearRightEnc, frontLeftRotationalMotor, rearLeftRotationalMotor, frontRightRotationalMotor, rearRightRotationalMotor, frontLeftTransMotor, rearLeftTransMotor, frontRightTransMotor, rearRightTransMotor, false);
	public static AnalogInput infraredSensor = new AnalogInput(RobotMap.infraredSensor);
	public static DriveTrain driveTrain = new DriveTrain();
	public static resetWheels reset = new resetWheels();
	
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		frontLeftEnc.reset();
		rearLeftEnc.reset();
		frontRightEnc.reset();
		rearRightEnc.reset();
		
		
		frontLeftEnc.setDistancePerPulse(0.86694762);
		rearLeftEnc.setDistancePerPulse(0.86694762);
		frontRightEnc.setDistancePerPulse(0.86694762);
		rearRightEnc.setDistancePerPulse(0.86694762);
		swerve.setWheelbaseDimensions(27.0, 17.5);
		
		
		//chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) autonomousCommand.cancel();
		//swerve.enablePID();
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		System.out.println(infraredSensor.getAverageVoltage());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}

