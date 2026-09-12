import javax.swing.*;

public class HomePageGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Homepage Test");
        frame.setSize(1300, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Background
        ImageIcon bg = new ImageIcon(
            HomePageGUI.class.getResource("/Resources/homepage_bg.png")
        );

        JLabel backgroundLabel = new JLabel(bg);
        backgroundLabel.setBounds(0, 0, 1300, 900);
        frame.add(backgroundLabel);

        // Start button
        ImageIcon startImage = new ImageIcon(
            HomePageGUI.class.getResource("/Resources/start_button.png")
        );

        JButton startButton = new JButton(startImage);
        startButton.setBounds(565, 490, 191, 81);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);

        frame.add(startButton);

        frame.setVisible(true);
    }
}