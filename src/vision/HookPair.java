package vision;

public class HookPair {
    public Contour left;
    public Contour right;
    
    // TODO: maybe figure out which is left and which is right here?
    public HookPair(Contour left, Contour right) {
        this.left = left;
        this.right = right;
    }
    
    public static HookPair fromValues(Contour a, Contour b) {
        return a.centerX >= b.centerX ? new HookPair(b, a) : new HookPair(a, b);
    }
    
    public double getCenterX() {
        return (this.right.centerX + this.left.centerX) / 2.0;
    }
}
