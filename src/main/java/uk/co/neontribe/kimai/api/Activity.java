package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@AllArgsConstructor
public class Activity extends Entity {

    private int project;


    public int getProject() {
        return project;
    }

    public static Activity[] getActivities(int id, Settings settings) throws IOException {
        URL url = new URL(settings.getKimaiUri() + "/api/activities");
        String content = Entity.getApi(url, settings);
        Gson gson = new Gson();
        TypeToken<List<Activity>> activityType = new TypeToken<List<Activity>>() {
        };
        List<Activity> data = gson.fromJson(content, activityType);
        data.removeIf(p -> p.getProject() != id && p.getProject() != 0);
        Activity[] activities = new Activity[data.size()];
        for (int i = 0; i < data.size(); i++) {
            activities[i] = data.get(i);
        }
        return activities;
    }

    public String toString() {
        return this.getName();
    }
}
