package killbots;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
import java.util.*;
import robocode.util.Utils;

/**
 * Marko - a robot by Vinícius Garcia
 */
public class Marko extends Robot {

    private int WALL_DISTANCE = 40;

    private double enemyBearing = 180;
    private boolean firing = false;

    /**
     * run: Marko's default behavior
     */
    public void run() {
        // setColors(Color.red,Color.blue,Color.green); // body,gun,radar

        // Robot main loop
        while(true) {
            moveTank();
            moveCannon();
        }
    }

    private boolean turnToHeading(double heading) {
        double tankHeading = getHeading();

        // If the angle is close enough already,
        // we are done:
        double a1 = Utils.normalAbsoluteAngleDegrees(heading), a2 = Utils.normalAbsoluteAngleDegrees(tankHeading);
        boolean done = Utils.isNear(
            Utils.normalAbsoluteAngleDegrees(heading),
            Utils.normalAbsoluteAngleDegrees(tankHeading)
        );

        if (state == "aligning") {
            // out.println("aligning");
            // out.println("A1: " + a1 + " A2: " + a2 + " done: " + done);
            // out.println("heading: " + heading + " tankHeading: " + tankHeading + " done: " + done);
        }

        if (done) {
            return true;
        } else {
            turnRight(
                turnAngle(tankHeading, heading)
            );
            return false;
        }
    }

    private double turnAngle(double from, double to) {
        double diff = (to % 360) - (from % 360);

        // Make sure to turn the shorter angle between both:
        if (Math.abs(diff) > 180) {
            return diff < 0 ? diff + 360 : diff - 360;
        } else {
            return diff;
        }
    }

    private void turnTo(double x, double y) {
    }

    private void goTo(double x, double y) {
    }

    private double xDist() {
        double x = getX();
        double width = getBattleFieldWidth();
        double relX = x/width;

        return relX > 0.5 ? width-x : x;
    }

    private double yDist() {
        double y = getY();
        double height = getBattleFieldHeight();
        double relY = y/height;

        return relY > 0.5 ? height-y : y;
    }

    String wall = "";
    private boolean moveToWall() {
        out.println("moving to wall");
        double x = getX(), y = getY();
        double height = getBattleFieldHeight();
        double width = getBattleFieldWidth();
        double relX = x/width, relY = y/height;

        // Movement vector:
        double xi, yi;

        // Calculate xi and yi
        if (relX > 0.5) xi = 1; else xi = -1;
        if (relY > 0.5) yi = 1; else yi = -1;

        double xDist = relX > 0.5 ? width-x : x;
        double yDist = relY > 0.5 ? height-y : y;

        // out.println("x, y: " + x + ", " + y);
        if (xDist < WALL_DISTANCE) {
            // out.println("xi: " + xi + " relX " + relX);
            // out.println("yi: " + yi + " relY " + relY);
            wall = yi == 1 ? "right" : "left";
            // out.println("Wall is: " + wall);
            return true;
        }

        if (yDist < WALL_DISTANCE) {
            // out.println("xi: " + xi + " relX " + relX);
            // out.println("yi: " + yi + " relY " + relY);
            wall = xi == 1 ? "top" : "bottom";
            // out.println("Wall is: " + wall);
            return true;
        }

        if (xDist < yDist) {
            turnToHeading(xi * 90);
            ahead(xDist - WALL_DISTANCE + 10);
        } else {
            turnToHeading(90 - xi * 90);
            ahead(yDist - WALL_DISTANCE + 10);
        }

        return false;
    }

    private boolean circulate() {
        double x = getX(), y = getY();
        double height = getBattleFieldHeight();
        double width = getBattleFieldWidth();

        switch (wall) {
            case "top":
                if (x < WALL_DISTANCE) {
                    wall = "left";
                    return true;
                } else {
                    ahead(200 - WALL_DISTANCE + 10);
                    return false;
                }
            case "bottom":
                if (width-x < WALL_DISTANCE) {
                    wall = "right";
                    return true;
                } else {
                    ahead(200 - WALL_DISTANCE + 10);
                    return false;
                }
            case "left":
                if (y < WALL_DISTANCE) {
                    wall = "bottom";
                    return true;
                } else {
                    ahead(200 - WALL_DISTANCE + 10);
                    return false;
                }
            case "right":
                if (height-y < WALL_DISTANCE) {
                    wall = "top";
                    return true;
                } else {
                    ahead(200 - WALL_DISTANCE + 10);
                    return false;
                }
            default:
                return true;
        }
    }

    private double desiredAngle;
    private String state = "start";
    private void moveTank() {

        if (state == "start") {
            out.println("Starting...");
            state = "toWall";
        }

        if (state == "toWall" && moveToWall()) {
            out.println("Got on wall");

            desiredAngle = getHeading() - 90;
            state = "aligning";
        }


        if (state == "aligning" && turnToHeading(desiredAngle)) {
            out.println("Aligned to " + wall + " wall");

            state = "circulate";
        }

        if (state == "circulate" && circulate()) {
            out.println("On edge");

            desiredAngle = getHeading() - 90;
            // out.println("desiredAngle: " + desiredAngle + " wall: " + wall);
            // out.println("xDist(): " + xDist() + " yDist(): " + yDist());
            state = "aligning";
        }
    }

    private void moveCannon() {
        if (firing) {
            turnGunRight(enemyBearing);
            firing = false;
        } else {
            turnGunRight(-90);
        }
        // turnGunRight(360);
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        enemyBearing = e.getBearing();
        firing = true;
        fire(1);
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        // ahead(80);
        // turnRight(90);
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        double angle = e.getBearing();
        desiredAngle = getHeading() - 90;
        state = "aligning";

        // Replace the next line with any behavior you would like
        back(20);
    }
}
