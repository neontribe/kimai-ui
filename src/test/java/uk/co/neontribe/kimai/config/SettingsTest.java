package uk.co.neontribe.kimai.config;


import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertThrows;

class SettingsTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws FileNotFoundException {
        Properties props = System.getProperties();
        props.setProperty("user.home", System.getProperty("java.io.tmpdir"));

        File configDir = Settings.getConfigDir();
        if (!configDir.mkdirs()) {
            throw new RuntimeException("Cannot make test config dir");
        }
        PrintWriter pw = new PrintWriter(Settings.getConfigFile());
        pw.println("kimaiUri: http://example.com/\nkimaiUsername: testuser\nkimaiPassword: testpass\ncustomers: []");
        pw.flush();
        pw.close();
    }

    @org.junit.jupiter.api.Test
    void getInstance() throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();

    }

    @org.junit.jupiter.api.Test
    void getInstanceExceptions() {
        Properties props = System.getProperties();
        props.setProperty("user.home", System.getProperty("java.io.tmpdir") + "/no-config-here");

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
    void getKimaiUri() {
    }

    @org.junit.jupiter.api.Test
    void setKimaiUri() {
    }

    @org.junit.jupiter.api.Test
    void getKimaiUsername() {
    }

    @org.junit.jupiter.api.Test
    void setKimaiUsername() {
    }

    @org.junit.jupiter.api.Test
    void getKimaiPassword() {
    }

    @org.junit.jupiter.api.Test
    void setKimaiPassword() {
    }

    @org.junit.jupiter.api.Test
    void getCustomers() {
    }

    @org.junit.jupiter.api.Test
    void setCustomers() {
    }

    @org.junit.jupiter.api.Test
    void save() {
    }
}