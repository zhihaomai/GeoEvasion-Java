package Enemies;

import java.awt.*;

public class Diamond extends Enemy {

    private double angle;
    private double dx, dy;

    public Diamond() {
        this.x = Math.random()*xBoundary + xOffset;
        this.y = Math.random()*yBoundary + yOffset;
        this.angle = Math.random()*2*Math.PI;
        this.dx = Math.cos(this.angle)*velocity;
        this.dy = Math.sin(this.angle)*velocity;
        this.shape = new Polygon();
    }

    protected void drawShape(){
        this.shape.reset();
        this.shape.addPoint((int)(this.x), (int)(this.y - size));
        this.shape.addPoint((int)(this.x) + size/2, (int)(this.y));
        this.shape.addPoint((int)(this.x), (int)(this.y + size));
        this.shape.addPoint((int)(this.x) - size/2, (int)(this.y));
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.magenta);
        graphics.drawPolygon(this.shape);
    }

    public void move(double targetX, double targetY) {

        if (this.x + this.dx - this.size/2 >= xBoundary) {
            this.dx = Math.abs(this.dx)*-1;
        }
        if (this.x + this.dx - this.size/2 <= 0) {
            this.dx = Math.abs(this.dx);
        }
        if (this.y + this.dy - this.size/2 >= yBoundary) {
            this.dy = Math.abs(this.dy)*-1;
        }
        if (this.y + this.dy - this.size/2 <= 0) {
            this.dy = Math.abs(this.dy);
        }

        this.x += this.dx;
        this.y += this.dy;

        this.drawShape();
    }
}
