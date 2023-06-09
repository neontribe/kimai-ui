package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@AllArgsConstructor
public class Project extends Entity {

    private int customer;


    public int getCustomer() {
        return customer;
    }

    public static Project[] getProjects(int id, Settings settings) throws IOException {
        URL url = new URL(settings.getKimaiUri() + "/api/projects");
        String content = Entity.getApi(url, settings);
        Gson gson = new Gson();
        TypeToken<List<Project>> projectType = new TypeToken<List<Project>>() {
        };
        List<Project> data = gson.fromJson(content, projectType);
        data.removeIf(p -> p.getCustomer() != id);
        Project[] projects = new Project[data.size()];
        for (int i = 0; i < data.size(); i++) {
            projects[i] = data.get(i);
        }
        return projects;
    }

    public String toString() {
        return this.getName();
    }
}
