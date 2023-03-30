package uk.co.neontribe.kimai.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

/**
 * Creates a singleton reference to our config and settings
 */
public class Settings {

    // STATIC FIELDS
    /**
     * The name of the config file
     */
    protected static final String CONFIG_FILENAME = "config.yml";
    /**
     * The path to the config file that holds the details to contact kimai
     */
    protected static final String CONFIG_DIR = ".config/neontribe/kimai-ui";

    /**
     * The singleton settings object
     */
    private static Settings kimaiSettings;

    /**
     * Get a singleton instance of our settings object.
     *
     * @return Settings
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException                   Thrown if we can't read from the file system
     */
    public static Settings getInstance() throws IOException, ConfigNotInitialisedException {
        // If we have a (static) singleton instantiated then exit early, return it.
        if (Settings.kimaiSettings != null) {
            return Settings.kimaiSettings;
        }

        // Get the user home save and create a File object that points to the config dire
        String home = System.getProperty("user.home");
        File settingsDir = new File(home, CONFIG_DIR);

        // If the config dir does not exist then create it or fail.
        if (!settingsDir.exists()) {
            if (!settingsDir.mkdirs()) {
                throw new RuntimeException("Cannot create config dir, " + settingsDir);
            }
        }

        // If the config file does not exist throw a ConfigNotInitialisedException error, this can be caught and handled
        File settingsFile = new File(settingsDir, CONFIG_FILENAME);
        if (!settingsFile.exists()) {
            throw new ConfigNotInitialisedException();
        }

        // Read in the YAML config file
        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(settingsFile.toPath());
        Map<String, Object> data = yaml.load(inputStream);

        // Create a new settings object and stash it in out static context
        Settings.kimaiSettings = new Settings();
        Settings.kimaiSettings.setKimaiUri((String) data.getOrDefault("kimaiUri", ""));
        Settings.kimaiSettings.setKimaiUsername((String) data.getOrDefault("kimaiUsername", ""));
        Settings.kimaiSettings.setKimaiPassword((String) data.getOrDefault("kimaiPassword", ""));

        // return the static instance of settings
        return Settings.kimaiSettings;
    }

    // Instance fields
    private String kimaiUri;
    private String kimaiUsername;
    private String kimaiPassword;

    public String getKimaiUri() {
        return kimaiUri;
    }

    public void setKimaiUri(String kimaiUri) {
        this.kimaiUri = kimaiUri;
    }

    public String getKimaiUsername() {
        return kimaiUsername;
    }

    public void setKimaiUsername(String kimaiUsername) {
        this.kimaiUsername = kimaiUsername;
    }

    public String getKimaiPassword() {
        return kimaiPassword;
    }

    public void setKimaiPassword(String kimaiPassword) {
        this.kimaiPassword = kimaiPassword;
    }

}
