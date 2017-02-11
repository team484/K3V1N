package vision;



import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionCalculations {
	private double radiansPerPixel = RobotSettings.degPerPix * (180/ Math.PI);
	private double radiansPerPixelHorizontal = radiansPerPixel;  //0.0012544553999;
	private double cameraHeight = 6;  //About? Maybe?
	private double cameraHorizontalOffset = 11.59375;
	private double cameraImageWidth = 1920;
	private double cameraImageHeight = 1080;
	private double cameraAngleUp = 0;
	private double heightOfTape = RobotSettings.tapeHeight;
	private double goalWidth = 2;

	public double lastHorizontal = Double.NaN;
	public double lastDistance = Double.NaN;
	public double lastAngle = Double.NaN;
	public double lastHorizontalAngle = Double.NaN;
	public static void main(String[] args) {
		new VisionCalculations().run();
	}
	public void run() {
		NetworkTable visionTable = NetworkTable.getTable("GRIP/vision");
		try {
			double[] centerX = visionTable.getNumberArray("centerX", new double[0]);
			double[] centerY = visionTable.getNumberArray("centerY", new double[0]);
			double[] area = visionTable.getNumberArray("area", new double[0]);
			double[] width = visionTable.getNumberArray("width", new double[0]);
			double[] height = visionTable.getNumberArray("height", new double[0]);
			int i = 0;
			int ii = 0;
			double maxArea = 0.0;
			int maxAreaSpot = -1;
			double otherArea = 0.0;
			int otherAreaSpot = -1;
			
			while (i < centerX.length) {
				if (area[i] > maxArea) {
					maxArea = area[i];
					maxAreaSpot = i;
				}
				i++;
			}
			while (ii < centerX.length) {
				if (area[ii] > maxArea) {
					if(ii == i){ii++;}
					otherArea = area[ii];
					otherAreaSpot = ii;
				}
				ii++;
			}
			if (maxAreaSpot > -1) {
				double horizontalAngleCenter = 1.5707963268 - (cameraImageWidth / 2.0 - centerX[maxAreaSpot]) * radiansPerPixelHorizontal;
				double horizontalAngleRight = 1.5707963268 - (cameraImageWidth / 2.0 - (centerX[maxAreaSpot] + width[maxAreaSpot])) * radiansPerPixelHorizontal;
				double verticalAngleCenter =  cameraAngleUp + (cameraImageHeight - centerY[maxAreaSpot] - 1) * radiansPerPixel;
				double distance = (heightOfTape - cameraHeight)/Math.tan(verticalAngleCenter);
				double angularDistance = (heightOfTape - cameraHeight)/Math.sin(verticalAngleCenter);
				double horizontalOffset1 = angularDistance / Math.tan(horizontalAngleCenter) - cameraHorizontalOffset;
				double horizontalOffset2 = angularDistance / Math.tan(horizontalAngleRight) - (cameraHorizontalOffset - goalWidth/2.0) + cameraHorizontalOffset;				
				if (centerY[maxAreaSpot] + height[maxAreaSpot]/2.0 >= 479 || centerY[maxAreaSpot] - height[maxAreaSpot]/2.0 <= 1) {
					noTarget();
				} else {
					//System.out.println("D: " + distance + "  h1: " + horizontalOffset1 + " h2: " + horizontalOffset2);
					//System.out.println("Too low by: " + (shootAngle - (1.5707963268 + Robot.robotIO.shooterArmEncoder.getDistance())));
					lastAngle = -Math.atan(Math.log(-373*(distance * distance - 938.338 * distance - 33746.6))-17.0344);
					lastHorizontal = horizontalOffset1;
					lastDistance = distance;
					lastHorizontalAngle = Math.atan((horizontalOffset1 - 3)/lastDistance) * 57.2958;
					SmartDashboard.putNumber("Distance", distance);
					SmartDashboard.putNumber("H1", horizontalOffset1);
					SmartDashboard.putNumber("H2", horizontalOffset2);
					SmartDashboard.putNumber("Proper Angle", lastAngle);
					SmartDashboard.putNumber("HAngCenter", lastHorizontalAngle);
				}
			} else {
				noTarget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void noTarget() {
		lastHorizontal = Double.NaN;
		lastDistance = Double.NaN;
		lastAngle = Double.NaN;
		lastHorizontal = Double.NaN;
		SmartDashboard.putNumber("Distance", Double.NaN);
		SmartDashboard.putNumber("H1", Double.NaN);
		SmartDashboard.putNumber("H2", Double.NaN);
		SmartDashboard.putNumber("Proper Angle", Double.NaN);
		SmartDashboard.putNumber("HAngCenter", Double.NaN);
	}

}

 