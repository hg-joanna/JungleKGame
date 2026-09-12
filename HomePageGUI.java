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
        homepageFrame.setLocationRelativeTo(null); // centers on screen

        // logo beside the title
        ImageIcon logo = new ImageIcon("Resources/logo.png");
        homepageFrame.setIconImage(logo.getImage());

        JLayeredPane layer = new JLayeredPane();
        layer.setBounds(0, 0, 1300, 900);
        homepageFrame.add(layer);

        ImageIcon homepageBG = new ImageIcon("Resources/homepage_bg.png");
        JLabel backgroundLabel = new JLabel(homepageBG);
        backgroundLabel.setBounds(0, 0, 1300, 900);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // start button
        ImageIcon startImageIcon = new ImageIcon("Resources/start_button.png");
        ImageIcon startImageIconHover = new ImageIcon("Resources/start_button_hovered.png");
        JButton startButton = new JButton(startImageIcon);
        // default and hover
        startButton.setIcon(startImageIcon);
        startButton.setRolloverIcon(startImageIconHover);
        startButton.setBounds(565, 490, 191, 81);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        layer.add(startButton, Integer.valueOf(1));

        // exit button
        ImageIcon exitImageIcon = new ImageIcon("Resources/exit_button.png");
        ImageIcon exitImageIconHover = new ImageIcon("Resources/exit_button_hovered.png");
        JButton exitButton = new JButton(exitImageIcon);
        exitButton.setIcon(exitImageIcon);
        exitButton.setRolloverIcon(exitImageIconHover);
        exitButton.setBounds(565, 600, 191, 81);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        layer.add(exitButton, Integer.valueOf(1));

        // action listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRuleFrame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        homepageFrame.setVisible(true);
    }

    /** Open the RuleFrameGUI after mouse click */
    public void openRuleFrame() {
        homepageFrame.dispose();

        JFrame testFrame = new JFrame("Rule Frame Test");
        testFrame.setSize(600, 400);
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setVisible(true);
    }

    /**
     * Main method
     * @param args main method
     */
    public static void main(String[] args) {
        new HomePageGUI();
    }
}
