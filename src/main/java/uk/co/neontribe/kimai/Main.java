package uk.co.neontribe.kimai;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;
import uk.co.neontribe.kimai.desktop.KimaiUiFrame;

import java.io.IOException;

public class Main {
    /**
     * The entry point. This will start our application.
     *
     * @param args String[]
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException Thrown if we can't read from the file system
     */
    public static void main(String[] args) throws ConfigNotInitialisedException, IOException {
        // Create a new application frame and show it.
        KimaiUiFrame frame = new KimaiUiFrame();
        frame.setVisible(true);

        // Read settings from the file system
        Settings settings = Settings.getInstance();

        // Print the values to the file system.
        System.out.println(settings.getKimaiUri());
        System.out.println(settings.getKimaiUsername());
        System.out.println(settings.getKimaiPassword());
    }
}