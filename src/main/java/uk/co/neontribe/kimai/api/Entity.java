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
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
        return Entity.callApi(url, "GET", null);
    }

    public static String callApi(URL url, List<Map.Entry<String, String>> parameters) throws ConfigNotInitialisedException, IOException {
        return Entity.callApi(url, "GET", parameters);
    }

    public static String callApi(URL url, String method, List<Map.Entry<String, String>> parameters) throws ConfigNotInitialisedException, IOException {
        if (parameters != null && parameters.size()>0) {
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> parameter : parameters) {
                query.append(
                        String.format(
                                "%s=%s&",
                                URLEncoder.encode(parameter.getKey(), "UTF-8"),
                                URLEncoder.encode(parameter.getValue(), "UTF-8"))
                );
            }
            url = new URL(url + "?" + query);
        }
        System.out.println(url);

        Settings settings = Settings.getInstance();
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
        return this.name;
    }
}
