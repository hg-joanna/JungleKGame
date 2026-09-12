
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the graphical user interface of the homepage
 */
public class HomePageGUI {
    private JFrame homepageFrame;

    /**
     * Instantiates the homepage of the Jungle King game
     */
    public HomePageGUI() {
        homepageFrame = new JFrame();
        homepageFrame.setTitle("Jungle King - Homepage");
        homepageFrame.setSize(1300, 900);
        homepageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homepageFrame.setResizable(false);
        homepageFrame.setLayout(null);
        homepageFrame.setLocationRelativeTo(null);

        JLayeredPane layer = new JLayeredPane();
        layer.setBounds(0, 0, 1300, 900);
        homepageFrame.add(layer);

        // Background
        ImageIcon homepageBG = new ImageIcon(
            "Resources/homepage_bg.png"
        );

        JLabel backgroundLabel = new JLabel(homepageBG);
        backgroundLabel.setBounds(0, 0, 1300, 900);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // Start button
        ImageIcon startImageIcon = new ImageIcon(
            "Resources/start_button.png"
        );

        System.out.println(
            "Start image loaded: " + (startImageIcon.getIconWidth() > 0)
        );

        JButton startButton = new JButton(startImageIcon);
        startButton.setBounds(565, 490, 191, 81);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        layer.add(startButton, Integer.valueOf(1));

        // Exit button
        ImageIcon exitImageIcon = new ImageIcon(
            "Resources/exit_button.png"
        );

        System.out.println(
            "Exit image loaded: " + (exitImageIcon.getIconWidth() > 0)
        );

        JButton exitButton = new JButton(exitImageIcon);
        exitButton.setBounds(565, 600, 191, 81);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        layer.add(exitButton, Integer.valueOf(1));

        // Start button action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    homepageFrame,
                    "START CLICKED"
                );
            }
        });

        // Exit button action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    homepageFrame,
                    "EXIT CLICKED"
                );
            }
        });

        homepageFrame.setVisible(true);
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new HomePageGUI();
    }
}

