
package org.usfirst.frc.team484.robot;

import org.opencv.core.Mat;
import org.usfirst.frc.team484.robot.commands.AutoCrossLine;
import org.usfirst.frc.team484.robot.commands.AutoDoNothing;
import org.usfirst.frc.team484.robot.commands.AutoGearPlace;
import org.usfirst.frc.team484.robot.commands.AutoLeftPeg;
import org.usfirst.frc.team484.robot.commands.AutoRightPeg;
import org.usfirst.frc.team484.robot.commands.AutoVisionGear;
import org.usfirst.frc.team484.robot.subsystems.Agitator;
import org.usfirst.frc.team484.robot.subsystems.BallPickup;
import org.usfirst.frc.team484.robot.subsystems.BallShooter;
import org.usfirst.frc.team484.robot.subsystems.Climber;
import org.usfirst.frc.team484.robot.subsystems.DriveTrain;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import vision.CameraSettings;
import vision.VisionResults;
import vision.VisionThread;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory. 
 */
public class Robot extends IterativeRobot {
	public static CameraSettings gearCamSettings;
	public static CameraSettings shooterCamSettings;
	public static VisionThread gearVisionThread;
	public static VisionThread shooterVisionThread;
	public static VisionResults gearResults;

	public static OI oi;
	public static RobotIO io;

	public static SwerveDrive swerve;
	public static DriveTrain driveTrain;
	public static BallShooter ballShooter;
	public static BallPickup ballPickup;
	public static Climber climber;
	public static Agitator agitator;


	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		io = new RobotIO();
		swerve = new SwerveDrive(RobotSettings.kP, RobotSettings.kI, RobotSettings.kD, io.frontLeftEnc, io.rearLeftEnc, io.frontRightEnc, io.rearRightEnc, io.frontLeftRotationalMotor, io.rearLeftRotationalMotor, io.frontRightRotationalMotor, io.rearRightRotationalMotor, io.frontLeftTransMotor, io.rearLeftTransMotor, io.frontRightTransMotor, io.rearRightTransMotor, false);
		driveTrain = new DriveTrain();
		ballShooter = new BallShooter();
		ballPickup = new BallPickup();
		climber = new Climber();
		agitator = new Agitator();


		oi = new OI();

		io.frontLeftEnc.reset();
		io.rearLeftEnc.reset();
		io.frontRightEnc.reset();
		io.rearRightEnc.reset();

		io.frontLeftEnc.setDistancePerPulse(RobotSettings.wheelEncDistancePerPulse);
		io.rearLeftEnc.setDistancePerPulse(RobotSettings.wheelEncDistancePerPulse);
		io.frontRightEnc.setDistancePerPulse(RobotSettings.wheelEncDistancePerPulse);
		io.rearRightEnc.setDistancePerPulse(RobotSettings.wheelEncDistancePerPulse);
		io.shooterEnc.setDistancePerPulse(.05);


		swerve.setWheelbaseDimensions(RobotSettings.wheelBaseX, RobotSettings.wheelBaseY);

		io.topGyro.initGyro();
		//io.topGyro.calibrate();
		io.bottomGyro.initGyro();
		//io.bottomGyro.calibrate();

		chooser.addDefault("DoNothing", new AutoDoNothing());
		chooser.addObject("Cross Line", new AutoCrossLine());
		chooser.addObject("RightGearPeg", new AutoRightPeg());
		chooser.addObject("LeftGearPeg", new AutoLeftPeg());
		chooser.addObject("CenterGearPeg", new AutoGearPlace());
		SmartDashboard.putData("Auto mode", chooser);

		gearCamSettings = new CameraSettings(320, 240, 0, 0, 0, 0, 0, 0.00384);
		shooterCamSettings = new CameraSettings(320, 240, 0, 0, 0, 0, 0, 0.00218);
		gearVisionThread = new VisionThread(VisionThread.Camera.GEAR, gearCamSettings, "gear");
		gearVisionThread.start();
		shooterVisionThread = new VisionThread(VisionThread.Camera.SHOOTER, shooterCamSettings, "shooter");
		shooterVisionThread.start();

		if (!RobotSettings.isBackupBot) {
			swerve.toggleVoltageCompensation(true, 12);
		}

		Thread t = new Thread(() -> {
			try {
				boolean allowCam1 = true;
				boolean allowCam2 = false;
				UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(1);
				camera1.setResolution(320, 240);
				camera1.setFPS(30);
				camera1.setExposureAuto();
				camera1.setWhiteBalanceAuto();
				UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(0);
				camera2.setResolution(320, 240);
				camera2.setFPS(30);
				if (camera1.isConnected() && camera2.isConnected()) {
					CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
					CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
					CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);
					Mat image = new Mat();

					while (!Thread.interrupted()) {
						if (Robot.io.driveStick.getRawButton(5)) {
							allowCam1 = true;
							allowCam2 = false;
						}
						if (Robot.io.driveStick.getRawButton(6)) {
							allowCam2 = true;
							allowCam1 = false;
						}
						if (allowCam1) {
							cvSink2.setEnabled(false);
							cvSink1.setEnabled(true);
							cvSink1.grabFrame(image);
						} else if (allowCam2) {
							cvSink1.setEnabled(false);
							cvSink2.setEnabled(true);
							cvSink2.grabFrame(image);
						}

						outputStream.putFrame(image);

					}
				}
			} catch (Exception e) {
				System.err.println("Cameras not connected!");
			}
		});
		t.start();

	}

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
		autonomousCommand = new AutoVisionGear();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (!RobotSettings.isBackupBot) {
			swerve.toggleVoltageCompensation(true, 9);
		}
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
		if (!RobotSettings.isBackupBot) {
			swerve.toggleVoltageCompensation(true, 12);
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		gearResults = gearVisionThread.getResults();
		SmartDashboard.putNumber("gyro", driveTrain.getRobotAngle());
		SmartDashboard.putNumber("voltage", io.pdp.getVoltage());
		gearResults = gearVisionThread.getResults();
		SmartDashboard.putNumber("X-Offset", gearResults.inchesX);
		SmartDashboard.putNumber("Distance", gearResults.inchesZ);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}

