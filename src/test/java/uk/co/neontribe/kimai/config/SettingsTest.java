package uk.co.neontribe.kimai.config;


<<<<<<< Updated upstream
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
=======
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
>>>>>>> Stashed changes

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
<<<<<<< Updated upstream
import java.util.Set;
=======
import java.util.Random;
>>>>>>> Stashed changes

import static org.junit.Assert.assertThrows;

//@TestMethodOrder(MethodOrderer.MethodName.class)
class SettingsTest {

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
    }

    @org.junit.jupiter.api.Test
    void save() {
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
        String[] customers = {"1", "2", "3", "5", "7", "11", "13"};
        settings.setCustomers(customers);
        String[] _customers = settings.getCustomers();
        Assertions.assertEquals(7, customers.length);
        Assertions.assertArrayEquals(customers, _customers);
    }
}