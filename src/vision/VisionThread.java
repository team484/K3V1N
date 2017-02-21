package vision;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import vision.calculators.GearCalculator;
import vision.calculators.ShooterCalculator;

public class VisionThread extends Thread {
	private static final int UPDATE_FREQUENCY = 67; //Update rate in ms
	
	private String networkTableName; //Name of table with vision data
	private CameraSettings camSettings; //Camera Settings
	private Camera cam; //Which camera this thread is responsible for
	private enum Camera {
		SHOOTER,
		GEAR
	}
	
	private boolean shouldUpdate = true; //Set to false when the thread should terminate
	private ArrayList<Contour> contours = new ArrayList<Contour>(); //List of contours found
	NetworkTable visionTable; //Network table with contours report
	private VisionResults visionResults; //The results of the vision calculation
	
	public VisionThread(Camera cam, CameraSettings camSettings, String tableName) {
		this.cam = cam;
		this.camSettings = camSettings;
		this.networkTableName = "GRIP/" + tableName;
		visionResults = new VisionResults();
	}
	
	//Gets called in new thread when this class' .start() method is called
	@Override
	public void run() { 
		while (shouldUpdate) {
			long startTime = System.currentTimeMillis();
			updateCalculations();
			long delta = System.currentTimeMillis() - startTime;
			if (delta < UPDATE_FREQUENCY) {
				pause(UPDATE_FREQUENCY - delta);
			}
		}
	}
	
	//Used to run the vision calculations once (not threaded) instead of repeatedly
	public void runOnce() {
		updateCalculations();
	}
	
	//Called to kill vision thread
	public void kill() {
		shouldUpdate = false;
	}
	
	//Used to change the camera settings after initialization
	public void updateCameraSettings(CameraSettings camSettings) {
		this.camSettings = camSettings;
	}
	
	//Returns the most recent vision calculation results
	public VisionResults getResults() {
		return visionResults;
	}
	
	//Runs all vision calculations from network table data
	private void updateCalculations() {
		visionTable = NetworkTable.getTable(networkTableName);
		contours = Contour.getContoursFromTable(visionTable);
		
		if (contours.size() >= 2) {
			sortContours(contours);
			getLargest(contours);
			switch (cam) {
			case SHOOTER:
				visionResults = ShooterCalculator.run(contours, camSettings);
				break;
			case GEAR:
				visionResults = GearCalculator.run(contours, camSettings);
				break;
			default:
				visionResults = new VisionResults();
				break;
			}
		} else {
			visionResults = new VisionResults();
		}
	}
	
	//Sorts contours from largest to smallest
	private static void sortContours(ArrayList<Contour> contours) {
		contours.sort((c1,c2) -> (int)(c2.area - c1.area));
	}
	
	//Removes all but the largest 2 contours
	private static void getLargest(ArrayList<Contour> contours) {
		for (int i = 2; i < contours.size();) {
			contours.remove(i);
		}
	}
	
	//Used to pause the vision thread between loops in the run method
	private static void pause(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
