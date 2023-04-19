package uk.co.neontribe.kimai.config;

public class ConfigNotInitialisedException extends Exception {
    public ConfigNotInitialisedException(String configNotInitialised) {
        super(configNotInitialised);
    }
}
