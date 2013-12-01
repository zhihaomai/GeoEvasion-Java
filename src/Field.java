import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Field extends JPanel implements ActionListener {

    private final int boxSize = 25;
    private ArrayList enemies;
    private Craft craft;
    private Timer timer;

    public Field() {
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(new Adapter());

        craft = new Craft();
        timer = new Timer(5,this);
        timer.start();
    }

    public void paint (Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics.create();
        drawGrid(g2d);

        orientateCraft(g2d);
        g2d.drawImage(craft.getImage(), craft.getX()-craft.getSize()/2, craft.getY()-craft.getSize()/2, this);

        g2d.dispose();
    }

    public void drawGrid (Graphics2D g2d) {
        g2d.setColor(Color.green);
        int dimension = getWidth();
        for (int i=1;i<=Math.ceil(dimension/boxSize);i++) {
            int currentIncrement = i*boxSize;
            g2d.drawLine(currentIncrement, 0, currentIncrement, dimension);
            g2d.drawLine(0, currentIncrement, dimension, currentIncrement);
        }
    }

    public void orientateCraft (Graphics2D g2d) {
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
        }
        public void keyPressed(KeyEvent e) {
            craft.keyPressed(e);
        }
    }
}
