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

    public KimaiUiFrame() throws IOException {
        super("Kimai Desktop UI");

        // Read settings from the file system
        settings = null;
        try {
            settings = Settings.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Cannot continue, home space is not writable", e);
        } catch (ConfigNotInitialisedException e) {
            System.err.println("Config is not initialised.");
            this.settings = new Settings();
            ConfigPanel.makeFrame(this, this.settings).setVisible(true);
        }

        this.add(new TimeEntryPanel());
        this.pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
