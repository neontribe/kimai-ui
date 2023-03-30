package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class KimaiUiFrame extends JFrame {

    private Settings settings;

    public KimaiUiFrame() {
        super("Kimai Desktop UI");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));

        JButton btn = new JButton("Config");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConfigPanel.makeFrame(settings).setVisible(true);
            }
        });

        this.add(this.makeLabel("Top strip", Color.BLACK, Color.MAGENTA), BorderLayout.NORTH);
        this.add(this.makeLabel("Right column", Color.BLACK, Color.YELLOW), BorderLayout.EAST);
        this.add(this.makeLabel("Left column", Color.BLACK, Color.CYAN), BorderLayout.WEST);
        this.add(btn, BorderLayout.SOUTH);
        this.add(this.makeLabel("Greedy middle panel", Color.BLACK, Color.RED), BorderLayout.CENTER);

        this.pack();
        setLocationRelativeTo(null);

        // Read settings from the file system
        settings = null;
        try {
            settings = Settings.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Cannot continue, home space is not writable", e);
        } catch (ConfigNotInitialisedException e) {
            System.err.println("Config is not initialised.");
            this.settings = new Settings();
            ConfigPanel.makeFrame(this.settings).setVisible(true);
        }
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
