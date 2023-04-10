package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class User extends Entity {
    public User(String name, int id) {
        super(name, id);
    }

    public static User getCurrentUser() throws IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/users/me");
        String content = Entity.callApi(url);
        Gson gson = new Gson();
        TypeToken<List<User>> userType = new TypeToken<List<User>>() {
        };
        List<User> data = gson.fromJson(content, userType);
        if (data.size() == 0) {
            throw new RuntimeException("Unreachable?");
        }
        User user = data.get(0);
        return user;
    }
}
