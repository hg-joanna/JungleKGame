import javax.swing.*;

public class HomePageGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Homepage Test");
        frame.setSize(400, 300);

        JLabel label = new JLabel("HOMEPAGE WORKS");
        frame.add(label);

        frame.setVisible(true);
    }
}