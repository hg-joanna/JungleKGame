import javax.swing.*;

public class HomePageGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Homepage Test");
        frame.setSize(1300, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon bg = new ImageIcon("Resources/homepage_bg.png");
        JLabel label = new JLabel(bg);
        frame.add(label);

        frame.setVisible(true);
    }
}