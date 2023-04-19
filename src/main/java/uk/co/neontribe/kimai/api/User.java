package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;

@AllArgsConstructor
public class User extends Entity {

    public static User getCurrentUser(Settings settings) throws IOException {
        URL url = new URL(settings.getKimaiUri() + "/api/users/me");
        String content = Entity.getApi(url, settings);
        Gson gson = new Gson();
        TypeToken<User> userType = new TypeToken<User>() {
        };
        return gson.fromJson(content, userType);
    }

    public String toString() {
        return this.getName();
    }
}
