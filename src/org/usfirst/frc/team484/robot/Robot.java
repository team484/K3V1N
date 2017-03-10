
package org.usfirst.frc.team484.robot;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.usfirst.frc.team484.robot.commands.AutoCrossBackForward;
import org.usfirst.frc.team484.robot.commands.AutoCrossBackLeft;
import org.usfirst.frc.team484.robot.commands.AutoCrossBackRight;
import org.usfirst.frc.team484.robot.commands.AutoDoNothing;
import org.usfirst.frc.team484.robot.commands.AutoDriveForTime;
import org.usfirst.frc.team484.robot.commands.AutoDriveForwardIntoGear;
import org.usfirst.frc.team484.robot.commands.AutoGearPlace;
import org.usfirst.frc.team484.robot.commands.AutoLeftPeg;
import org.usfirst.frc.team484.robot.commands.AutoRightPeg;
import org.usfirst.frc.team484.robot.subsystems.Agitator;
import org.usfirst.frc.team484.robot.subsystems.BallPickup;
import org.usfirst.frc.team484.robot.subsystems.BallShooter;
import org.usfirst.frc.team484.robot.subsystems.Climber;
import org.usfirst.frc.team484.robot.subsystems.DriveTrain;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

	public static PowerDistributionPanel pdp = new PowerDistributionPanel();
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
		chooser.addObject("RightGearPeg", new AutoRightPeg());
		chooser.addObject("LeftGearPeg", new AutoLeftPeg());
		chooser.addObject("CenterGearPeg", new AutoGearPlace());
		chooser.addObject("CrossBackRight", new AutoCrossBackRight());
		chooser.addObject("CrossBackLeft", new AutoCrossBackLeft());
		chooser.addObject("CrossBackForward", new AutoCrossBackForward());
		chooser.addObject("DeadReckonGear", new AutoDriveForwardIntoGear());
		chooser.addObject("timeDrve", new AutoDriveForTime());

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
		if (!RobotSettings.isBackupBot) {
			((CANTalon) io.frontLeftRotationalMotor).enableBrakeMode(false);
			((CANTalon) io.rearLeftRotationalMotor).enableBrakeMode(false);
			((CANTalon) io.frontRightRotationalMotor).enableBrakeMode(false);
			((CANTalon) io.rearRightRotationalMotor).enableBrakeMode(false);
			((CANTalon) io.frontLeftTransMotor).enableBrakeMode(false);
			((CANTalon) io.rearLeftTransMotor).enableBrakeMode(false);
			((CANTalon) io.frontRightTransMotor).enableBrakeMode(false);
			((CANTalon) io.rearRightTransMotor).enableBrakeMode(false);
		}
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
		if (!RobotSettings.isBackupBot) {
			((CANTalon) io.frontLeftRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.rearLeftRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.frontRightRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.rearRightRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.frontLeftTransMotor).enableBrakeMode(true);
			((CANTalon) io.rearLeftTransMotor).enableBrakeMode(true);
			((CANTalon) io.frontRightTransMotor).enableBrakeMode(true);
			((CANTalon) io.rearRightTransMotor).enableBrakeMode(true);
		}
		autonomousCommand = chooser.getSelected();
		SmartDashboard.putString("Robot Test", "Auto mode");

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
		if (!RobotSettings.isBackupBot) {
			((CANTalon) io.frontLeftRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.rearLeftRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.frontRightRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.rearRightRotationalMotor).enableBrakeMode(true);
			((CANTalon) io.frontLeftTransMotor).enableBrakeMode(true);
			((CANTalon) io.rearLeftTransMotor).enableBrakeMode(true);
			((CANTalon) io.frontRightTransMotor).enableBrakeMode(true);
			((CANTalon) io.rearRightTransMotor).enableBrakeMode(true);
		}
		SmartDashboard.putString("Robot Test", "Teleop-mode");
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

	
	
	int testStage = 0;
	int stageTimer = 0;
	ArrayList<String> errors = new ArrayList<String>();
	double startFL = 0;
	double startRL = 0;
	double startFR = 0;
	double startRR = 0;
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
		switch (testStage) {
		case 0:
			SmartDashboard.putString("Robot Test", "Press trigger to begin");
			if (io.driveStick.getTrigger()) {
				testStage = 1;
				stageTimer = 0;
				errors.clear();
			}
			break;
		case 1:
			SmartDashboard.putString("Robot Test", "Checking Systems...");
			if (stageTimer == 0) {
				startFL = io.frontLeftEnc.getDistance();
				startRL = io.rearLeftEnc.getDistance();
				startFR = io.frontRightEnc.getDistance();
				startRR = io.rearRightEnc.getDistance();
			}
			if (stageTimer < 50) {
			io.frontLeftTransMotor.set(0.5);
			io.rearLeftTransMotor.set(0.5);
			io.frontRightTransMotor.set(0.5);
			io.rearRightTransMotor.set(0.5);
			io.frontLeftRotationalMotor.set(0.5);
			io.rearLeftRotationalMotor.set(0.5);
			io.frontRightRotationalMotor.set(0.5);
			io.rearRightRotationalMotor.set(0.5);
			io.shooterMotor.set(12);
			io.climbMotorA.set(12);
			io.climbMotorB.set(12);
			io.pickupMotor.set(12);
			io.agitateMotor.set(12);
			}
			stageTimer++;
			if (stageTimer > 50) {
				
				double MIN_CURRENT = 4;
				double MAX_CURRENT = 10;
				double MINR_CURRENT = 4;
				double MAXR_CURRENT = 10;
				double flCurrent = pdp.getCurrent(1);
				double rlCurrent = pdp.getCurrent(2);
				double frCurrent = pdp.getCurrent(3);
				double rrCurrent = pdp.getCurrent(4);
				
				double flRCurrent = pdp.getCurrent(5);
				double rlRCurrent = pdp.getCurrent(6);
				double frRCurrent = pdp.getCurrent(7);
				double rrRCurrent = pdp.getCurrent(8);
				
				double shooterCurrent = pdp.getCurrent(9);
				double MIN_Shooter_Current = 4;
				double MAX_Shooter_Current = 10;
				
				double climberACurrent = pdp.getCurrent(10);
				double climberBCurrent = pdp.getCurrent(11);
				double MIN_CLIMBER_CURRENT = 4;
				double MAX_CLIMBER_CURRENT = 10;
				
				double intakeCurrent = pdp.getCurrent(12);
				double MIN_INTAKE_CURRENT = 4;
				double MAX_INTAKE_CURRENT = 10;
				
				double agitateCurrent = pdp.getCurrent(13);
				double MIN_AGITATE_CURRENT = 4;
				double MAX_AGITATE_CURRENT = 10;
				
				if (flCurrent < MIN_CURRENT) {
					errors.add("Front left CIM may not be plugged in");
				} else if (flCurrent > MAX_CURRENT) {
					errors.add("Front left CIM had trouble spinning");
				}
				if (rlCurrent < MIN_CURRENT) {
					errors.add("Rear left CIM may not be plugged in");
				} else if (rlCurrent > MAX_CURRENT) {
					errors.add("Rear left CIM had trouble spinning");
				}
				if (frCurrent < MIN_CURRENT) {
					errors.add("Front right CIM may not be plugged in");
				} else if (frCurrent > MAX_CURRENT) {
					errors.add("Front right CIM had trouble spinning");
				}
				if (rrCurrent < MIN_CURRENT) {
					errors.add("Rear right CIM may not be plugged in");
				} else if (rrCurrent > MAX_CURRENT) {
					errors.add("Rear right CIM had trouble spinning");
				}
				
				
				if (flRCurrent < MINR_CURRENT) {
					errors.add("Front left rotation motor may not be plugged in");
				} else if (flCurrent > MAXR_CURRENT) {
					errors.add("Front left rotation motor had trouble spinning");
				} else if (Math.abs(startFL - io.frontLeftEnc.getDistance()) < 90) {
					errors.add("Front left encoder is not working");
				}
				if (rlRCurrent < MINR_CURRENT) {
					errors.add("Rear left rotation motor may not be plugged in");
				} else if (rlCurrent > MAXR_CURRENT) {
					errors.add("Rear left rotation motor had trouble spinning");
				} else if (Math.abs(startRL - io.rearLeftEnc.getDistance()) < 90) {
					errors.add("Rear left encoder is not working");
				}
				if (frRCurrent < MINR_CURRENT) {
					errors.add("Front right rotation motor may not be plugged in");
				} else if (frCurrent > MAXR_CURRENT) {
					errors.add("Front right rotation motor had trouble spinning");
				} else if (Math.abs(startFR - io.frontRightEnc.getDistance()) < 90) {
					errors.add("Front right encoder is not working");
				}
				if (rrRCurrent < MINR_CURRENT) {
					errors.add("Rear right rotation motor may not be plugged in");
				} else if (rrCurrent > MAXR_CURRENT) {
					errors.add("Rear right rotation motor had trouble spinning");
				} else if (Math.abs(startRR - io.rearRightEnc.getDistance()) < 90) {
					errors.add("Rear right encoder is not working");
				}
				
				if (shooterCurrent < MIN_Shooter_Current) {
					errors.add("Shooter motor may not be plugged in");
				} else if (shooterCurrent > MAX_Shooter_Current) {
					errors.add("Shooter motor had trouble spinning");
				} else if (Math.abs(io.shooterEnc.getRate()) < 20) {
					errors.add("Shooter encoder is not working");
				}
				
				if (climberACurrent < MIN_CLIMBER_CURRENT) {
					errors.add("Climber motor A may not be plugged in");
				} else if (climberACurrent > MAX_CLIMBER_CURRENT) {
					errors.add("Climber motor A had trouble spinning");
				}
				if (climberBCurrent < MIN_CLIMBER_CURRENT) {
					errors.add("Climber motor B may not be plugged in");
				} else if (climberBCurrent > MAX_CLIMBER_CURRENT) {
					errors.add("Climber motor B had trouble spinning");
				}
				
				if (intakeCurrent < MIN_INTAKE_CURRENT) {
					errors.add("Intake motor may not be plugged in");
				} else if (intakeCurrent > MAX_INTAKE_CURRENT) {
					errors.add("Intake motor had trouble spinning");
				}
				
				if (agitateCurrent < MIN_AGITATE_CURRENT) {
					errors.add("Agitator motor may not be plugged in");
				} else if (agitateCurrent > MAX_AGITATE_CURRENT) {
					errors.add("Agitator motor had trouble spinning");
				}
				System.out.println("Problems: ");
				if (errors.size() == 0) {
					System.out.println("No problems found!");
					SmartDashboard.putString("Robot Test", "No Problems Found");
				} else if (errors.size() == 1) {
					SmartDashboard.putString("Robot Test", errors.get(0));
					System.err.println(errors.get(0));
				} else {
					SmartDashboard.putString("Robot Test", "There were " + errors.size() + " errors found. Check console for more");
					for (String error : errors) {
						System.err.println(error);
					}
				}
				io.frontLeftTransMotor.set(0);
				io.rearLeftTransMotor.set(0);
				io.frontRightTransMotor.set(0);
				io.rearRightTransMotor.set(0);
				io.frontLeftRotationalMotor.set(0);
				io.rearLeftRotationalMotor.set(0);
				io.frontRightRotationalMotor.set(0);
				io.rearRightRotationalMotor.set(0);
				io.shooterMotor.set(0);
				io.climbMotorA.set(0);
				io.climbMotorB.set(0);
				io.pickupMotor.set(0);
				io.agitateMotor.set(0);
				
				if (errors.size() > 0) {
					testStage = -1;
				} else {
					testStage = 2;
				}
				stageTimer = 0;
			}
			break;
		case 2:
			if (stageTimer == 0) {
				io.topGyro.reset();
				io.bottomGyro.reset();
			}
			stageTimer++;
			SmartDashboard.putString("Robot Test", "Rotate robot, then press trigger");
			if (io.driveStick.getTrigger() && stageTimer > 30) {
				testStage = 3;
				stageTimer = 0;
			}
			break;
		case 3:
			if (io.topGyro.getAngle() + io.bottomGyro.getAngle() < 10) {
				if (Math.abs(io.topGyro.getAngle()) < 10) {
					errors.add("Error in top gyro (port 0)");
				}
				if (Math.abs(io.bottomGyro.getAngle()) < 10) {
					errors.add("Error in bottom gyro (port 1)");
				}
			} else {
				if (Math.abs(io.topGyro.getAngle()) - Math.abs(io.bottomGyro.getAngle()) > 0) {
					errors.add("Error in bottom gyro (port 1)");
				} else {
					errors.add("Error in top gyro (port 0)");
				}
			}
			testStage = -1;
			stageTimer = 0;
			break;
		case -1:
			//Test over
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void testInit() {
		testStage = 0;
		stageTimer = 0;
	}
}

