package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class KimaiUiFrame extends JFrame {

    public KimaiUiFrame() throws IOException {
        super("Kimai Desktop UI");

        this.add(new TimeEntryPanel());
        this.pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
