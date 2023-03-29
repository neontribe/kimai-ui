package uk.co.neontribe.kimai.config;

import java.io.File;

public class Settings {

    protected static final String CONFIG_FILENAME = "config.yml";
    protected static final String CONFIG_DIR = ".config/neontribe/kimai-ui";

    private static File settingsDir;
    private static File settingsFile;

    static {
        String home = System.getProperty("user.home");

        Settings.settingsDir = new File(home, CONFIG_DIR);
        if (!Settings.settingsDir.exists()) {
            Settings.settingsDir.mkdirs();
        }

        Settings.settingsFile = new File(settingsDir, CONFIG_FILENAME);
        if (!Settings.settingsFile.exists()) {
            try {
                throw new ConfigNotInitialisedException();
            } catch (ConfigNotInitialisedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public Settings() {
        throw new RuntimeException("Settings is a singleton. Don't instantiate!!!");
    }
}
