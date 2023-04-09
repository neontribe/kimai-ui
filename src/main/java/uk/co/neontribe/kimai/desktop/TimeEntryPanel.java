package uk.co.neontribe.kimai.desktop;

import org.jdatepicker.JDatePanel;
import uk.co.neontribe.kimai.api.Activity;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.api.Project;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class TimeEntryPanel extends JPanel {

    private JComboBox customer;
    private JComboBox project;
    private JComboBox activity;

    public TimeEntryPanel() throws IOException, ConfigNotInitialisedException {
        this.setLayout(new BorderLayout());

        this.customer = new JComboBox<>(Customer.getCustomers());
        this.customer.setSelectedIndex(-1);
        this.project = new JComboBox<Project>();
        this.activity = new JComboBox<Activity>();

        JPanel activityEntry = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        activityEntry.add(new JLabel("Client"), c);
        c.gridy = 1;
        activityEntry.add(new JLabel("Project"), c);
        c.gridy = 2;
        activityEntry.add(new JLabel("Activity"), c);
        c.gridy = 3;
        activityEntry.add(new JLabel("Duration"), c);

        c.ipadx = 5;
        c.ipady = 5;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        activityEntry.add(this.customer, c);
        c.gridy = 1;
        activityEntry.add(this.project, c);
        c.gridy = 2;
        activityEntry.add(this.activity, c);
        c.gridy = 3;
        activityEntry.add(new JTextField("this field is long enough to str"), c);
        ;

        JPanel right = new JPanel(new BorderLayout(5, 5));
        JDatePanel datePicker = new JDatePanel();
        right.add(datePicker, BorderLayout.CENTER);

        this.add(activityEntry, BorderLayout.WEST);
        this.add(right, BorderLayout.EAST);

        customer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateActivityCombos();
            }
        });
        project.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateActivityCombos();
            }
        });

        this.customer.setSelectedIndex(1);
        updateActivityCombos();
    }

    public void updateActivityCombos() {
        Customer selectedCustomer = (Customer) customer.getSelectedItem();
        if (selectedCustomer != null) {
            try {
                Project[] projects = Project.getProjects(selectedCustomer.getId());
                DefaultComboBoxModel<Project> model = new DefaultComboBoxModel<>(projects);
                project.setModel(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Project selectedProject = (Project) this.project.getSelectedItem();
        if (selectedProject != null) {
            try {
                Activity[] activities = Activity.getActivities(selectedProject.getId());
                System.out.println(activities.length);
                DefaultComboBoxModel<Activity> model = new DefaultComboBoxModel<>(activities);
                activity.setModel(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Temp method, for deving
     */
    public static void main(String[] args) throws ConfigNotInitialisedException, IOException {
        JPanel config = new TimeEntryPanel();
        JFrame frame = new JFrame("TimeEntryPanel");
        frame.add(config);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
