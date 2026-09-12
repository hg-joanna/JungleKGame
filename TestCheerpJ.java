import javax.swing.*;

public class TestCheerpJ {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CheerpJ Test");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel("CheerpJ works!"));
        frame.setVisible(true);
    }
}