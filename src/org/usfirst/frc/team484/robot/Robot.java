
package org.usfirst.frc.team484.robot;

import org.usfirst.frc.team484.robot.commands.AutoVisionGear;
import org.usfirst.frc.team484.robot.subsystems.Agitator;
import org.usfirst.frc.team484.robot.subsystems.BallPickup;
import org.usfirst.frc.team484.robot.subsystems.BallShooter;
import org.usfirst.frc.team484.robot.subsystems.Climber;
import org.usfirst.frc.team484.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
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
	public static RobotIO io;

	public static SwerveDrive swerve;
	public static DriveTrain driveTrain;
	public static BallShooter ballShooter;
	public static BallPickup ballPickup;
	public static Climber climber;
	public static Agitator agitate;
	//public static GripPipeline visionPipe = new GripPipeline();
	//public static VisionThread visionThread;
	//public static Object imgLock = new Object();
	//public static double[] centerX = new double[0];
	//public static double[] centerY = new double[0];
	//public static double[] area = new double[0];
	
	//public static CameraServer camServer;
	//public static CvSink visionVid = camServer.getVideo();


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
		agitate = new Agitator();
		//camServer = CameraServer.getInstance();
		//camServer = CameraServer.getInstance();
		//IO.shooterMotor.changeControlMode(TalonControlMode.Voltage);
		
		//camServer.putVideo("cam", 640, 480);
		
		//These Settings don't work! Write a startup script using v4l2-utils!
		//camera.setExposureManual(17);
		//camera.setBrightness(0);
		//camera.setWhiteBalanceManual(5000);
		/*
		visionThread = new VisionThread(camera1, new GripPipeline(), gripPipeline ->{
			synchronized(imgLock){
				centerX = new double[gripPipeline.filterContoursOutput().size()];
				centerY = new double[gripPipeline.filterContoursOutput().size()];
				area = new double[centerX.length];
				for (int i = 0; i < centerX.length; i++) {
					Rect r = Imgproc.boundingRect(gripPipeline.filterContoursOutput().get(i));
					centerX[i] = r.x;
					centerY[i] = r.y;
					area[i] = r.area();

				}
			}
		});
		*/
		oi = new OI();
		//visionThread.start();
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
		io.topGyro.calibrate();
		io.bottomGyro.initGyro();
		io.bottomGyro.calibrate();
		
		//io.led.set(Relay.Value.kOn);

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
		autonomousCommand = new AutoVisionGear();

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
		System.out.println(io.shooterEnc.getRate());
		/*
		double[] centerX;
		double[] centerY;
		double distance;
		synchronized (imgLock) {
			centerX = this.centerX;	
			centerY = this.centerY;
		}
		double area1 = -1;
		double area2 = -1;
		int pos1 = -1;
		int pos2 = -1;
		if (centerX.length > 1) {
			for (int i = 0; i < centerX.length; i++) {
				if (area[i] > area1) {
					area2 = area1;
					pos2 = pos1;
					area1 = area[i];
					pos1 = i;
				} else if (area[i] > area2) {
					area2 = area[i];
					pos2 = i;
				}
			}
			double trueCenter = (centerX[pos1] + centerX[pos2]) / 2.0;
			//System.out.println((area1 + area2) / 2.0);
			System.out.println("pos1" + centerX[pos1]);
			System.out.println("pos2" + centerX[pos2]);
			distance = ((RobotSettings.tapeHeight - RobotSettings.cameraVerticleOffset) * Math.toRadians(RobotSettings.degPerPix)) * (Math.tan(Math.PI / 2.0 - centerY[pos1]) + Math.tan(Math.PI / 2.0 - centerY[pos1])/2);

		} else {
			System.out.println(" I don't seee the gear peg vision targets right now :(");
		}
		System.out.println(centerX.length);
		
		
		if(IO.driveStick.getTrigger()){
			UsbCamera camera1 = camServer.startAutomaticCapture();
		}
		*/
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}

