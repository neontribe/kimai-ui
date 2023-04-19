package uk.co.neontribe.kimai.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.yaml.snakeyaml.Yaml;
import uk.co.neontribe.kimai.api.Activity;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.api.LastAccessed;
import uk.co.neontribe.kimai.api.Project;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

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

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
        CONFIG_DIR = dotenv.get("CONFIG_DIR", ".config/neontribe/kimai-ui");
        CONFIG_FILENAME = dotenv.get("CONFIG_FILENAME", "config.yml");
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
    private String[] customers = {};
    private int fontSize = 12;

    private boolean dirty = false;

    private LastAccessed lastAccessed = new LastAccessed();

    public String getKimaiUri() {
        return kimaiUri;
    }

    public String getKimaiUsername() {
        return kimaiUsername;
    }

    public String getKimaiPassword() {
        return kimaiPassword;
    }

    public String[] getCustomers() {
        return customers;
    }

    public LastAccessed getLastAccessed() {
        return lastAccessed;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public void setKimaiUri(String kimaiUri) {
        if (!this.kimaiUri.equals(kimaiUri)) {
            this.kimaiUri = kimaiUri;
            this.dirty = true;
        }
    }

    public void setKimaiUsername(String kimaiUsername) {
        if (!this.kimaiUsername.equals(kimaiUsername)) {
            this.kimaiUsername = kimaiUsername;
            this.dirty = true;
        }
    }

    public void setKimaiPassword(String kimaiPassword) {
        if (!this.kimaiUsername.equals(kimaiPassword)) {
            if (kimaiPassword.length() > 0) {
                this.kimaiPassword = kimaiPassword;
                this.dirty = true;
            }
        }
    }

    public void setCustomers(String[] customers) {
        if (!Arrays.equals(this.customers, customers)) {
            this.customers = customers;
            this.dirty = true;
        }
    }

    public void setLastAccessedCustomer(Customer customer) {
        if (this.lastAccessed.getCustomer() == customer.getId()) {
            this.lastAccessed.setCustomer(customer.getId());
            this.dirty = true;
        }
    }

    public void setLastAccessedProject(Project project) {
        if (this.lastAccessed.getProject() == project.getId()) {
            this.lastAccessed.setProject(project.getId());
            this.dirty = true;
        }
    }

    public void setLastAccessedActivity(Activity activity) {
        if (this.lastAccessed.getActivity() == activity.getId()) {
            this.lastAccessed.setActivity(activity.getId());
            this.dirty = true;
        }
    }

//    public void setFontSize(int fontSize) {
//        this.fontSize = fontSize;
//    }

    public boolean isDirty() {
        return this.dirty;
    }

    /**
     * Get a singleton instance of our settings object.
     *
     * @return Settings
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException                   Thrown if we can't read from the file system
     */
    public static Settings load() throws IOException, ConfigNotInitialisedException {
        Settings settings;

        File settingsDir = Settings.getConfigDir();
        // If the config dir does not exist then create it or fail.
        if (!settingsDir.exists()) {
            if (!settingsDir.mkdirs()) {
                throw new RuntimeException("Cannot create config dir, " + settingsDir);
            }
        }

        // If the config file does not exist throw a ConfigNotInitialisedException error, this can be caught and handled
        File settingsFile = Settings.getConfigFile();
        if (!settingsFile.exists()) {
            throw new ConfigNotInitialisedException("Config not initialised");
        }

        // Read in the YAML config file
        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(settingsFile.toPath());
        System.out.println("Reading settings from " + settingsFile.toPath());
        Map<String, Object> data = yaml.load(inputStream);

        if (data == null) {
            settings = new Settings();
            return settings;
        }

        // Create a new settings object and stash it in out static context
        settings = new Settings();

        settings.kimaiUri = ((String) data.getOrDefault("kimaiUri", ""));
        settings.kimaiUsername = ((String) data.getOrDefault("kimaiUsername", ""));
        settings.kimaiPassword = ((String) data.getOrDefault("kimaiPassword", ""));

        settings.lastAccessed.setCustomer((Integer) data.getOrDefault("last-accessed-customer", -1));
        settings.lastAccessed.setProject((Integer) data.getOrDefault("last-accessed-project", -1));
        settings.lastAccessed.setActivity((Integer) data.getOrDefault("last-accessed-activity", -1));

        settings.fontSize = (Integer) data.getOrDefault("font-size", 48);

        Object rawCustomers = data.get("customers");
        if (rawCustomers instanceof ArrayList<?>) {
            ArrayList<String> customers = (ArrayList<String>) rawCustomers;
            settings.customers = (customers.toArray(new String[0]));
        }

        return settings;
    }

    public void save() {
        System.out.println("Saving: " + this.dirty + ", " + this.getKimaiUri() + ", " + this.getKimaiUsername());
        if (this.dirty) {
            Map<String, Object> kimai = new HashMap<String, Object>();
            kimai.put("kimaiUri", this.getKimaiUri());
            kimai.put("kimaiUsername", this.getKimaiUsername());
            kimai.put("kimaiPassword", this.getKimaiPassword());
            kimai.put("customers", this.getCustomers());

            // TODO refactor this into a nested array. I don't understand YAML serializer in Java well enough
            kimai.put("last-accessed-customer", this.getLastAccessed().getCustomer());
            kimai.put("last-accessed-project", this.getLastAccessed().getProject());
            kimai.put("last-accessed-activity", this.getLastAccessed().getActivity());

            try {
                Yaml yaml = new Yaml();
                PrintWriter writer = new PrintWriter(Settings.getConfigFile());
                yaml.dump(kimai, writer);
                writer.flush();
                writer.close();
                this.dirty = false;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
