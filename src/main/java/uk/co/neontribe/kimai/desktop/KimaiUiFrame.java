package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class KimaiUiFrame extends JFrame {

    public KimaiUiFrame() throws IOException {
        super("Kimai Desktop UI");

        // Read settings from the file system
        Settings settings = null;
        try {
            settings = Settings.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Cannot continue, home space is not writable", e);
        } catch (ConfigNotInitialisedException e) {
            System.err.println("Config is not initialised.");
            settings = new Settings();
            ConfigPanel.makeFrame(this, settings).setVisible(true);
        }

        this.add(new TimeEntryPanel());
        this.pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
