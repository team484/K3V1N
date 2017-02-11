package vision;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleConsumer;

public class HookPair {
    public Contour left;
    public Contour right;
    
    // TODO: maybe figure out which is left and which is right here?
    public HookPair(Contour left, Contour right) {
        this.left = left;
        this.right = right;
    }
    
    //public static HookPair nanPair() {
    	//return new 
    //}
    
    public static Optional<HookPair> fromValues(List<Contour> contours) {
    	
    	if(contours.size() < 2)
    		return Optional.empty();
    	
    	contours.sort((a, b) -> a.area > b.area ? -1 : 1);
    	
    	Contour a = contours.get(0);
    	Contour b = contours.get(1);
    	
        return Optional.of(a.centerX >= b.centerX ? new HookPair(b, a) : new HookPair(a, b));
    }
    
    public double getCenterX() {
        return (this.right.centerX + this.left.centerX) / 2.0;
    }
}
