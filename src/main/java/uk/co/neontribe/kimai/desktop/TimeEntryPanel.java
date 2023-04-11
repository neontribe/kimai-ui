package uk.co.neontribe.kimai.desktop;

import org.jdatepicker.JDatePanel;
import sun.awt.XSettings;
import uk.co.neontribe.kimai.api.*;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeEntryPanel extends JPanel implements ActionListener {

    private final JList<Customer> customer;
    private final JList<Project> project;
    private final JList<Activity> activity;

    private final JTextArea notes;
    private final JDatePanel date;
    private final DurationPanel duration;

    private final StatusPanel statusPanel;

    public TimeEntryPanel() throws IOException, ConfigNotInitialisedException {
        this.setLayout(new GridBagLayout());

        this.customer = new JList<>(Customer.getCustomers());
        this.project = new JList<Project>();
        this.activity = new JList<Activity>();
        this.customer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.project.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.activity.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.duration = new DurationPanel();

        this.customer.setSelectedIndex(-1);

        GridBagConstraints c = new GridBagConstraints();

        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;

        // TODO Refactor these to use TitledBorder (See notes below)
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
        this.add(this.duration, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        notes = new JTextArea();
        JScrollPane notesPane = new JScrollPane(notes);
        notesPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Notes"));
        this.add(notesPane, c);

        date = new JDatePanel(new Date());
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 2;
        c.gridwidth = 1;
        this.add(date, c);

        JPanel actionPanel = new JPanel(new GridBagLayout());
        JButton save = new JButton("Log time");
        save.addActionListener(this);
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

        customer.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                updateProjectCombo();
            }
        });
        project.addListSelectionListener(new ListSelectionListener() {
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

    // adds desired JComponent to specific row/column in GridBagLayout
    private void addToGridBagLayout(int x, int y, GridBagConstraints c, JComponent component) {

        c.gridx = x;
        c.gridy = y;

        this.add(component, c);

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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // TODO Better error handling and better feedback
        try {
            int _project = ((Project) this.project.getSelectedValue()).getId();
            int _activity = ((Activity) this.activity.getSelectedValue()).getId();
            Date _begin = (Date) this.date.getModel().getValue();
            String _notes = this.notes.getText();
            String _duration = this.duration.getDuration();
            int user = User.getCurrentUser().getId();

            Pattern pattern = Pattern.compile("^\\d+:\\d{2}$");
            Matcher matcher = pattern.matcher(_duration);

            if (!matcher.find()) {
                throw new RuntimeException("Duration is not hh:mm");
            }

            String match = matcher.group();
            int hours = Integer.parseInt(match.substring(0, match.indexOf(":")));
            int minutes = (hours * 60) + Integer.parseInt(match.substring(match.indexOf(":") + 1));
            Calendar cal = Calendar.getInstance();
            cal.setTime(_begin);
            Date _end = new Date(cal.getTimeInMillis() + (60L * minutes * 1000));

            TimeSheet timesheet = new TimeSheet(
                    _notes,
                    _begin,
                    _end,
                    _project,
                    _activity,
                    user);
            TimeSheet.postTimeSheet(timesheet);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                this.statusPanel.setText(e.getClass() + ": TODO - better feedback");
            } else {
                this.statusPanel.setText(e.getMessage());
            }
        }
    }
}
