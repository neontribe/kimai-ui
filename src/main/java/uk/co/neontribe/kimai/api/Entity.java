package uk.co.neontribe.kimai.api;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public abstract class Entity {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static HttpURLConnection makeHttpConnection(URL url, Settings settings) throws IOException {
        System.out.println("makeHttpConnection: " + settings.getKimaiUsername() + ", " + settings.getKimaiPassword() + ", " + ", " + settings.getKimaiUri());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-AUTH-USER", settings.getKimaiUsername());
        con.setRequestProperty("X-AUTH-TOKEN", settings.getKimaiPassword());
        con.setRequestProperty("Accept", "application/json");
        con.setDoInput(true);
        return con;
    }

    public static String getApi(URL url, Settings settings) throws IOException {
        return Entity.getApi(url, settings, null);
    }

    public static String getApi(URL url, Settings settings, List<Map.Entry<String, String>> parameters) throws IOException {
        if (parameters != null && parameters.size() > 0) {
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> parameter : parameters) {
                query.append(String.format("%s=%s&", URLEncoder.encode(parameter.getKey(), "UTF-8"), URLEncoder.encode(parameter.getValue(), "UTF-8")));
            }
            url = new URL(url + "?" + query);
        }
        HttpURLConnection con = makeHttpConnection(url, settings);

        return callApi(con);
    }

    public static String postApi(URL url, Settings settings, String body) throws IOException {
        HttpURLConnection con = makeHttpConnection(url, settings);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        OutputStream os = con.getOutputStream();
        byte[] input = body.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        return callApi(con);
    }

    private static String callApi(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}
