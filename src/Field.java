import Enemies.Square;
import Enemies.Enemy;

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
    private final int fieldWidth = 600;
    private final int fieldHeight = 600;
    private final long bulletDelay = 350;
    private long lastBulletTime;
    private ArrayList<Bullet> bulletsInPlay;
    private ArrayList<Explosion> explosions;
    private ArrayList<Enemy> enemies;

    private ArrayList<Rectangle> quadrants;
    private ArrayList<ArrayList<Enemy>> enemyQuadrants;

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
        enemies = new ArrayList<Enemy>();
        timer = new Timer(5,this);
        timer.start();
        lastBulletTime = System.currentTimeMillis();

        quadrants = new ArrayList<Rectangle>();
        enemyQuadrants = new ArrayList<ArrayList<Enemy>>();
        ArrayList<Enemy> northWestQuad = new ArrayList<Enemy>();
        enemyQuadrants.add (northWestQuad);
        quadrants.add(new Rectangle(0, 0, fieldWidth/2, fieldHeight/2));                    // North-west Quadrant
        ArrayList<Enemy> northEastQuad = new ArrayList<Enemy>();
        enemyQuadrants.add (northEastQuad);
        quadrants.add(new Rectangle(fieldWidth/2, 0, fieldWidth, fieldHeight/2));           // North-east Quadrant
        ArrayList<Enemy> southWestQuad = new ArrayList<Enemy>();
        enemyQuadrants.add (southWestQuad);
        quadrants.add(new Rectangle(0, fieldHeight/2, fieldWidth/2, fieldHeight));          // South-west Quadrant
        ArrayList<Enemy> southEastQuad = new ArrayList<Enemy>();
        enemyQuadrants.add (southEastQuad);
        quadrants.add(new Rectangle(fieldWidth/2, fieldHeight/2, fieldWidth, fieldHeight)); // South-east Quadrant
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics.create();

        populateEnemyQuadrants();

        drawGrid(g2d);
        drawBullets(graphics);
        drawExplosions(graphics);
        drawEnemies(graphics);
        drawCraft(graphics);
        g2d.dispose();
    }

    public void populateEnemyQuadrants() {
        for (ArrayList<Enemy> enemyQuadrant : enemyQuadrants) {
            enemyQuadrant.clear();
        }
        for (Enemy enemy : enemies) {
            for (int i=0; i<quadrants.size(); i++) {
                if (enemy.getBounds().intersects(quadrants.get(i))) {
                    enemyQuadrants.get(i).add(enemy);
                }
            }
        }
    }

    public void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.darkGray);
        int dimension = fieldWidth;
        for (int i=1; i<=Math.ceil(dimension/boxSize); i++) {
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

    public void drawEnemies(Graphics graphics) {
        for (int i=0; i<enemies.size(); i++) {
            enemies.get(i).move(craft.getX(), craft.getY());
            enemies.get(i).draw(graphics);
        }
        detectFellowEnemyCollisions();
    }

    public void drawBullets(Graphics graphics) {
        for (int i=0; i<bulletsInPlay.size(); i++) {
            graphics.setColor(Color.red);
            bulletsInPlay.get(i).move();
            // if bullet is out of play
            if (bulletsInPlay.get(i).getX() >= 600 || bulletsInPlay.get(i).getX() <= 0 || bulletsInPlay.get(i).getY() >= 600 || bulletsInPlay.get(i).getY() <= 0) {
                explosions.add (new Explosion(bulletsInPlay.get(i).getX(), bulletsInPlay.get(i).getY()));
                bulletsInPlay.remove(i);
            } else {
                graphics.drawPolygon(bulletsInPlay.get(i).getBulletShape());
                detectBulletCollision(bulletsInPlay.get(i));
            }
        }
        detectNewBullets();
    }

    public void drawExplosions(Graphics graphics) {
        for (int i=0; i<explosions.size(); i++) {
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

    public void detectNewBullets() {
        if (keys['Z'] && (System.currentTimeMillis() > lastBulletTime + bulletDelay)) {
            bulletsInPlay.add(new Bullet(craft.getX(), craft.getY(), craft.getDirection()));
            lastBulletTime = System.currentTimeMillis();
        } else if (keys['Q'] && enemies.size() < 50) {
            enemies.add(new Square());
        }
    }

    public void detectBulletCollision(Bullet bullet) {
        for (int j=0; j<quadrants.size(); j++) {
            if (bullet.getBulletShape().getBounds().intersects(quadrants.get(j))) {
                for (Enemy enemy : enemyQuadrants.get(j)) {
                    if (bullet.getBulletShape().getBounds().intersects(enemy.getBounds())) {
                        explosions.add (new Explosion(bullet.getX(), bullet.getY()));
                        bulletsInPlay.remove(bullet);
                        enemies.remove(enemy);
                        break;
                    }
                }
            }
        }
    }

    public void detectFellowEnemyCollisions() {
        for (Enemy enemy1 : enemies) {
            for (Enemy enemy2 : enemies) {
                if (enemy1.getBounds().intersects(enemy2.getBounds())){
                    enemy1.adjustForCollision(enemy2);
                }
            }
        }
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
