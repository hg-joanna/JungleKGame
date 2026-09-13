import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Represents the graphical user interface of the rules of the game.
 */
public class RuleFrameGUI extends JFrame {

    /** Determines whether the rules are shown before the game starts. */
    private boolean beforeGameStarted;

    /**
     * Instantiates the GUI for the rules of the game.
     *
     * @param beforeGameStarted boolean flag to track if the rule view
     *                          is clicked on before the game starts
     */
    public RuleFrameGUI(boolean beforeGameStarted) {

        this.beforeGameStarted = beforeGameStarted;

        setTitle("Jungle King - Rules");
        setSize(850, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Logo beside the title
        ImageIcon logo =
            getResourceIcon("/Resources/logo.png");

        setIconImage(logo.getImage());

        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(null);
        setContentPane(layer);

        // Rules background
        ImageIcon rulesBG =
            getResourceIcon("/Resources/rules.png");

        JLabel backgroundLabel =
            new JLabel(rulesBG);

        backgroundLabel.setBounds(
            0,
            0,
            850,
            800
        );

        layer.add(
            backgroundLabel,
            Integer.valueOf(0)
        );

        // Action button
        JButton actionButton = new JButton();

        ImageIcon nextIcon =
            getResourceIcon(
                "/Resources/next_rules.png"
            );

        ImageIcon nextIconHover =
            getResourceIcon(
                "/Resources/next_rules_hovered.png"
            );

        ImageIcon closeIcon =
            getResourceIcon(
                "/Resources/exit_rules.png"
            );

        ImageIcon closeIconHover =
            getResourceIcon(
                "/Resources/exit_rules_hovered.png"
            );

        if (beforeGameStarted) {

            actionButton.setIcon(nextIcon);
            actionButton.setRolloverIcon(
                nextIconHover
            );

            actionButton.setBounds(
                653,
                675,
                145,
                62
            );

            actionButton.addActionListener(
                new NextButtonListener(this)
            );

        } else {

            actionButton.setIcon(closeIcon);
            actionButton.setRolloverIcon(
                closeIconHover
            );

            actionButton.setBounds(
                653,
                675,
                145,
                62
            );

            actionButton.addActionListener(
                new CloseButtonListener(this)
            );
        }

        actionButton.setBorderPainted(false);
        actionButton.setContentAreaFilled(false);
        actionButton.setFocusPainted(false);

        layer.add(
            actionButton,
            JLayeredPane.POPUP_LAYER
        );

        setVisible(true);
    }

    /**
     * Loads an image from the JAR resources.
     */
    private ImageIcon getResourceIcon(String path) {

        java.net.URL resource =
            RuleFrameGUI.class.getResource(path);

        return new ImageIcon(resource);
    }

    /**
     * Opens PlayerSetupGUI after the rules.
     */
    private void proceedToPlayerSetup() {

        new PlayerSetupGUI();

        dispose();
    }

    /**
     * Listener for the NEXT button.
     */
    public static class NextButtonListener
        implements ActionListener {

        private RuleFrameGUI gui;

        public NextButtonListener(
            RuleFrameGUI gui
        ) {
            this.gui = gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {
            gui.proceedToPlayerSetup();
        }
    }

    /**
     * Listener for the CLOSE button.
     */
    public static class CloseButtonListener
        implements ActionListener {

        private RuleFrameGUI gui;

        public CloseButtonListener(
            RuleFrameGUI gui
        ) {
            this.gui = gui;
        }

        public void actionPerformed(
            ActionEvent e
        ) {
            gui.dispose();
        }
    }

    /**
     * Main method.
     *
     * @param args main method arguments
     */
    public static void main(String[] args) {
        new RuleFrameGUI(true);
    }
}