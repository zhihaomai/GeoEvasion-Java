import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {
    private ArrayList enemies;

    public Board() {

    }
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class Adapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            //when keyboard key is released
        }
        public void keyPressed(KeyEvent e) {
            //when keyboard key is pressed down
        }
    }
}
