package Enemies;

import java.awt.*;

public abstract class Enemy {

    //Boundary variables used so Enemies are not generated off screen
    final double xOffset = 10;
    final double xBoundary = 580;
    final double yOffset = 10;
    final double yBoundary = 580;

    final protected double velocity = 0.5;
    final protected int size = 20;
    protected double x, y;
    protected Polygon shape;

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
