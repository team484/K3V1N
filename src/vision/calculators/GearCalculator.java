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
		double[] results = distanceFromHeight(contours, camSettings);
		distances.add(results[0]);
		distances.add(distanceFromWidth(contours, camSettings));
		distances.add(distanceFromSpacing(contours, camSettings));
		distances.sort((c1, c2) -> (int)(c2 * 100000.0 - c1 * 100000.0));
		
		double medianDistance = distances.get(1);
		double angleX = offsetXAngleFromCenters(contours, camSettings);
		double offsetX = Math.tan(angleX) * medianDistance;
		//TODO: Properly look at different distance values to error correct
		
		return new VisionResults(angleX + camSettings.angleX, 0, offsetX + camSettings.posX, 0, medianDistance + camSettings.posY, results[1]);
	}
	
	//Calculates distance to target based on height of the tape
	private static double[] distanceFromHeight(ArrayList<Contour> contours, CameraSettings camSettings) {
		double[] results = new double[2];
		
		Contour leftCntr = contours.get(0).centerX < contours.get(1).centerX ? contours.get(0) : contours.get(1);
		Contour rightCntr = contours.get(0).centerX > contours.get(1).centerX ? contours.get(0) : contours.get(1);

		double height = leftCntr.height;
		double angle = camSettings.radPerPixel * height;
		results[0] = TAPE_HEIGHT / (2.0 * Math.tan(angle/2.0));
		
		height = rightCntr.height;
		angle = camSettings.radPerPixel * height;
		results[1] = TAPE_HEIGHT / (2.0 * Math.tan(angle/2.0));
		
		double width = Math.abs(contours.get(0).centerX - contours.get(1).centerX);
		angle = camSettings.radPerPixel * width;
		
		
		//4 ways to get relative rotation of target and robot using difference in heights of tapes
		double targetAngleLoS1 = Math.asin(Math.sin(angle) * results[0] / TAPE_SPACING) + angle / 2.0 - Math.PI / 2.0;
		double targetAngleLoS2 = Math.PI / 2.0 - angle / 2.0 - Math.asin(Math.sin(angle) * results[1] / TAPE_SPACING);
		double targetAngleLOC1 = Math.acos((Math.pow(TAPE_SPACING, 2)+Math.pow(results[1], 2)-Math.pow(results[0], 2))/(2.0 * TAPE_SPACING * results[1])) + angle / 2.0 - Math.PI / 2.0;
		double targetAngleLOC2 = Math.PI / 2.0 - angle / 2.0 - Math.acos((Math.pow(TAPE_SPACING, 2)+Math.pow(results[0], 2)-Math.pow(results[1], 2))/(2.0 * TAPE_SPACING * results[0]));
		ArrayList<Double> angles = new ArrayList<Double>();
		angles.add(targetAngleLoS1);
		angles.add(targetAngleLoS2);
		angles.add(targetAngleLOC1);
		angles.add(targetAngleLOC2);
		angles.sort((c1, c2) -> (int)(c2 * 100000.0 - c1 * 100000.0));
		double medianAngle = (angles.get(1) + angles.get(2))/2.0; //Might be better to get the 3rd smallest angle instead of the median
		return new double[] {(results[0] + results[1]) / 2.0, medianAngle};
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
