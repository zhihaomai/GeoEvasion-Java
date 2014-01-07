package GameObjects;

import java.awt.*;
import java.util.ArrayList;

public class Explosion {

    private ArrayList<Spark> sparks;
    private final int numOfSparksPerExplosion = 50;

    public Explosion(double x, double y) {
        sparks = new ArrayList<Spark>();
        for (int i=0;i<numOfSparksPerExplosion;i++) {
            Color colour = new Color(255, (int)(Math.random()*255), 0);
            sparks.add (new Spark (x, y, colour));
        }
    }

    public void draw(Graphics graphics) {
        for (int i=0;i<sparks.size();i++) {
            if (sparks.get(i).getColour().getRed() < 20.0) {
                sparks.remove(i);
            } else {
                sparks.get(i).draw(graphics);
            }
        }
    }

    public int getSparkCount() {
        return sparks.size();
    }

    private class Spark {

        private final double sparkSlowdownRate = 0.92;
        private final double sparkColorFadeSpeed = 0.94;
        private final double sparkSize = 5.0;
        private final double velocityMaxRange = 3.0;
        private double x, y, velocity, vx, vy, radian;
        private Color colour;

        public Spark(double x, double y, Color colour) {
            this.radian = Math.toRadians(Math.random()*360);
            this.x = x;
            this.y = y;
            this.velocity = Math.random()*velocityMaxRange;
            this.vx = this.velocity*Math.cos(this.radian);
            this.vy = this.velocity*Math.sin(this.radian);
            this.colour = colour;
        }

        public Color getColour() {
            return this.colour;
        }

        public void draw(Graphics graphics) {
            graphics.setColor(this.colour);
            graphics.drawLine((int)this.x, (int)this.y, (int)(this.x + this.sparkSize*Math.cos(this.radian)), (int)(this.y + this.sparkSize*Math.sin(this.radian)));
            move();
        }

        private void move() {
            this.x += this.vx;
            this.vx *= sparkSlowdownRate;
            this.y += this.vy;
            this.vy *= sparkSlowdownRate;
            this.colour = new Color((int)(this.colour.getRed()*sparkColorFadeSpeed), (int)(this.colour.getGreen()*sparkColorFadeSpeed), (int)(this.colour.getBlue()*sparkColorFadeSpeed));
        }

    }
}
