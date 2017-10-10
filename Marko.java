package killbots;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Marko - a robot by Vinĩcius Garcia
 */
public class Marko extends Robot
{
    private double enemyBearing = 180;
    private boolean firing = false;

	/**
	 * run: Marko's default behavior
	 */
	public void run() {
		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			ahead(200);
            // turnRight(90);
            if (firing) {
			    turnGunRight(enemyBearing);
                firing = false;
            } else {
                turnGunRight(-90);
            }

			back(100);
                // turnLeft(90);
			    // turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
        out.println("Velocidade: " + e.getVelocity());
        out.println("Energia: " + e.getEnergy());
        out.println("Distancia: " + e.getDistance());
        out.println("Direção: " + e.getHeading());
        out.println("Bearing: " + e.getBearing());

        enemyBearing = e.getBearing();
        firing = true;
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		ahead(80);
        // turnRight(90);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}
