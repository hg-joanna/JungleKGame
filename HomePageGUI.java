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

        JLabel backgroundLabel = new JLabel(
            new ImageIcon("Resources/homepage_bg.png")
        );
        backgroundLabel.setBounds(0, 0, 1300, 900);
        homepageFrame.add(backgroundLabel);

        JButton startButton = new JButton("START");
        startButton.setBounds(565, 490, 191, 81);
        homepageFrame.add(startButton);

        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(565, 600, 191, 81);
        homepageFrame.add(exitButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(homepageFrame, "START CLICKED");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(homepageFrame, "EXIT CLICKED");
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