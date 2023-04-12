package uk.co.neontribe.kimai.config;

public class ConfigNotInitialisedException extends RuntimeException {
    public ConfigNotInitialisedException(String configNotInitialised) {
        super(configNotInitialised);
    }
}
