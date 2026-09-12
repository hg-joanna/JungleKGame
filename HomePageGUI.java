import javax.swing.*;

public class HomePageGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Homepage Test");
        frame.setSize(1300, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon bg = new ImageIcon(
            HomePageGUI.class.getResource("/Resources/homepage_bg.png")
        );

        System.out.println(bg.getIconWidth());
        System.out.println(bg.getIconHeight());

        JLabel label = new JLabel(bg);
        frame.add(label);

        frame.setVisible(true);
    }
}