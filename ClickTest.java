import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class ClickTest extends JButton implements MouseListener {

    public ClickTest(javax.swing.Icon icon) {
        super(icon);
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("START CLICKED");
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}