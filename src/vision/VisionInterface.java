package vision;

import java.util.Iterator;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionInterface {
    
    // TODO: table name
    private static NetworkTable table = NetworkTable.getTable("vision");
    
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
        Iterator<Contour> contours = Contour.getContoursFromTable(table).iterator();
        HookPair pair = HookPair.fromValues(contours.next(), contours.next());

        return centerpoint - pair.getCenterX();
    }
    
    //Returns the distance from the center
    public static double getLookDirection(double centerpoint) {
        Iterator<Contour> contours = Contour.getContoursFromTable(table).iterator();
        HookPair pair = HookPair.fromValues(contours.next(), contours.next());
        
        return pair.left.area - pair.right.area;
    
    }

}
