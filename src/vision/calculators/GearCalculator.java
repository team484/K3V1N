package vision.calculators;

import java.util.ArrayList;

import vision.CameraSettings;
import vision.Contour;
import vision.VisionResults;

public class GearCalculator {
	private static final double TAPE_SPACING = 8.25; //Distance (inches) between center of each tape
	private static final double TAPE_HEIGHT = 5; //Height of the tape (inches)
	private static final double TAPE_WIDTH = 2; //Width of the tape (inches)
	public static VisionResults run(ArrayList<Contour> contours, CameraSettings camSettings) {
		ArrayList<Double> distances = new ArrayList<Double>();
		distances.add(distanceFromHeight(contours, camSettings));
		distances.add(distanceFromWidth(contours, camSettings));
		distances.add(distanceFromSpacing(contours, camSettings));
		double avgDistance = (distances.get(0) + distances.get(1) + distances.get(2)) / 3.0;
		double angleX = offsetXAngleFromCenters(contours, camSettings);
		double offsetX = Math.tan(angleX) * avgDistance;
		//TODO: Properly look at different distance values to error correct
		//TODO: Look at camera position and orientation when performing calculations
		
		return new VisionResults(angleX, 0, offsetX, 0, avgDistance);
	}
	
	//Calculates distance to target based on height of the tape
	private static double distanceFromHeight(ArrayList<Contour> contours, CameraSettings camSettings) {
		double[] results = new double[2];
		
		for (int i = 0; i < 2; i++) {
			double height = contours.get(i).height;
			double angle = camSettings.radPerPixel * height;
			results[i] = TAPE_HEIGHT / (2.0 * Math.tan(angle/2.0));
		}
		return (results[0] + results[1]) / 2.0;
	}
	
	//Calculates distance to target based on the width of the tape
	private static double distanceFromWidth(ArrayList<Contour> contours, CameraSettings camSettings) {
		double[] results = new double[2];
		
		for (int i = 0; i < 2; i++) {
			double width = contours.get(i).width;
			double angle = camSettings.radPerPixel * width;
			results[i] = TAPE_WIDTH / (2.0 * Math.tan(angle/2.0));
		}
		return (results[0] + results[1]) / 2.0;
	}
	
	//Calculates distance to target based on the distance between the tape
	private static double distanceFromSpacing(ArrayList<Contour> contours, CameraSettings camSettings) {
		
			double width = Math.abs(contours.get(0).centerX - contours.get(1).centerX);
			double angle = camSettings.radPerPixel * width;
			return TAPE_SPACING / (2.0 * Math.tan(angle/2.0));

	}
	
	//Calculates the angle in the x-y plane (horizontal) to target based on the centerX of the 2 tapes
	private static double offsetXAngleFromCenters(ArrayList<Contour> contours, CameraSettings camSettings) {
		double center = (contours.get(0).centerX + contours.get(1).centerX) / 2.0;
		double angle = camSettings.radPerPixel * (center - camSettings.width / 2.0);
		return angle;
	}
}
