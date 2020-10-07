import javax.swing.*;
import java.awt.*;

public class Tile extends JLabel {
    public Tile(int num) {
        setText(String.valueOf(num));

        // Style
        setFont(new Font(null, Font.PLAIN, 70));
        setForeground(Color.LIGHT_GRAY);
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}
