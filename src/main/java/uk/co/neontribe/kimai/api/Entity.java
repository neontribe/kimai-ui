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
import java.util.List;

public abstract class Entity {
    private String name;
    private int id;

    public Entity(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public static String callApi(URL url) throws ConfigNotInitialisedException, IOException {
        return Entity.callApi(url, "GET");
    }

    public static String callApi(URL url, String method) throws ConfigNotInitialisedException, IOException {
        // get settings object
        Settings settings = Settings.getInstance();
        // call api
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-AUTH-USER", settings.getKimaiUsername());
        con.setRequestProperty("X-AUTH-TOKEN", settings.getKimaiPassword());
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    public String toString() {
        return this.name + " [" + this.id + "]";

    }
}
