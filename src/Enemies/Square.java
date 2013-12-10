package Enemies;
import java.awt.*;

public class Square extends Enemy {

    public Square() {
        this.x = Math.random()*xBoundary + xOffset;
        this.y = Math.random()*yBoundary + yOffset;
        this.shape = new Polygon();
    }

    protected void drawShape(){
        this.shape.reset();
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2));
        this.shape.addPoint((int) (this.x - size / 2), (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2) + size);
        this.shape.addPoint((int) (this.x - size / 2) + size, (int) (this.y - size / 2));
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.cyan);
        graphics.drawPolygon(this.shape);
    }

    public void move(double targetX, double targetY) {
        double rad = Math.atan2((targetY - this.y),(targetX - this.x));
        this.x += Math.cos(rad)*velocity;
        this.y += Math.sin(rad)*velocity;
        this.drawShape();
    }
}
