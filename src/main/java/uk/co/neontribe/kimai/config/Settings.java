package uk.co.neontribe.kimai.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a singleton reference to our config and settings
 */
public class Settings {

    // STATIC FIELDS
    /**
     * The name of the config file
     */
    protected static String CONFIG_FILENAME;
    /**
     * The path to the config file that holds the details to contact kimai
     */
    protected static String CONFIG_DIR;

    /**
     * The singleton settings object
     */
    private static Settings kimaiSettings;

    static {

        Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
        CONFIG_DIR = dotenv.get("CONFIG_DIR", ".config/neontribe/kimai-ui");
        CONFIG_FILENAME = dotenv.get("CONFIG_FILENAME", "config.yml");
    }

    public static void reset() {
        Settings.kimaiSettings = null;

    }

    /**
     * Get a singleton instance of our settings object.
     *
     * @return Settings
     * @throws ConfigNotInitialisedException Thrown when config location cannot be
     *                                       created.
     * @throws IOException                   Thrown if we can't read from the file
     *                                       system
     */
    public static Settings getInstance() throws IOException, ConfigNotInitialisedException {
        // If we have a (static) singleton instantiated then exit early, return it.
        if (Settings.kimaiSettings != null) {
            return Settings.kimaiSettings;
        }

        File settingsDir = Settings.getConfigDir();
        // If the config dir does not exist then create it or fail.
        if (!settingsDir.exists()) {
            if (!settingsDir.mkdirs()) {
                throw new RuntimeException("Cannot create config dir, " + settingsDir);
            }
        }

        // If the config file does not exist throw a ConfigNotInitialisedException
        // error, this can be caught and handled
        File settingsFile = Settings.getConfigFile();
        if (!settingsFile.exists()) {
            throw new ConfigNotInitialisedException("Config not initialised");
        }

        // Read in the YAML config file
        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(settingsFile.toPath());
        Map<String, Object> data = yaml.load(inputStream);

        if (data == null) {
            Settings.kimaiSettings = new Settings();
            return Settings.kimaiSettings;
        }

        // Create a new settings object and stash it in out static context
        Settings.kimaiSettings = new Settings();

        Settings.kimaiSettings.setKimaiUri((String) data.getOrDefault("kimaiUri", ""));
        Settings.kimaiSettings.setKimaiUsername((String) data.getOrDefault("kimaiUsername", ""));
        Settings.kimaiSettings.setKimaiPassword((String) data.getOrDefault("kimaiPassword", ""));

        Object rawCustomers = data.get("customers");
        if (rawCustomers instanceof ArrayList<?>) {
            ArrayList<String> customers = (ArrayList<String>) rawCustomers;
            Settings.kimaiSettings.setCustomers(customers.toArray(new String[0]));
        }

        // return the static instance of settings
        return Settings.kimaiSettings;
    }

    /**
     * Get the user home and create a File object that points to the config dir
     *
     * @return File The config dir
     */
    public static File getConfigDir() {
        String home = System.getProperty("user.home");
        return new File(home, CONFIG_DIR);
    }

    /**
     * Get the user home and create a File object that points to the config dir
     *
     * @return File The config dir
     */
    public static File getConfigFile() {
        return new File(Settings.getConfigDir(), CONFIG_FILENAME);
    }

    // Instance fields
    private String kimaiUri = "";
    private String kimaiUsername = "";
    private String kimaiPassword = "";
    private Object kimaiFilteredCustomers = null;
    private String[] customers = {};

    public String getKimaiUri() {
        return kimaiUri;
    }

    public void setKimaiUri(String kimaiUri) {this.kimaiUri = kimaiUri;}

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

    public void setFilteredCustomers(Object kimaiFilteredCustomers) {

        this.kimaiFilteredCustomers = kimaiFilteredCustomers;
        System.out.println("this has run");
        System.out.println(this.kimaiFilteredCustomers);
    }

    public Object getFilteredCustomers() {
        System.out.println(kimaiFilteredCustomers);
        return kimaiFilteredCustomers;
    }

    public String[] getCustomers() {
        return customers;
    }

    public void setCustomers(String[] customers) {
        this.customers = customers;
    }

    public static boolean save(Settings settings) throws FileNotFoundException, SecurityException {
        Map<String, Object> kimai = new HashMap<String, Object>();
        kimai.put("kimaiUri", settings.getKimaiUri());
        kimai.put("kimaiUsername", settings.getKimaiUsername());
        kimai.put("kimaiPassword", settings.getKimaiPassword());
        kimai.put("filteredCustomers", settings.getFilteredCustomers());

        kimai.put("customers", settings.getCustomers());

        Yaml yaml = new Yaml();
        PrintWriter writer = new PrintWriter(Settings.getConfigFile());
        yaml.dump(kimai, writer);
        writer.flush();
        writer.close();

        reset();

        return true;
    }
}
