import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Field extends JPanel implements ActionListener {

    protected boolean []keys;
    private final int boxSize = 25;
    private final long bulletDelay = 300;
    private long lastBulletTime;
    private ArrayList<Bullet> bulletsInPlay;
    private ArrayList<Explosion> explosions;
    private Craft craft;
    private Timer timer;

    public Field() {
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(new Adapter());
        keys = new boolean[2000];
        craft = new Craft();
        bulletsInPlay = new ArrayList<Bullet>();
        explosions = new ArrayList<Explosion>();
        timer = new Timer(5,this);
        timer.start();
        lastBulletTime = System.currentTimeMillis();
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics.create();
        drawGrid(g2d);
        drawBullets(graphics);
        drawExplosions(graphics);
        drawCraft(graphics);
        if (keys['Z'] && (System.currentTimeMillis() > lastBulletTime + bulletDelay)) {
            bulletsInPlay.add(new Bullet(craft.getX(), craft.getY(), craft.getDirection()));
            lastBulletTime = System.currentTimeMillis();
        }
        g2d.dispose();
    }

    public void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.green);
        int dimension = getWidth();
        for (int i=1;i<=Math.ceil(dimension/boxSize);i++) {
            int currentIncrement = i*boxSize;
            g2d.drawLine(currentIncrement, 0, currentIncrement, dimension);
            g2d.drawLine(0, currentIncrement, dimension, currentIncrement);
        }
    }

    public void drawCraft(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        drawCraftTrail(graphics);
        orientateCraft(g2d);
        g2d.drawImage(craft.getImage(), (int)craft.getX()-craft.getSize()/2, (int)craft.getY()-craft.getSize()/2, this);
    }

    public void drawCraftTrail(Graphics graphics) {
        craft.trail.updateTrail();
        for (int i=0; i<craft.trail.getPoints().size(); i++){
            graphics.setColor(craft.trail.getFadeColors().get(i));
            graphics.fillRect((int) craft.trail.getPoints().get(i).getX(), (int) craft.trail.getPoints().get(i).getY(), 1, 1);
        }
    }

    public void drawBullets(Graphics graphics) {
        for (int i=0;i<bulletsInPlay.size();i++) {
            graphics.setColor(Color.red);
            bulletsInPlay.get(i).move();
            if (bulletsInPlay.get(i).getX() >= 600 || bulletsInPlay.get(i).getX() <= 0 || bulletsInPlay.get(i).getY() >= 600 || bulletsInPlay.get(i).getY() <= 0) {
                explosions.add (new Explosion(bulletsInPlay.get(i).getX(), bulletsInPlay.get(i).getY()));
                bulletsInPlay.remove(i);
            } else {
                graphics.drawPolygon(bulletsInPlay.get(i).getBulletShape());
            }
        }
    }

    public void drawExplosions(Graphics graphics) {
        for (int i=0;i<explosions.size();i++) {
            if (explosions.get(i).getSparkCount() > 0) {
                explosions.get(i).draw(graphics);
            } else {
                explosions.remove(i);
            }
        }
    }

    public void orientateCraft(Graphics2D g2d) {
        AffineTransform at = g2d.getTransform();
        at.rotate(craft.getDirection(), craft.getX(), craft.getY());
        g2d.setTransform(at);
    }

    public void actionPerformed(ActionEvent e) {
        craft.move();
        repaint();
    }

    private class Adapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
            keys[e.getKeyCode()] = false;
        }

        public void keyPressed(KeyEvent e) {
            craft.keyPressed(e);
            keys[e.getKeyCode()] = true;
        }
    }
}
