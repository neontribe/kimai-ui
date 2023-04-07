package uk.co.neontribe.kimai.desktop;

import org.jdatepicker.JDatePanel;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TimeEntryPanel extends JPanel {

    private JComboBox customer;
    private JComboBox project;
    private JComboBox activity;

    public TimeEntryPanel() {
        this.setLayout(new BorderLayout());

        this.customer = makeCustomerCobmo();

        JPanel activityEntry = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        activityEntry.add(new JLabel("Client"), c);
        c.gridy = 1;
        activityEntry.add(new JLabel("Activity"), c);
        c.gridy = 2;
        activityEntry.add(new JLabel("Project"), c);
        c.gridy = 3;
        activityEntry.add(new JLabel("Duration"), c);

        c.ipadx = 5;
        c.ipady =5;
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        activityEntry.add(customer, c);
        c.gridy = 1;
        activityEntry.add(new JComboBox<String>(), c);
        c.gridy = 2;
        activityEntry.add(new JComboBox<String>(), c);
        c.gridy = 3;
        activityEntry.add(new JTextField("this field is long enough to str"), c);;

        JPanel right = new JPanel(new BorderLayout(5, 5));

        JDatePanel datePicker = new JDatePanel();
        right.add(new JLabel("Date"), BorderLayout.NORTH);
        right.add(datePicker, BorderLayout.CENTER);

        this.add(activityEntry, BorderLayout.WEST);
        this.add(right, BorderLayout.EAST);
    }

    private JComboBox makeCustomerCobmo() {
        try {
            Customer[] customers = Customer.getCustomers();
            JComboBox<Customer> customer = new JComboBox<>(customers);
            return  customer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Temp method, for deving
     */
    public static void main(String [] args) {
        JPanel config = new TimeEntryPanel();
        JFrame frame = new JFrame("TimeEntryPanel");
        frame.add(config);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
