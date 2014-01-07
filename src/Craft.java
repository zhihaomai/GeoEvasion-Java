import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Craft {

    public CraftTrail trail;
    private Image craftImage;
    private DIRECTION currentDirection;
    private double x, y, dx, dy;
    private boolean pressedUp, pressedDown, pressedLeft, pressedRight;
    private final int craftSize = 25;
    private final int defaultX = 300, defaultY = 300;
    private Rectangle bounds;
    private boolean alive;

    public enum DIRECTION {
        NORTH (0),
        NORTHEAST (Math.PI/4),
        EAST (Math.PI/2),
        SOUTHEAST (3*Math.PI/4),
        SOUTH (Math.PI),
        SOUTHWEST (5*Math.PI/4),
        WEST (3*Math.PI/2),
        NORTHWEST (7*Math.PI/4);
        private final double rotation;
        DIRECTION(double rotation) {
            this.rotation = rotation;
        }
        double getRotation() {
            return rotation;
        }
    }

    public Craft() {
        String imageFileName = "img/spacecraft.png";
        ImageIcon icon = new ImageIcon(this.getClass().getResource(imageFileName));
        this.craftImage = icon.getImage().getScaledInstance(craftSize, craftSize, Image.SCALE_SMOOTH);
        this.x = defaultX;
        this.y = defaultY;
        this.dx = 0;
        this.dy = 0;
        this.pressedDown = pressedLeft = pressedUp = pressedRight = false;
        this.currentDirection = DIRECTION.NORTH;
        this.trail = new CraftTrail();
        this.bounds = new Rectangle((int)this.x, (int)this.y, this.craftSize, this.craftSize);
        this.alive = true;
    }

    public void move() {
        if (dx > 0 && (x+dx+craftSize/2) < 600) {
            x += dx*Math.abs(Math.sin(currentDirection.getRotation()));
            trail.createPoint((int)this.x, (int)this.y);
        } else if (dx < 0 && (x+dx-craftSize/2) > 0) {
            x += dx*Math.abs(Math.sin(currentDirection.getRotation()));
            trail.createPoint((int)this.x, (int)this.y);
        }

        if (dy > 0 && (y+dy+craftSize/2) < 600) {
            y += dy*Math.abs(Math.cos(currentDirection.getRotation()));
            trail.createPoint((int)this.x, (int)this.y);
        } else if (dy < 0 && (y+dy-craftSize/2) > 0) {
            y += dy*Math.abs(Math.cos(currentDirection.getRotation()));
            trail.createPoint((int)this.x, (int)this.y);
        }
    }

    public void draw(Field field, Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        // craft tail
        this.trail.updateTrail();
        for (int i=0; i<this.trail.getPoints().size(); i++){
            graphics.setColor(this.trail.getFadeColors().get(i));
            graphics.fillRect((int) this.trail.getPoints().get(i).getX(), (int) this.trail.getPoints().get(i).getY(), 1, 1);
        }
        // craft itself
        this.bounds = null;
        this.bounds = new Rectangle((int)this.x-this.craftSize/2, (int)this.y-this.craftSize/2, this.craftSize, this.craftSize);
        AffineTransform at = g2d.getTransform();
        at.rotate(this.getDirection(), this.x, this.y);
        g2d.setTransform(at);
        g2d.drawImage(this.getImage(), (int)this.x-this.craftSize/2, (int)this.y-this.craftSize/2, field);
        at.rotate(-this.getDirection(), this.x, this.y);
        g2d.setTransform(at);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void kill() {
        this.alive = false;
    }

    public void reborn() {
        this.alive = true;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public double getDirection() {
        if (dx > 0) {
            if (dy > 0) {
                currentDirection = DIRECTION.SOUTHEAST;
            } else if (dy < 0) {
                currentDirection = DIRECTION.NORTHEAST;
            } else {
                currentDirection = DIRECTION.EAST;
            }
        } else if (dx < 0) {
            if (dy > 0) {
                currentDirection = DIRECTION.SOUTHWEST;
            } else if (dy < 0) {
                currentDirection = DIRECTION.NORTHWEST;
            } else {
                currentDirection = DIRECTION.WEST;
            }
        } else {
            if (dy > 0) {
                currentDirection = DIRECTION.SOUTH;
            } else if (dy < 0) {
                currentDirection = DIRECTION.NORTH;
            }
        }
        return currentDirection.getRotation();
    }

    public Image getImage() {
        return craftImage;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            pressedLeft = true;
            dx = -1;
        }
        if (key == KeyEvent.VK_D) {
            pressedRight = true;
            dx = 1;
        }
        if (key == KeyEvent.VK_W) {
            pressedUp = true;
            dy = -1;
        }
        if (key == KeyEvent.VK_S) {
            pressedDown = true;
            dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            pressedLeft = false;
            if (pressedRight) {
                dx = 1;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_D) {
            pressedRight = false;
            if (pressedLeft) {
                dx = -1;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_W) {
            pressedUp = false;
            if (pressedDown) {
                dy = 1;
            } else {
                dy = 0;
            }
        }
        if (key == KeyEvent.VK_S) {
            pressedDown = false;
            if (pressedUp) {
                dy = -1;
            } else {
                dy = 0;
            }
        }
    }

    public class CraftTrail {

        private ArrayList<Point> points;
        private ArrayList<Color> fadeColors;
        private final double fadeSpeed = 0.98;
        private final int trailSize = 10;

        public CraftTrail() {
            points = new ArrayList<Point>();
            fadeColors = new ArrayList<Color>();
        }

        public ArrayList<Point> getPoints() {
            return this.points;
        }

        public ArrayList<Color> getFadeColors() {
            return this.fadeColors;
        }

        public void createPoint(int x, int y) {
            for (int i=0;i<3;i++) {
                points.add(new Point(x  - trailSize/2 + (int)(Math.random() * trailSize), y  - trailSize/2 + (int)(Math.random() * trailSize)));
                fadeColors.add (new Color(245, 170, 0));
            }
        }

        public void updateTrail() {
            for (int i=0;i<points.size();i++) {
                fadeColors.set(i, new Color((int)(fadeColors.get(i).getRed()*fadeSpeed), (int)(fadeColors.get(i).getGreen()*fadeSpeed),(int)(fadeColors.get(i).getBlue()*fadeSpeed)));
                if (fadeColors.get(i).getRed()<=10){
                    points.remove(i);
                    fadeColors.remove(i);
                }
            }
        }
    }
}
