package util;


import org.jetbrains.annotations.Contract;

public class MU {

    //rotation and scale////////////////////////////////////////////////////////////////////////////
    @Contract("_, _, _ -> !null")
    public static Vector2D rotate(int a, int pnts, double rotate) {
        return new Vector2D(cos(360 / pnts * (a + 1) + rotate), sin(360 / pnts * (a + 1) + rotate));
    }

    @Contract("_ -> !null")
    public static Vector2D rotate(double rotate) {
        return new Vector2D(cos(rotate), sin(rotate));
    }

    @Contract("_ -> !null")
    public static Vector2D zoom(double zoom) {
        return new Vector2D(sin(zoom), sin(zoom));
    }

    @Contract("_ -> !null")
    public static Vector2D rotatex(double rotate) {
        return new Vector2D(sin(rotate), 1);
    }

    @Contract("_ -> !null")
    public static Vector2D rotatey(double rotate) {
        return new Vector2D(1, sin(rotate));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //trig functions////////////////////////////////////////////////////////////////////////////////
    public static double cos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    public static double sin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double tan(double degree) {
        return Math.tan(Math.toRadians(degree));
    }

    public static double arctan(double theta) {
        return Math.atan(theta);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //squares///////////////////////////////////////////////////////////////////////////////////////
    @Contract(pure = true)
    public static int makeSquareI(boolean shift, int i, int size) {
        if (i < 0) {
            return 0;
        }
        double quad = size / 4.0;
        if (!shift) {
            if (i > 0 && i <= quad) {
                return 0;
            }
            if (i > quad && i <= 2 * quad) {
                return 1;
            }
            if (i > 2 * quad && i <= 3 * quad) {
                return 1;
            }
            if ((i > 3 * quad && i <= 4 * quad) || i == 0) {
                return 0;
            }
        } else {
            if (i > 0 && i <= quad) {
                return 0;
            }
            if (i > quad && i <= 2 * quad) {
                return 0;
            }
            if (i > 2 * quad && i <= 3 * quad) {
                return 1;
            }
            if ((i > 3 * quad && i <= 4 * quad) || i == 0) {
                return 1;
            }
        }
        return 0;
    }

    @Contract(pure = true)
    public static double square(double val) {
        return val * val;
    }

    @Contract(pure = true)
    public static boolean makeSquareB(boolean shift, int i, int size) {
        if (i < 0) {
            return false;
        }
        double quad = size / 4.0;
        if (!shift) {
            if (i > 0 && i <= quad) {
                return false;
            }
            if (i > quad && i <= 2 * quad) {
                return true;
            }
            if (i > 2 * quad && i <= 3 * quad) {
                return true;
            }
            if ((i > 3 * quad && i <= 4 * quad) || i == 0) {
                return false;
            }
        } else {
            if (i > 0 && i <= quad) {
                return false;
            }
            if (i > quad && i <= 2 * quad) {
                return false;
            }
            if (i > 2 * quad && i <= 3 * quad) {
                return true;
            }
            if ((i > 3 * quad && i <= 4 * quad) || i == 0) {
                return true;
            }
        }


        return false;
    }

    @Contract(pure = true)
    public static boolean makeHalvesB(boolean inverse, int i, int size) {
        double half = size / 8.0;
        if ((i > 0 && i <= 3 * half) || (i > 7 * half && i <= size)) {
            return !inverse;
        }
        return i > 3 * half && i <= 7 * half && inverse;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //circles///////////////////////////////////////////////////////////////////////////////////////
    @Contract(pure = true)
    public static double getRadiusCircum(double circumfrence) {
        return circumfrence / (2.0 * Math.PI);
    }

    public static double getRadiusArea(double area) {
        return Math.sqrt(area / Math.PI);
    }

    @Contract(pure = true)
    public static double getAreaRadius(double radius) {
        return Math.PI * square(radius);
    }

    @Contract(pure = true)
    public static double getAreaCircum(double circumference) {
        return square(circumference) / (4.0 * Math.PI);
    }

    public static double getCircumArea(double area) {
        return 2.0 * Math.PI * Math.sqrt(area / Math.PI);
    }

    @Contract(pure = true)
    public static double getCircumRadius(double radius) {
        return 2.0 * Math.PI * radius;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    //other//////////////////////////////////////////////////////////////////////////////////////////
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(square(x1 - x2) + square(y1 - y2));
    }

    @Contract(pure = true)
    public static double getPercent(double val1, double val2){
     return val1/val2;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

}
