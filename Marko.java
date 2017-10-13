package killbots;
import robocode.*;
import java.awt.Color;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Marko - a robot by Vinícius Garcia
 */
public class Marko extends Robot {
    private boolean ra = false;
    private double enemyBearing = 180;
    private boolean firing = false;
    private String estado = "inicio";

    /**
     * run: Marko's default behavior
     */
    public void run() {
        // setColors(Color.red,Color.blue,Color.green); // body,gun,radar
        // Robot main loop
        setBodyColor(new Color(200, 200, 30));
        setGunColor(new Color(45, 100, 0));
        setRadarColor(new Color(0, 255, 100));
        setBulletColor(new Color(255, 255, 100));
        setScanColor(new Color(0, 255, 0));

        while(true) {
            moveTank();
            moveCannon();
        }
    }

    private void moveTank() {
        // quando começar o robo ira direto pro canto
        if (estado == "inicio") {
            turnToHeading(90);
            ahead(1000);
            estado = "combate";
        }

        // Make sure the tank is always ortogonal to the borders
        if (getHeading() % 90 != 0) {
            double round = Math.round(getHeading() / 90) * 90;
            turnToHeading(round);
        }

        ahead(150);
        turnGunRight(180);
        turnGunLeft(180);
    }

    public void moveCannon() {
    }

    public void turnCannon(double heading) {
        double angle = turnAngle(getGunHeading(), heading);
        turnGunRight(angle);
    }

    public void turnToHeading(double heading) {
        double angle = turnAngle(getHeading(), heading);

        // Turn to the desired position:
        turnRight(angle);
    }

    public void RevidarAtaque() {
        if (ra) {
            fire(10);
            ra = false;
            //Esse seria assim que eu tomar um tiro eu disparo
        }
    }
    
    public void anteciparAtaque() {
    // Este método será para quando for chamado ele tente encontrar o robo
    // assim que encontrar dispara imediatamente.
    // Pode ficar a vontade em criar estrategias para tiro
    // Seguinte pode ficar a vontade assim que o meu amigo finalizar
    // eu te mando e mexe sempre da função Movecannon

        while(true) {
            if (firing) {
                turnGunRight(enemyBearing);
                firing = false;
            } else {
                turnGunRight(-90);
            }
        // turnGunRight(360);
       }
    }

    // onScannedRobot: What to do when you see another robot
    public void onScannedRobot(ScannedRobotEvent e) {
        double distance = e.getDistance();
        firing = true;

        // controla a potencia do tiro de acordo com a distancia do inimigo
        if (distance >= 600) {
            fire(1);
        } else if (distance >= 400) {
            fire(2);
        } else if (distance >= 300) {
            fire(5);
        } else if (distance >= 150) {
            fire(8);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // corre quando atingido
        ahead(1000);
        ra = true;
        // turnRight(90);
    }

    public void onHitWall(HitWallEvent e) {
        // vira 90 graus ao bater na parede
        turnRight(90);
    }

    public void onHitRobot(HitRobotEvent e){
        double angle = e.getBearing();

        // vira tambem quando bater em um robo de frente:
        if (angle < 45 && angle > -45) {
            turnRight(90);
        }
    }

    /* * * * Private Helper Funcs * * * */
    
    // Calcula o menor angulo necessário para virar
    // para a direita do angulo `from` para `to`
    private double turnAngle(double from, double to) {
        double diff = (to % 360) - (from % 360);

        // Make sure to turn the shorter angle between both:
        if (Math.abs(diff) > 180) {
            return diff < 0 ? diff + 360 : diff - 360;
        } else {
            return diff;
        }
    }
}
