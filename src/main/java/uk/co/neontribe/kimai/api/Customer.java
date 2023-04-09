package uk.co.neontribe.kimai.api;

import com.google.gson.reflect.TypeToken;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Customer extends Entity {
    public Customer(String name, int id) {
        super(name, id);
    }


    public static Customer[] getCustomers() throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/customers");
        String content = Entity.callApi(url);
        Gson gson = new Gson();
        TypeToken<List<Customer>> customerType = new TypeToken<List<Customer>>() {
        };
        List<Customer> data = gson.fromJson(content, customerType);
        Customer[] customers = new Customer[data.size()];
        for (int i = 0; i < data.size(); i++) {
            customers[i] = data.get(i);
        }
        return customers;
    }
}
