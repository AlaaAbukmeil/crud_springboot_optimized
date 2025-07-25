package com.energybox.backendcodingchallenge.util;

import java.math.BigDecimal;

public class DistanceUtil {


    public static Double calculateEuclideanDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        if (x1 == null || y1 == null || x2 == null || y2 == null) {
            return Double.MAX_VALUE; // Return maximum distance if any coordinate is null
        }

        double dx = x2.subtract(x1).doubleValue();
        double dy = y2.subtract(y1).doubleValue();

        return Math.sqrt(dx * dx + dy * dy);
    }


    public static Double calculateManhattanDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        if (x1 == null || y1 == null || x2 == null || y2 == null) {
            return Double.MAX_VALUE; // Return maximum distance if any coordinate is null
        }

        double dx = Math.abs(x2.subtract(x1).doubleValue());
        double dy = Math.abs(y2.subtract(y1).doubleValue());

        return dx + dy;
    }
}