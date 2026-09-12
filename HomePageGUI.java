
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

        JLabel testLabel = new JLabel("HOMEPAGE TEST");
        testLabel.setBounds(500, 400, 300, 50);
        homepageFrame.add(testLabel);

        homepageFrame.setVisible(true);
    }

    /** Open the RuleFrameGUI after mouse click */
    public void openRuleFrame() {
        homepageFrame.dispose();
        RuleFrameGUI ruleFrame = new RuleFrameGUI(true);
        ruleFrame.setVisible(true);
    }

    /**
     * Main method
     * @param args main method
     */
    public static void main(String[] args) {
        new HomePageGUI();
    }
}

