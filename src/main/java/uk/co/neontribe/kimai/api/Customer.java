package uk.co.neontribe.kimai.api;

import com.google.gson.reflect.TypeToken;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;

public class Customer {
    private String name;
    private int id;

    public Customer(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Customer[] getCustomers() throws ConfigNotInitialisedException, IOException {
        // get settings object
        Settings settings = Settings.getInstance();
        // call api
        URL url = new URL(settings.getKimaiUri() + "/api/customers");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-AUTH-USER", settings.getKimaiUsername());
        con.setRequestProperty("X-AUTH-TOKEN", settings.getKimaiPassword());
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        Gson gson = new Gson();
        TypeToken<List<Customer>> customerType = new TypeToken<List<Customer>>() {};
        List<Customer> data = gson.fromJson(content.toString(), customerType );

        Customer customers[]= new Customer[data.size()];

        for (int i=0; i<data.size(); i++){
            customers[i] = data.get(i);
        }

        // map response
        return customers;
    }

    public static void main(String [] args) throws ConfigNotInitialisedException, IOException {
        Customer.getCustomers();
    }
}
