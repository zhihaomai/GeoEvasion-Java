package Enemies;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Enemy {
    private Boolean isParent;
    final private int childSize = 15;
    final private int parentSize = 30;

    public Circle(double cx, double cy, Boolean isParent) {
        double potentialX, potentialY;

        if (isParent) {
            this.size = parentSize;
            while (true) {
                potentialX = Math.random()*xBoundary + xOffset;
                potentialY = Math.random()*yBoundary + yOffset;
                if (potentialX > cx + this.enemySpawnPadding || potentialX < cx - this.enemySpawnPadding || potentialY > cy + this.enemySpawnPadding || potentialY < cy - this.enemySpawnPadding) {
                    break;
                }
            }
        } else {
            this.size = childSize;
            potentialX = Math.random()*50 - 25 + cx;
            potentialY = Math.random()*50 - 25 + cy;
        }

        this.isParent = isParent;
        this.x = potentialX;
        this.y = potentialY;
        this.shape = new Polygon();
    }

    protected void drawShape(){
        this.dodgeZone = null;
        this.shape.reset();
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2));
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2));
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.magenta);
        graphics.drawOval((int)this.x - size / 2, (int)this.y - size / 2 , size, size);
    }

    public void move(double targetX, double targetY) {
        double rad = Math.atan2((targetY - this.y),(targetX - this.x));
        this.x += Math.cos(rad)*velocity;
        this.y += Math.sin(rad)*velocity;
        this.drawShape();
    }

    public Boolean isCircleAParent() {
        return this.isParent;
    }
}
