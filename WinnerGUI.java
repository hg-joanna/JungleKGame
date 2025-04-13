import java.awt.*;
import javax.swing.*;

/**
 * Represents the graphical user interface displaying the winner of the game and the option to play again or exit game
 */
public class WinnerGUI {
    private JFrame winnerFrame;

    /**
     * Instantiates the winner frame of the player when the game is over
     * @param winnerName name of the player who won the game
     */
    public WinnerGUI(String winnerName)
    {
        winnerFrame = new JFrame();
        winnerFrame.setTitle("Jungle King - Winner");
        winnerFrame.setSize(850, 800);
        winnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winnerFrame.setResizable(false);
        winnerFrame.setLayout(null);
        winnerFrame.setLocationRelativeTo(null);
        
        // logo beside the title
        ImageIcon logo = new ImageIcon("Resources/logo.png");
        winnerFrame.setIconImage(logo.getImage());

        JLayeredPane layer = new JLayeredPane();
        layer.setBounds(0, 0, 850, 800);
        winnerFrame.add(layer);

        ImageIcon winnerBG = new ImageIcon("Resources/winner_bg.png");
        JLabel backgroundLabel = new JLabel(winnerBG);
        backgroundLabel.setBounds(0, 0, 850, 800);
        layer.add(backgroundLabel, Integer.valueOf(0));

        // display winner name
        JLabel winnerNameLabel = new JLabel(winnerName + " Wins!", SwingConstants.CENTER);
        winnerNameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        winnerNameLabel.setForeground(Color.BLACK);
        winnerNameLabel.setBounds(208, 160, 294, 68);
        layer.add(winnerNameLabel, Integer.valueOf(1)); 

        // play again button
        ImageIcon playAgainIcon  = new ImageIcon("Resources/Buttons/playagain_button.png");
        ImageIcon playAgainIconHover = new ImageIcon("Resources/Buttons/playagain_button_hovered.png");
        JButton playAgainButton = new JButton(playAgainIcon);
		
        // default and hover
        playAgainButton.setIcon(playAgainIcon);
        playAgainButton.setRolloverIcon(playAgainIconHover);
        playAgainButton.setBounds(235, 490, 379, 73);
        playAgainButton.setBorderPainted(false);
        playAgainButton.setContentAreaFilled(false);
        playAgainButton.setFocusPainted(false);
        layer.add(playAgainButton, Integer.valueOf(1));

        ImageIcon exitGameIcon  = new ImageIcon("Resources/Buttons/exitgame_button.png");
        ImageIcon exitGameIconHover = new ImageIcon("Resources/Buttons/exitgame_button_hovered.png");
        JButton exitGameButton = new JButton(exitGameIcon);
		
        // default and hover
        exitGameButton.setIcon(exitGameIcon);
        exitGameButton.setRolloverIcon(exitGameIconHover);
        exitGameButton.setBounds(235, 599, 379, 73);
        exitGameButton.setBorderPainted(false);
        exitGameButton.setContentAreaFilled(false);
        exitGameButton.setFocusPainted(false);
        layer.add(exitGameButton, Integer.valueOf(1));

        playAgainButton.addActionListener(e -> restartGame()); 
        exitGameButton.addActionListener(e -> returnHome());

        winnerFrame.setVisible(true);
    }

	/**Allows restarting of game by opening RuleFrame */
	public void restartGame() {
		RuleFrameGUI ruleFrame = new RuleFrameGUI(true);
        ruleFrame.setVisible(true); // Show the RuleFrame
		winnerFrame.setVisible(false);
	}

	/**Returns to home page of the game */
	public void returnHome(){
		HomePageGUI homepageGUI = new HomePageGUI(); 
		winnerFrame.setVisible(false);
	}
}
