package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;

@AllArgsConstructor
public class User extends Entity {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public static User getCurrentUser() throws IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/users/me");
        String content = Entity.getApi(url);
        Gson gson = new Gson();
        TypeToken<User> userType = new TypeToken<User>() {
        };
        return gson.fromJson(content, userType);
    }

    public String toString() {
        return this.getName();
    }
}
