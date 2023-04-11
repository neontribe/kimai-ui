package uk.co.neontribe.kimai;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.desktop.KimaiUiFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    /**
     * The entry point. This will start our application.
     *
     * @param args String[]
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException Thrown if we can't read from the file system
     */
    public static void main(String[] args) throws ConfigNotInitialisedException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // Create a new application frame and show it.
        // UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        KimaiUiFrame frame = new KimaiUiFrame();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }
}