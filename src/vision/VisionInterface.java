package vision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

import org.usfirst.frc.team484.robot.RobotSettings;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionInterface {
    
    // TODO: table name
    private static NetworkTable table = NetworkTable.getTable("GRIP/vision");
    
    public enum LookDirection {
        Left, Right, Center
    }
    
    /**
     * Returns which direction the robot needs to turn in order to get 
     * the gear peg in the bot's center
     * @param centerpoint The center point of the camera's view
     * @return Which direction to turn the robot
     */
    public static double getDriveDirection(double centerpoint) {
        // Get all the data we need from the network table
    	Optional<HookPair> pair = HookPair.fromValues(Contour.getContoursFromTable(table));

        return centerpoint - pair.map((hp) -> hp.getCenterX()).orElse(Double.NaN);
        
    }
  
    
    //Returns the distance from the center
    public static double getLookDirection(double centerpoint) {
        Optional<HookPair> pair = HookPair.fromValues(Contour.getContoursFromTable(table));
        
        return pair.map((p) -> p.left.area - p.right.area).orElse(Double.NaN);
    
    }
    
    public static double calculateDistance(){
    	Optional<HookPair> pair = HookPair.fromValues(Contour.getContoursFromTable(table));
    	//Assuming camera is level with the bottom of the tape vertically to calculate!
    	
    	double distance = pair.map((HookPair p) -> Math.tan(90 - (p.right.centerY + (p.right.height / 2)) * RobotSettings.degPerPix)).orElse(Double.NaN);
    	
//    	 = Math.tan(90 - ((pair.get().right.centerY + (pair.get().right.height / 2) ) * RobotSettings.degPerPix));
    	
    	return distance * Math.tan(pair.get().right.centerX * RobotSettings.degPerPix);
  
    }

}
