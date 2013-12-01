import javax.swing.JFrame;

public class GeoEvasion extends JFrame {
    public GeoEvasion() {
        add (new Board());
        setTitle("GeoEvasion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GeoEvasion();
    }
}
