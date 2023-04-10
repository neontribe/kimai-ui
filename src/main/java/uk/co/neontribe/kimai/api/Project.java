package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project extends Entity {

    private int customer;


    public Project(String name, int id, int customer) {
        super(name, id);
        this.customer = customer;
    }

    public int getCustomer() {
        return customer;
    }

    public static Project[] getProjects(int id) throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/projects");
        String content = Entity.callApi(url);
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
}
