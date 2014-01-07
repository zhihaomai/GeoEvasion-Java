package GameObjects;

import Enemies.Diamond;
import Enemies.Square;
import Enemies.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Field extends JPanel implements ActionListener {

    protected boolean []keys;
    private enum GameState{
        INTRO,
        INPLAY,
        GAMEOVER
    }
    private GameState gameState;
    private int gameScore;
    private int lives;
    private final Font gameOverFont = new Font ("sansserif",Font.PLAIN, 32);
    private final Font scoreTextFont = new Font ("sansserif",Font.PLAIN, 14);
    private final Font livesTextFont = new Font ("sansserif",Font.PLAIN, 14);
    private final int boxSize = 25;
    private final int fieldWidth = 600;
    private final int fieldHeight = 600;
    private final long bulletDelay = 200;
    private float fadeOutAmount = 0;
    private float mx, my, mousedown = 0;
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
        addKeyListener(new kAdapter());
        addMouseListener(new mAdapter());
        addMouseMotionListener(new mAdapter());
        keys = new boolean[2000];
        gameState = GameState.INTRO;
        gameScore = 0;
        craft = new Craft();
        bulletsInPlay = new ArrayList<Bullet>();
        explosions = new ArrayList<Explosion>();
        enemies = new ArrayList<Enemy>();
        timer = new Timer(5,this);
        timer.start();
        lastBulletTime = System.currentTimeMillis();
        lives = 100;

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
        if (this.gameState == GameState.INTRO) {
            this.gameState = GameState.INPLAY;
        } else if (this.gameState == GameState.INPLAY) {
            populateEnemyQuadrants();
            drawGrid(graphics);
            drawScore(graphics);
            drawLives(graphics);
            drawBullets(graphics);
            drawExplosions(graphics);
            drawEnemies(graphics);
            drawCraft(graphics);
            drawAnimationIfDying(graphics);
            graphics.dispose();
        } else if (this.gameState == GameState.GAMEOVER) {
            drawGameOverScreen(graphics);
        }
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

    public void drawGrid(Graphics graphics) {
        graphics.setColor(Color.darkGray);
        int dimension = fieldWidth;
        for (int i=1; i<=Math.ceil(dimension/boxSize); i++) {
            int currentIncrement = i*boxSize;
            graphics.drawLine(currentIncrement, 0, currentIncrement, dimension);
            graphics.drawLine(0, currentIncrement, dimension, currentIncrement);
        }
    }

    public void drawCraft(Graphics graphics) {
        craft.draw(this, graphics);
    }

    public void drawEnemies(Graphics graphics) {
        detectEnemyCollisions();
        for (int i=0; i<enemies.size(); i++) {
            enemies.get(i).move(craft.getX(), craft.getY());
            enemies.get(i).draw(graphics);
        }
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

    public void detectNewBullets() {
        if (mousedown == 1 && (System.currentTimeMillis() > lastBulletTime + bulletDelay)) { // left-clicked
            double direction = Math.atan2(this.my - craft.getY(), this.mx - craft.getX());
            bulletsInPlay.add(new Bullet(craft.getX(), craft.getY(), direction));
            lastBulletTime = System.currentTimeMillis();
        } else if (keys['Z'] && enemies.size() < 100) {
            enemies.add(new Square(craft.getX(), craft.getY()));
        } else if (keys['X'] && enemies.size() < 100) {
            enemies.add(new Diamond(craft.getX(), craft.getY()));
        }
    }

    public void detectBulletCollision(Bullet bullet) {
        for (int j=0; j<quadrants.size(); j++) {
            if (bullet.getBulletShape().getBounds().intersects(quadrants.get(j))) {
                for (Enemy enemy : enemyQuadrants.get(j)) {
                    Rectangle bulletBounds = bullet.getBulletShape().getBounds();
                    if (enemy.getDodgeZone() != null && bulletBounds.intersects(enemy.getDodgeZone())) {
                        enemy.dodge(bullet);
                    } else {
                        enemy.stopDodging();
                    }
                    if (bulletBounds.intersects(enemy.getBounds())) {
                        explosions.add(new Explosion(bullet.getX(), bullet.getY()));
                        bulletsInPlay.remove(bullet);
                        enemies.remove(enemy);
                        this.gameScore += 10;
                        break;
                    }
                }
            }
        }
    }

    public void detectEnemyCollisions() {
        for (Enemy enemy1 : enemies) {
            if (enemy1.getBounds().intersects(craft.getBounds())) {
                craft.kill();
            }
            if (bulletsInPlay.size() == 0) {
                enemy1.stopDodging();
            }
            for (Enemy enemy2 : enemies) {
                if (enemy1.getBounds().intersects(enemy2.getBounds())){
                    enemy1.adjustForCollision(enemy2);
                }
            }
        }
    }

    public void drawAnimationIfDying(Graphics graphics) {
        if (!craft.isAlive()) {
            if (this.fadeOutAmount <= 0.99) {
                this.fadeOutAmount += 0.005;
                explosions.add(new Explosion(craft.getX(), craft.getY()));
                graphics.setColor(new Color(0, 0, 0, this.fadeOutAmount));
            } else {
                this.bulletsInPlay.clear();
                this.explosions.clear();
                this.enemies.clear();
                this.lives -= 1;
                if (lives == 0) {
                    gameState = GameState.GAMEOVER;
                } else {
                    craft.reborn();
                    this.fadeOutAmount = 0.0f;
                }

                // cause a delay
                try {
                    Thread.sleep(500);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void drawScore(Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.setFont(this.scoreTextFont);
        graphics.drawString("SCORE:" + this.gameScore, 5, 15);
    }

    public void drawLives(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(this.livesTextFont);
        graphics.drawString("LIVES:" + this.lives, 545, 15);
    }

    public void drawGameOverScreen(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setFont(this.gameOverFont);
        graphics.setColor(Color.red);
        graphics.drawString("GAME OVER", 200, 260);
        graphics.setColor(Color.green);
        graphics.drawString("SCORE: " + this.gameScore, 200, 300);
        timer.stop();
    }

    public void actionPerformed(ActionEvent e) {
        if (craft.isAlive()) {
            craft.move();
        }
        repaint();
    }

    private class kAdapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
            keys[e.getKeyCode()] = false;
        }

        public void keyPressed(KeyEvent e) {
            craft.keyPressed(e);
            keys[e.getKeyCode()] = true;
        }
    }

    private class mAdapter extends MouseAdapter {
        private void updateMouse(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
            updateMouse(e);
            mousedown = 0;
        }

        public void mouseClicked(MouseEvent e) {
            updateMouse(e);
            mousedown = 0;
        }

        public void mouseDragged(MouseEvent e) {
            updateMouse(e);
        }

        public void mouseMoved(MouseEvent e) {
            updateMouse(e);
        }

        public void mousePressed(MouseEvent e) {
            updateMouse(e);
            mousedown = e.getButton();
        }
    }
}
