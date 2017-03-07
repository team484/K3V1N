package org.usfirst.frc.team484.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

import org.opencv.core.Mat;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSource;

public class CameraSwitch extends Thread{
	Thread visionThread;
	Joystick joystick;
	UsbCamera gearCam = null;
	UsbCamera climberCam = null;
	//UsbCamera camera = null;
	//CvSink cvSink;
	//CvSource outputStream;
	CameraServer server;


	public void run() {

		Mat mat = new Mat();

		while (!Thread.interrupted()) {

			if(joystick.getRawButton(10))
				try {
					try {
						//camera.free();
						//camera = new UsbCamera("cam3", 0);
						//camera.setResolution(320, 240);
					} catch (Exception e) {
						System.err.println("Could not find gear camera!");
					}
					//cvSink = CameraServer.getInstance().getVideo(camera);
					server.removeCamera("cam0");
					server.startAutomaticCapture("cam3", 0);
				} catch (Exception e) {
					System.err.println("Could not find gear camera!");
				}

			if(joystick.getRawButton(11))
				try {
					try {
						//camera.free();
						//camera = new UsbCamera("cam0", 0);
						//camera.setResolution(320, 240);
					} catch (Exception e) {
						System.err.println("Could not find gear camera!");
					}
					server.startAutomaticCapture("cam0", 0);

					//cvSink = CameraServer.getInstance().getVideo(camera);
				} catch (Exception e) {
					System.err.println("Could not find climber camera!");
				}

		}
	}
	public CameraSwitch(Joystick joystick) {
		/*
		 * makes the field joystick the passed in joystick
		 */
		this.joystick = joystick;

		/*
		 * this creates a new thread for the robot to run
		 * this thread will just put camera output to the dash
		 * and switch cameras at the press of a button on the joystick
		 */
		/*
		 * create the cameras here
		 */
		try {
			//camera = new UsbCamera("cam3", 0);
			//camera.setResolution(320, 240);
		} catch (Exception e) {
			System.err.println("Could not find gear camera!");
		}
		//gearCam = new UsbCamera("cam0", 0);
		//climberCam = new UsbCamera("cam3", 0);
		
		server = CameraServer.getInstance();
		UsbCamera test = new UsbCamera("cam0", 0);
		//UsbCamera test2 = new UsbCamera("cam3",3);
		server.addCamera(test);
		server.startAutomaticCapture();
		//server.addCamera(gearCam);
		//server.addCamera(climberCam);
		//cvSink = CameraServer.getInstance().getVideo(camera);
		//outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

	}
}
