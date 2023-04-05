package uk.co.neontribe.kimai.desktop;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeEntryPanel extends JPanel {

    private JComboBox<String> list;
    private JComboBox<String> clients;
    private JComboBox<String> project;
    private JComboBox<String> activity;

    public TimeEntryPanel() throws ConfigNotInitialisedException, IOException {

        Customer[] customers = Customer.getCustomers();

        ArrayList<String> listArray = new ArrayList<>(customers.length);
        for (int i = 0; i < customers.length; i++) {
            listArray.add(customers[i].getName());
        }

        this.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(0, 5, 5, 5));
        JPanel customersPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel projectPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel activityPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel datePickerPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel durationPanel = new JPanel(new GridLayout(0, 1, 5, 5));


        customersPanel.add(new JLabel("Client"));
        customersPanel.add(new JComboBox(listArray.toArray()));
        center.add(customersPanel, center);

        projectPanel.add(new JLabel("Client"));
        projectPanel.add(new JComboBox<String>());
        center.add(projectPanel, center);

        activityPanel.add(new JLabel("Activity"));
        activityPanel.add(new JComboBox<String>());
        center.add(activityPanel, center);

        datePickerPanel.add(new JLabel("Date"));
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        datePickerPanel.add(datePicker);
        center.add(datePickerPanel, center);

        durationPanel.add(new JLabel(("Duration")));
        JFormattedTextField duration = new JFormattedTextField();
        duration.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat(
                "H'h' mm'm'"))));
        duration.setValue(Calendar.getInstance().getTime());

        durationPanel.add(duration);
        center.add(durationPanel, center);

        this.add(center, BorderLayout.CENTER);


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
