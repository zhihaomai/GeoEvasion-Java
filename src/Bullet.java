import java.awt.*;

public class Bullet {

    private final double velocity = 1.5;
    private double x, y, vx, vy, radian;
    private Polygon bulletShape;

    public Bullet(double x, double y, double radian) {
        this.x = x;
        this.y = y;
        this.vx = Math.cos(radian)*velocity;
        this.vy = Math.sin(radian)*velocity;
        this.radian = radian;
        bulletShape = new Polygon();
        drawShape();
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void move() {
        this.x += vx;
        this.y += vy;
        drawShape();
    }

    public Polygon getBulletShape() {
        return this.bulletShape;
    }

    private void drawShape(){
        bulletShape.reset();
        double width = 5;
        double size = 20;
        bulletShape.addPoint((int)(this.x - width/2*Math.sin(this.radian)),(int)(this.y + width/2*Math.cos(this.radian)));
        bulletShape.addPoint((int)(this.x + width/2*Math.sin(this.radian)),(int)(this.y - width/2*Math.cos(this.radian)));
        bulletShape.addPoint((int)(this.x + size*Math.cos(this.radian)), (int)(this.y + size*Math.sin(this.radian)));
    }
}
