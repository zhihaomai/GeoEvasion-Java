import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Craft {

    private String imageFileName = "img/spacecraft.png";
    private Image craftImage;
    private DIRECTION currentDirection;
    private int x, y, dx, dy;
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
        System.out.println (this.getClass().getResource(imageFileName));
        ImageIcon icon = new ImageIcon(this.getClass().getResource(imageFileName));
        craftImage = icon.getImage().getScaledInstance(craftSize, craftSize, Image.SCALE_SMOOTH);
        x = defaultX;
        y = defaultY;
        dx = 0;
        dy = 0;
        currentDirection = DIRECTION.NORTH;
    }

    public void move() {
        if (dx > 0 && (x+dx+craftSize/2) < 600) {
            x += dx;
        } else if (dx < 0 && (x+dx-craftSize/2) > 0) {
            x += dx;
        }

        if (dy > 0 && (y+dy+craftSize/2) < 600) {
            y += dy;
        } else if (dy < 0 && (y+dy-craftSize/2) > 0) {
            y += dy;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
            dx = -1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}
