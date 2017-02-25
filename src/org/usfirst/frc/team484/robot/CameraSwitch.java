package org.usfirst.frc.team484.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

import org.opencv.core.Mat;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSource;

public class CameraSwitch {
	Thread visionThread;
	Joystick joystick;

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
		visionThread = new Thread(() -> {
			/*
			 * create the cameras here
			 */
			UsbCamera gearCam = null;
			UsbCamera climberCam = null;
			try {
				gearCam = new UsbCamera("cam0", 0);
				gearCam.setResolution(640, 480);
			} catch (Exception e) {
				System.err.println("Could not find gear camera!");
			}
			
			try {
				climberCam = new UsbCamera("cam1",0);
				climberCam.setResolution(640, 480);
			} catch (Exception e) {
				System.err.println("Could not find climber camera!");
			}
			
			CvSink cvSink = CameraServer.getInstance().getVideo(gearCam);
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			Mat mat = new Mat();

			while (!Thread.interrupted()) {

				if(joystick.getRawButton(10))
					try {
						cvSink = CameraServer.getInstance().getVideo(gearCam);
					} catch (Exception e) {
						try {
							gearCam = new UsbCamera("cam0", 0);
							gearCam.setResolution(640, 480);
						} catch (Exception ex) {
							System.err.println("Could not find gear camera!");
						}
					}
				
				if(joystick.getRawButton(11))
					try {
					cvSink = CameraServer.getInstance().getVideo(climberCam);
					} catch (Exception e) {
						try {
							climberCam = new UsbCamera("cam1",0);
							climberCam.setResolution(640, 480);
						} catch (Exception ex) {
							System.err.println("Could not find climber camera!");
						}
					}
				
				
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
	}
}