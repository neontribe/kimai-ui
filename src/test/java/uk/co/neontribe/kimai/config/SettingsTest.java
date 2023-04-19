package uk.co.neontribe.kimai.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

import static org.junit.Assert.assertThrows;

class SettingsTest {

    private static final String [] CUSTOMERS = {"1", "2", "3", "5", "7", "11", "13"};

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        Properties props = System.getProperties();
        props.setProperty("user.home", System.getProperty("java.io.tmpdir"));

        Settings.reset();

        File configDir = Settings.getConfigDir();
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw new RuntimeException("Cannot make test config dir");
        }
        if (Settings.getConfigFile().exists()) {
            Settings.getConfigFile().delete();
        }
        PrintWriter pw = new PrintWriter(Settings.getConfigFile());
        pw.println("kimaiUri: http://example.com/\nkimaiUsername: testuser\nkimaiPassword: testpass\ncustomers: []");
        pw.flush();
        pw.close();
    }

    @AfterEach
    void tearDown() {
        if (Settings.getConfigFile().exists()) {
            Settings.getConfigFile().delete();
        }
    }

    @org.junit.jupiter.api.Test
    void getInstance() throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();
        Assertions.assertInstanceOf(Settings.class, settings);
    }

    @org.junit.jupiter.api.Test
    void save() throws IOException {
        Settings settings = Settings.getInstance();
        settings.setKimaiUri("http://new.url.com");
        settings.setKimaiUsername("saveuser");
        settings.setKimaiPassword("savepass");
        settings.setCustomers(CUSTOMERS);

        Settings.saveAndReset(settings);

        Settings _settings = Settings.getInstance();
        Assertions.assertEquals("http://new.url.com", _settings.getKimaiUri());
        Assertions.assertEquals("saveuser", _settings.getKimaiUsername());
        Assertions.assertEquals("savepass", _settings.getKimaiPassword());
        Assertions.assertArrayEquals(CUSTOMERS, _settings.getCustomers());
    }

    private String randomName() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    @org.junit.jupiter.api.Test
    void getInstanceExceptions() {
        Properties props = System.getProperties();
        props.setProperty("user.home", System.getProperty("java.io.tmpdir") + "/" + randomName());

        Exception configNotInitialisedException = assertThrows(ConfigNotInitialisedException.class, () -> {
            Settings settings = Settings.getInstance();
        });
        Assertions.assertEquals("Config not initialised", configNotInitialisedException.getMessage());

        props.setProperty("user.home", "/no/such/file");
        Exception runtimeException = assertThrows(RuntimeException.class, () -> {
            Settings settings = Settings.getInstance();
        });
        Assertions.assertEquals("Cannot create config dir, /no/such/file/.config/neontribe/kimai-ui", runtimeException.getMessage());
    }

    @org.junit.jupiter.api.Test
    void getConfigDir() {
        Assertions.assertEquals(new File(System.getProperty("java.io.tmpdir"), ".config/neontribe/kimai-ui"), Settings.getConfigDir());
    }

    @org.junit.jupiter.api.Test
    void getConfigFile() {
        Assertions.assertEquals(new File(System.getProperty("java.io.tmpdir"), ".config/neontribe/kimai-ui/config.yml"), Settings.getConfigFile());
    }

    @org.junit.jupiter.api.Test
    void getKimaiUri() throws IOException {
        Assertions.assertEquals("http://example.com/", Settings.getInstance().getKimaiUri());
    }

    @org.junit.jupiter.api.Test
    void getKimaiUsername() throws IOException {
        Assertions.assertEquals("testuser", Settings.getInstance().getKimaiUsername());
    }

    @org.junit.jupiter.api.Test
    void getKimaiPassword() throws IOException {
        Assertions.assertEquals("testpass", Settings.getInstance().getKimaiPassword());
    }

    @org.junit.jupiter.api.Test
    void getCustomers() throws IOException {
        String[] customers = Settings.getInstance().getCustomers();
        Assertions.assertEquals(0, customers.length);
    }

    @org.junit.jupiter.api.Test
    void setKimaiUri() throws IOException {
        Settings.getInstance().setKimaiUri("http://foo.bar.com");
        Assertions.assertEquals("http://foo.bar.com", Settings.getInstance().getKimaiUri());
    }

    @org.junit.jupiter.api.Test
    void setKimaiUsername() throws IOException {
        Settings.getInstance().setKimaiUri("newusername");
        Assertions.assertEquals("newusername", Settings.getInstance().getKimaiUri());
    }

    @org.junit.jupiter.api.Test
    void setKimaiPassword() throws IOException {
        Settings.getInstance().setKimaiUri("shhh-secret-password");
        Assertions.assertEquals("shhh-secret-password", Settings.getInstance().getKimaiUri());
    }

    @org.junit.jupiter.api.Test
    void setCustomers() throws IOException {
        Settings settings = Settings.getInstance();
        settings.setCustomers(CUSTOMERS);
        String[] _customers = settings.getCustomers();
        Assertions.assertEquals(7, CUSTOMERS.length);
        Assertions.assertArrayEquals(CUSTOMERS, _customers);
    }
}