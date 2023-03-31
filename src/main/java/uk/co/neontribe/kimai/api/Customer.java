package uk.co.neontribe.kimai.api;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;

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
        ArrayList<Customer> customers = new ArrayList<Customer>();
        // get settings object
        Settings settings = Settings.getInstance();
        // call api
        URL url = new URL(settings.getKimaiUri() + "api/customers");
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
        System.out.println(content);

        // map response
        return null; // (Customer[]) customers.toArray();
    }

    public static void main(String [] args) throws ConfigNotInitialisedException, IOException {
        Customer.getCustomers();
    }
}
