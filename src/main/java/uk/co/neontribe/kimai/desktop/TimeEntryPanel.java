package uk.co.neontribe.kimai.desktop;

import org.jdatepicker.JDatePanel;
import sun.awt.XSettings;
import uk.co.neontribe.kimai.api.Activity;
import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.api.Project;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class TimeEntryPanel extends JPanel {

    private JList customer;
    private JList project;
    private JList activity;

    private JTextArea notes;

    private StatusPanel statusPanel;

    public TimeEntryPanel() throws IOException, ConfigNotInitialisedException {
        this.setLayout(new GridBagLayout());

        this.customer = new JList<>(Customer.getCustomers());
        this.project = new JList<Project>();
        this.activity = new JList<Activity>();
        this.customer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.project.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.activity.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.customer.setSelectedIndex(-1);

        GridBagConstraints c = new GridBagConstraints();

        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;

        c.gridx = 0;
        this.add(new JLabel("Client"), c);
        c.gridx = 1;
        this.add(new JLabel("Project"), c);
        c.gridx = 2;
        this.add(new JLabel("Activity"), c);

        c.gridy = 1;

        c.gridx = 0;
        this.add(new JScrollPane(this.customer), c);
        c.gridx = 1;
        this.add(new JScrollPane(this.project), c);
        c.gridx = 2;
        this.add(new JScrollPane(this.activity), c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        this.add(new DurationPanel(), c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        notes = new JTextArea();
        JScrollPane notesPane = new JScrollPane(notes);
        notesPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Notes"));
        this.add(notesPane, c);

        JDatePanel datePicker = new JDatePanel();
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 2;
        c.gridwidth = 1;
        this.add(datePicker, c);

        JPanel actionPanel = new JPanel(new GridBagLayout());
        JButton save = new JButton("Log time");
        actionPanel.add(save);
        c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 3;
        this.add(actionPanel, c);

        Settings settings = Settings.getInstance();
        statusPanel = new StatusPanel();
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.gridwidth = 3;
        this.add(statusPanel, c);

        customer.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                updateProjectCombo();
            }
        });
        project.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                updateActivityCombo();
            }
        });

        this.customer.setSelectedIndex(1);
        updateProjectCombo();
    }

    public void updateProjectCombo() {
        Customer selectedCustomer = (Customer) customer.getSelectedValue();
        if (selectedCustomer != null) {
            try {
                Project[] projects = Project.getProjects(selectedCustomer.getId());
                DefaultComboBoxModel<Project> model = new DefaultComboBoxModel<>(projects);
                project.setModel(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateActivityCombo() {
        Project selectedProject = (Project) this.project.getSelectedValue();
        if (selectedProject != null) {
            try {
                Activity[] activities = Activity.getActivities(selectedProject.getId());
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
        // frame.setSize(1024,768);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
