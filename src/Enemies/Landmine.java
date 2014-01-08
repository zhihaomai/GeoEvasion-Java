package Enemies;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Landmine extends Enemy {
    private double rotationAngle;
    final private double rotationSpeed = 1;

    public Landmine(double cx, double cy) {
        double potentialX, potentialY;
        while (true) {
            potentialX = Math.random()*xBoundary + xOffset;
            potentialY = Math.random()*yBoundary + yOffset;
            if (potentialX > cx + enemySpawnPadding || potentialX < cx - enemySpawnPadding || potentialY > cy + enemySpawnPadding || potentialY < cy - enemySpawnPadding) {
                break;
            }
        }
        this.x = potentialX;
        this.y = potentialY;
        this.shape = new Polygon();
        this.rotationAngle = 0;
    }

    protected void drawShape() {
        this.dodgeZone = null;
        this.rotationAngle += Math.toRadians(rotationSpeed);

        this.shape.reset();
        this.shape.addPoint((int)this.x, (int)this.y);
        this.shape.addPoint((int)(this.x - size / 4), (int)this.y + size);
        this.shape.addPoint((int)(this.x + size / 4), (int)this.y + size);
        this.shape.addPoint((int)this.x, (int)this.y);
        this.shape.addPoint((int)this.x + size, (int)(this.y - size / 4));
        this.shape.addPoint((int)this.x + size, (int)(this.y + size / 4));
        this.shape.addPoint((int)this.x, (int)this.y);
        this.shape.addPoint((int)(this.x - size / 4), (int)this.y - size);
        this.shape.addPoint((int)(this.x + size / 4), (int)this.y - size);
        this.shape.addPoint((int)this.x, (int)this.y);
        this.shape.addPoint((int)this.x - size, (int)(this.y - size / 4));
        this.shape.addPoint((int)this.x - size, (int)(this.y + size / 4));
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.orange);
        Graphics2D g2d = (Graphics2D) graphics;
        AffineTransform at = g2d.getTransform();
        at.rotate(this.rotationAngle, this.x, this.y);
        g2d.setTransform(at);
        g2d.drawPolygon(this.shape);
        at.rotate(-this.rotationAngle, this.x, this.y);
        g2d.setTransform(at);
    }

    public void move(double targetX, double targetY) {
        this.drawShape();
    }
}
