package uk.co.neontribe.kimai.desktop;

import javax.swing.*;
import java.awt.*;

public class KimaiUiFrame extends JFrame {

    public KimaiUiFrame() {
        super("Kimai Desktop UI");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));

        this.add(this.makeLabel("Top strip", Color.BLACK, Color.MAGENTA), BorderLayout.NORTH);
        this.add(this.makeLabel("Right column", Color.BLACK, Color.YELLOW), BorderLayout.EAST);
        this.add(this.makeLabel("Left column", Color.BLACK, Color.CYAN), BorderLayout.WEST);
        this.add(this.makeLabel("Bottom strip", Color.BLACK, Color.LIGHT_GRAY), BorderLayout.SOUTH);
        this.add(this.makeLabel("Greedy middle panel", Color.BLACK, Color.RED), BorderLayout.CENTER);

        this.pack();
        setLocationRelativeTo(null);
    }

    protected final JComponent makeLabel(String text, Color bgColor, Color fgColor) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(fgColor);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));

        JPanel pan = new JPanel();
        pan.setBackground(bgColor);
        pan.add(lbl, BorderLayout.CENTER);

        return pan;
    }
}
