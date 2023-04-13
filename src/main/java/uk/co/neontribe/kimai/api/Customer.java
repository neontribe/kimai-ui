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
public class Customer extends Entity {

    public static Customer[] getCustomers() throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/customers");
        String content = Entity.getApi(url);
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

    public String toString() {
        return this.getName();
    }
}
