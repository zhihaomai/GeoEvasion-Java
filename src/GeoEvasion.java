import javax.swing.JFrame;

public class GeoEvasion extends JFrame {

    public GeoEvasion() {
        add (new Field());
        setTitle("GeoEvasion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(606,628); // 600x600 accounting for sides
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GeoEvasion();
    }
}
