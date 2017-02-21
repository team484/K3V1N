package vision;

//Object that stores info on camera location, orientation, and resolution
public class CameraSettings {
	public int width, height;
	public double posX,posY,posZ,angleX,angleY,radPerPixel;
	public CameraSettings(int width, int height, double posX, double posY, double posZ, double angleX, double angleY, double radPerPixel) {
		this.width = width; //Width of camera image
		this.height = height; //Height of camera image
		this.posX = posX; //Offset of camera from the center of the x-axis of the robot
		this.posY = posY; //Offset of camera from the front of the robot along y-axis
		this.posZ = posZ; //Height of camera from the ground
		this.angleX = angleX; //Direction camera points in x-y (horizontal) plane
		this.angleY = angleY; //Direction camera points in y-z (vertical) plane
		this.radPerPixel = radPerPixel; //The radians (angle) of each pixel in camera image
	}
}
