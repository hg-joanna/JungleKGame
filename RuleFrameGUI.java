import javax.swing.*;

/**
 * Represents the graphical user interface of the rules of the game
 */
public class RuleFrameGUI extends JFrame {
	/**Determines what button is shown on screen*/
    private boolean beforeGameStarted;

    /**
     * Instantiates the GUI for the rules of the game
     * @param beforeGameStarted boolean flag to track if the rule view is clicked on before the game starts
     */
    public RuleFrameGUI(boolean beforeGameStarted) {
        this.beforeGameStarted = beforeGameStarted; // Flag to track if rules are before game start
        setTitle("Jungle King - Rules");
        setSize(850, 800); 
        setResizable(false); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 

        // logo beside the title
        ImageIcon logo = new ImageIcon("Resources/logo.png");
        setIconImage(logo.getImage());
        
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(null);
        setContentPane(layer);

        ImageIcon rulesBG = new ImageIcon("Resources/rules.png");
        JLabel backgroundLabel = new JLabel(rulesBG);
        backgroundLabel.setBounds(0, 0, 850, 800);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // Create the action button
        JButton actionButton = new JButton();

        ImageIcon nextIcon = new ImageIcon("Resources/next_rules.png");
        ImageIcon nextIconHover = new ImageIcon("Resources/next_rules_hovered.png");
        ImageIcon closeIcon = new ImageIcon("Resources/exit_rules.png");
        ImageIcon closeIconHover = new ImageIcon("Resources/exit_rules_hovered.png");

        // Set the button properties based on beforeGameStarted flag
        if (beforeGameStarted) {
            actionButton.setIcon(nextIcon);
            actionButton.setRolloverIcon(nextIconHover);
            actionButton.setBounds(653, 675, 145, 62); 
            actionButton.addActionListener(e -> proceedToPlayerSetup());
        } else {
            actionButton.setIcon(closeIcon);
            actionButton.setRolloverIcon(closeIconHover);
            actionButton.setBounds(653, 675, 145, 62);
            actionButton.addActionListener(e -> dispose());
        }

        actionButton.setBorderPainted(false);  // Remove the button border
        actionButton.setContentAreaFilled(false);  // Make the button transparent
        actionButton.setFocusPainted(false);  // Remove the focus border

        layer.add(actionButton, JLayeredPane.POPUP_LAYER);

        setVisible(true);
    }

	/**opens PlayerSetupGUI */
    private void proceedToPlayerSetup() {
        SwingUtilities.invokeLater(() -> {
            new PlayerSetupGUI();
            dispose();  
        });
    }

    /**
     * Main method
     * @param args main method
     */
    public static void main(String[] args) {
        new RuleFrameGUI(true);
    }
}