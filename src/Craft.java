import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Craft {

    public CraftTrail trail;
    private Image craftImage;
    private DIRECTION currentDirection;
    private double x, y, dx, dy;
    private Boolean pressedUp, pressedDown, pressedLeft, pressedRight;
    private final int craftSize = 25;
    private final int defaultX = 300, defaultY = 300;

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
        craftImage = icon.getImage().getScaledInstance(craftSize, craftSize, Image.SCALE_SMOOTH);
        x = defaultX;
        y = defaultY;
        dx = 0;
        dy = 0;
        pressedDown = pressedLeft = pressedUp = pressedRight = false;
        currentDirection = DIRECTION.NORTH;
        trail = new CraftTrail();
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSize() {
        return craftSize;
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
        if (key == KeyEvent.VK_LEFT) {
            pressedLeft = true;
            dx = -1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            pressedRight = true;
            dx = 1;
        }
        if (key == KeyEvent.VK_UP) {
            pressedUp = true;
            dy = -1;
        }
        if (key == KeyEvent.VK_DOWN) {
            pressedDown = true;
            dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            pressedLeft = false;
            if (pressedRight) {
                dx = 1;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            pressedRight = false;
            if (pressedLeft) {
                dx = -1;
            } else {
                dx = 0;
            }
        }
        if (key == KeyEvent.VK_UP) {
            pressedUp = false;
            if (pressedDown) {
                dy = 1;
            } else {
                dy = 0;
            }
        }
        if (key == KeyEvent.VK_DOWN) {
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
