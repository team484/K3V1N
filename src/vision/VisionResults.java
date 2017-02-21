package vision;

//Object that stores results of the vision calculations
public class VisionResults {
	public double relativeAngleX, relativeAngleY, inchesX, inchesY, inchesZ;
	public boolean targetFound = false;
	
	//Initialized this way when target was found
	public VisionResults(double relativeAngleX, double relativeAngleY, double inchesX, double inchesY, double inchesZ) {
		this.relativeAngleX = relativeAngleX; //The angle of target along the x-y plane relative to robot orientation
		this.relativeAngleY = relativeAngleY; //The angle of the target along the y-z plane relative to robot orientation
		this.inchesX = inchesX; //The offset in the x axis of the target relative to the robot
		this.inchesY = inchesY; //The offset in the y axis (height) of the target relative to the robot
		this.inchesZ = inchesZ; //The distance from the robot to the target
		targetFound = true; //True if vision calculations successfully found the target
	}
	
	//Initialized this way when target wasn't found
	public VisionResults() {
		
	}
}
