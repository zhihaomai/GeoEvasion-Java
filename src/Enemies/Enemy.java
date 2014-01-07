package Enemies;
import GameObjects.Bullet;
import java.awt.*;

public abstract class Enemy {

    //Boundary variables used so Enemies are not generated off screen
    final double xOffset = 10;
    final double xBoundary = 580;
    final double yOffset = 10;
    final double yBoundary = 580;
    final double enemySpawnPadding = 100;

    final protected double velocity = 0.5;
    final protected int size = 20;
    protected double x, y;
    protected Polygon shape;

    protected Rectangle dodgeZone;
    protected Bullet incomingBullet;
    final protected int dodgeZoneSize = 100;
    protected Boolean isDodging = false;

    public abstract void draw(Graphics graphics);
    public abstract void move(double targetX, double targetY);
    protected abstract void drawShape();

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Rectangle getBounds() {
        return this.shape.getBounds();
    }

    public Rectangle getDodgeZone() {
        return this.dodgeZone;
    }

    public void dodge(Bullet b) {
        this.incomingBullet = b;
        this.isDodging = true;
    }

    public void stopDodging() {
        this.incomingBullet = null;
        this.isDodging = false;
    }

    public void adjustForCollision(Enemy enemy) {
        double distanceApart = distance(this.x, this.y, enemy.getX(), enemy.getY());
        if (distanceApart != 0) {
            this.x += (this.x-enemy.getX())/(distanceApart);
            this.y += (this.y-enemy.getY())/(distanceApart);
        }
    }

    protected double distance(double x1, double y1, double x2, double y2){
        return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
    }
}
