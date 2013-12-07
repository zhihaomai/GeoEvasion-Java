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
}
