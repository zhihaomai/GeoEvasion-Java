package Enemies;

import java.awt.*;

public class Square implements Enemy {

    final private double velocity = 0.5;
    final private int size = 20;
    private double x, y;

    public Square() {
        this.x = Math.random()*xBoundary + xOffset;
        this.y = Math.random()*yBoundary + yOffset;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void move(double targetX, double targetY) {
        double rad = Math.atan2((targetY - this.y),(targetX - this.x));
        this.x += Math.cos(rad)*velocity;
        this.y += Math.sin(rad)*velocity;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.cyan);
        graphics.drawRect((int) (this.x - size / 2), (int) (this.y - size / 2), size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) (this.x - size / 2), (int) (this.y - size / 2), size, size);
    }

    public void adjustForCollision(Enemy enemy) {
        double distanceApart = distance(this.x, this.y, enemy.getX(), enemy.getY());
        if (distanceApart != 0) {
            this.x += (this.x-enemy.getX())/(distanceApart);
            this.y += (this.y-enemy.getY())/(distanceApart);
        }
    }

    public double distance(double x1, double y1, double x2, double y2){
        return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
    }
}
