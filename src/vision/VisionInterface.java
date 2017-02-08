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
        List<Contour> contours = Contour.getContoursFromTable(table);
        
        // Average the contours
        return contours
                .stream()
                .mapToDouble((c) -> c.centerX)
                .average()
                .orElse(Double.NaN);
    }
    
    public static double getLookDirection(double centerpoint) {
        Iterator<Contour> contours = Contour.getContoursFromTable(table).iterator();
        HookPair pair = HookPair.fromValues(contours.next(), contours.next());
        
        if(pair.left.area > pair.right.area)
//            return LookDirection.Right;
            return centerpoint - pair.getCenterX();
        else if(pair.left.area < pair.right.area)
            return centerpoint - pair.getCenterX();
        else
            return LookDirection.Center;
    }

}
