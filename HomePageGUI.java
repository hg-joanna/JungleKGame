import javax.swing.*;

public class HomePageGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Homepage Test");
        frame.setSize(1300, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new java.awt.Dimension(1300, 900));

        // Background
        ImageIcon bg = new ImageIcon(
            HomePageGUI.class.getResource("/Resources/homepage_bg.png")
        );

        JLabel backgroundLabel = new JLabel(bg);
        backgroundLabel.setBounds(0, 0, 1300, 900);

        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // Start button
        ImageIcon startImage = new ImageIcon(
            HomePageGUI.class.getResource("/Resources/start_button.png")
        );

        JButton startButton = new JButton(startImage);
        startButton.setBounds(565, 490, 191, 81);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);

        layeredPane.add(startButton, Integer.valueOf(1));

        startButton.addActionListener(new java.awt.event.ActionListener() {
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("START CLICKED");
            }
        });

        frame.setContentPane(layeredPane);
        frame.pack();
        frame.setSize(1300, 900);
        frame.setVisible(true);
    }
}