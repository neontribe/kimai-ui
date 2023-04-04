package uk.co.neontribe.kimai.desktop;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TimeEntryPanel extends JPanel {

    private JComboBox<String> list;
    private JComboBox<String> clients;
    private JComboBox<String> project;
    private JComboBox<String> activity;

    public TimeEntryPanel() throws ConfigNotInitialisedException, IOException {

        Customer[] customers = Customer.getCustomers();

        ArrayList<String> listArray = new ArrayList<>(customers.length);
        for (int i=0; i<customers.length; i++){
            listArray.add( customers[i].getName());
        }

        this.setLayout(new BorderLayout());

        JPanel left = new JPanel(new GridLayout(0, 2, 5, 5));
        left.add(new JLabel("Client"));
        left.add(new JComboBox(listArray.toArray()));
        left.add(new JLabel("Project"));
        left.add(new JComboBox<String>());
        left.add(new JLabel("Activity"));
        left.add(new JComboBox<String>());

        this.add(left, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(5, 5));

        JPanel datePicker = new JPanel(new GridLayout(3,1, 2, 2));
        datePicker.add(this.clients = new JComboBox<String>());
        datePicker.add(this.project = new JComboBox<String>());
        datePicker.add(this.activity = new JComboBox<String>());

        right.add(new JLabel("Date"), BorderLayout.NORTH);
        right.add(datePicker, BorderLayout.CENTER);

        JPanel duration = new JPanel(new BorderLayout());
        duration.add(new JLabel("Duration"), BorderLayout.WEST);
        duration.add(new JTextField(), BorderLayout.CENTER);

        right.add(duration, BorderLayout.SOUTH);

        this.add(right, BorderLayout.EAST);
    }

    /**
     * Temp method, for deving
     */
    public static void main(String [] args) throws ConfigNotInitialisedException, IOException {
        JPanel config = new TimeEntryPanel();
        JFrame frame = new JFrame("TimeEntryPanel");
        frame.add(config);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
