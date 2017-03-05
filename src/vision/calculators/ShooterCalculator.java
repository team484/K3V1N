package vision.calculators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import vision.CameraSettings;
import vision.Contour;
import vision.VisionResults;

@SuppressWarnings("unused")
public class ShooterCalculator {
	
	private static final double TAPE_WIDTH = 15;
	private static final double TOP_TAPE_THICKNESS = 4;
	private static final double BOTTOM_TAPE_THICKNESS = 2;
	private static final double TOP_TAPE_ELEVATION = 86;
	private static final double BOTTOM_TAPE_ELEVATION = 79;
	public static VisionResults run(ArrayList<Contour> contours, CameraSettings camSettings) {
		
		double distanceTop = distanceOfTopTape(contours, camSettings) + camSettings.posZ;
		double distanceBottom = distanceOfBottomTape(contours, camSettings) + camSettings.posZ;
		
		double offsetXTop = offsetXTopTape(contours, camSettings, distanceTop);
		double offsetXBottom = offsetXBottomTape(contours, camSettings, distanceBottom);
		
		double offsetAngleTop = offsetAngleTape(distanceTop, offsetXTop);
		double offsetAngleBottom = offsetAngleTape(distanceBottom, offsetXBottom);
		
		double avgDistance = (distanceTop + distanceBottom) / 2.0;
		double avgOffsetX = (offsetXTop + offsetXBottom) / 2.0;
		double avgOffsetAngle = (offsetAngleTop + offsetAngleBottom) / 2.0;

		
		return new VisionResults(avgOffsetAngle, 0, avgOffsetX, 0, avgDistance, 0);
	}
	
	//Calculates distance to target based on height of the top tape
	private static double distanceOfTopTape(ArrayList<Contour> contours, CameraSettings camSettings) {
		Contour topTape = (contours.get(0).centerY < contours.get(1).centerY) ? contours.get(0) : contours.get(1);
		double pixelDelta = camSettings.height / 2.0 - topTape.centerY;
		double angleFromCamCenter = pixelDelta * camSettings.radPerPixel;
		double angleFromHorizontal = angleFromCamCenter + camSettings.angleY;
		double goalHighDelta = TOP_TAPE_ELEVATION - camSettings.posY;
		double distance = goalHighDelta / Math.tan(angleFromHorizontal);
		return distance;
	}
	
	//Calculates distance to target based on height of the bottom tape
	private static double distanceOfBottomTape(ArrayList<Contour> contours, CameraSettings camSettings) {
		Contour bottomTape = (contours.get(0).centerY > contours.get(1).centerY) ? contours.get(0) : contours.get(1);
		double pixelDelta = camSettings.height / 2.0 - bottomTape.centerY;
		double angleFromCamCenter = pixelDelta * camSettings.radPerPixel;
		double angleFromHorizontal = angleFromCamCenter + camSettings.angleY;
		double goalHighDelta = BOTTOM_TAPE_ELEVATION - camSettings.posY;
		double distance = goalHighDelta / Math.tan(angleFromHorizontal);
		return distance;
	}
	
	//Calculates x offset of target based on distance from the top tape
	private static double offsetXTopTape(ArrayList<Contour> contours, CameraSettings camSettings, double distance) {
		Contour topTape = (contours.get(0).centerY < contours.get(1).centerY) ? contours.get(0) : contours.get(1);
		double pixelDelta = topTape.centerX - camSettings.width/2.0;
		double angleFromCamCenter = pixelDelta * camSettings.radPerPixel;
		double triangleAngle = Math.PI / 2.0 - angleFromCamCenter;
		double relativeOffset = distance / Math.tan(triangleAngle);
		double offsetX = relativeOffset + camSettings.posX;
		return offsetX;
	}
	
	//Calculates x offset of target based on distance from the bottom tape
	private static double offsetXBottomTape(ArrayList<Contour> contours, CameraSettings camSettings, double distance) {
		Contour bottomTape = (contours.get(0).centerY > contours.get(1).centerY) ? contours.get(0) : contours.get(1);
		double pixelDelta = bottomTape.centerX - camSettings.width/2.0;
		double angleFromCamCenter = pixelDelta * camSettings.radPerPixel;
		double triangleAngle = Math.PI / 2.0 - angleFromCamCenter;
		double relativeOffset = distance / Math.tan(triangleAngle);
		double offsetX = relativeOffset + camSettings.posX;
		return offsetX;
	}
	
	//Calculates angle offset of target based on distance from a tape and inches offset
	private static double offsetAngleTape(double distance, double offsetX) {
		double angle = Math.atan(distance / offsetX);
		return angle;
	}
	
}
