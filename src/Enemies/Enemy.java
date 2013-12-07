package Enemies;

import java.awt.*;

public interface Enemy {

    //Boundary variables used so Enemies are not generated off screen
    final double xOffset = 10;
    final double xBoundary = 580;
    final double yOffset = 10;
    final double yBoundary = 580;

    public double getX();
    public double getY();
    public void move(double targetX, double targetY);
    public void draw(Graphics graphics);
    public Rectangle getBounds();
    public void adjustForCollision(Enemy enemy);
}
