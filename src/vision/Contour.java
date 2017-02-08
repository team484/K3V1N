package vision;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Contour {
    
    double centerX;
    double centerY;
    double area;
    double width;
    double height;
    double solidity;
    
    public Contour(double centerX, double centerY, double area, double width, double height, double solidity) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.area = area;
        this.width = width;
        this.height = height;
        this.solidity = solidity;
    }
    
    public static List<Contour> getContoursFromTable(ITable table) {
        double[] centerX = table.getNumberArray("centerX", new double[0]);
        double[] centerY = table.getNumberArray("centerY", new double[0]);
        double[] area = table.getNumberArray("area", new double[0]);
        double[] width = table.getNumberArray("width", new double[0]);
        double[] height = table.getNumberArray("height", new double[0]);
        double[] solidity = table.getNumberArray("solidity", new double[0]);
        
        List<Contour> contours = new ArrayList<>();
        
        for(int i = 0; i < centerX.length; i++)
            contours.add(new Contour(centerX[i], centerY[i], area[i], width[i], height[i], solidity[i]));
        
        return contours;
    }
    
}
