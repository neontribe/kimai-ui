package uk.co.neontribe.kimai.desktop;

import org.jdatepicker.JDatePanel;


import uk.co.neontribe.kimai.api.*;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;

import javax.swing.border.EmptyBorder;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeEntryPanel extends JPanel implements ActionListener {

    Component topLevelFrame;

    private final JList<Customer> customer;
    private final JList<Project> project;
    private final JList<Activity> activity;

    private final JTextArea notes;
    private final JDatePanel date;
    private final DurationPanel duration;

    private final StatusPanel statusPanel;

    public TimeEntryPanel() throws IOException, ConfigNotInitialisedException {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridBagLayout());

        this.customer = new JList<>(Customer.getCustomers());
        this.project = new JList<Project>();
        this.activity = new JList<Activity>();
        this.customer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.project.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.activity.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.duration = new DurationPanel();

        this.customer.setFixedCellWidth(240);
        this.project.setFixedCellWidth(240);
        this.activity.setFixedCellWidth(240);

        this.customer.setVisibleRowCount(13);
        this.project.setVisibleRowCount(13);
        this.activity.setVisibleRowCount(13);

        this.customer.setSelectedIndex(-1);

        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.ipady = 20;

        this.add(new Header("Timetracking sheet"), c);

        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1000;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 1;

        c.gridx = 0;
        this.add(addBorder("Client", new JScrollPane(this.customer)), c);
        c.gridx = 1;
        this.add(addBorder("Project", new JScrollPane(this.project)), c);

        c.gridx = 2;
        this.add(addBorder("Activity", new JScrollPane(this.activity)), c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weighty = 1;
        this.add(this.duration, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weighty = 2;
        notes = new JTextArea();
        JScrollPane notesPane = new JScrollPane(notes);

        this.add(addBorder("Notes", notesPane), c);

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
        c.weighty = 1;
        this.add(actionPanel, c);

        statusPanel = new StatusPanel();
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.gridwidth = 3;
        this.add(statusPanel, c);

        customer.addListSelectionListener(listSelectionEvent -> updateProjectCombo());
        project.addListSelectionListener(listSelectionEvent -> updateActivityCombo());
        activity.addListSelectionListener(listSelectionEvent -> saveLastAccessedActivity());

        try {
            // If we have a last selected, then try and find it, and set it
            int customerId = Settings.getInstance().getLastAccessed().getCustomer();
            if (customerId >= 0) {
                ListModel<Customer> model = customer.getModel();
                for (int i=0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).getId() == customerId) {
                        this.customer.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }

        updateProjectCombo();
    }

    private void _setCursor(Cursor cursor) {
        if (topLevelFrame == null) {
            this.topLevelFrame = ConfigPanel.getParentFrame(this);
        }
        if (topLevelFrame != null) {
            topLevelFrame.setCursor(cursor);
        }
    }

    private void updateProjectCombo() {
        Settings settings;
        try {
            settings = Settings.getInstance();
            this._setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Customer selectedCustomer = customer.getSelectedValue();
            if (selectedCustomer != null) {
                settings.setLastAccessedCustomer(selectedCustomer);
                Project[] projects = Project.getProjects(selectedCustomer.getId());
                DefaultComboBoxModel<Project> model = new DefaultComboBoxModel<>(projects);
                project.setModel(model);
            }
            // If we have a last selected, then try and find it, and set it
            int projectId = Settings.getInstance().getLastAccessed().getProject();
            if (projectId >= 0) {
                ListModel<Project> model = project.getModel();
                for (int i=0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).getId() == projectId) {
                        this.project.setSelectedIndex(i);
                        break;
                    }
                }
            }
            Settings.save(settings);
        } catch (IOException e) {
            this._setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            throw new RuntimeException(e);
        }
        this._setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void updateActivityCombo() {
        Settings settings;
        try {
            settings = Settings.getInstance();
            this._setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Project selectedProject = project.getSelectedValue();
            if (selectedProject != null) {
                settings.setLastAccessedProject(selectedProject);
                Activity[] activities = Activity.getActivities(selectedProject.getId());
                DefaultComboBoxModel<Activity> model = new DefaultComboBoxModel<>(activities);
                activity.setModel(model);
            }
            // If we have a last selected, then try and find it, and set it
            int activityId = Settings.getInstance().getLastAccessed().getActivity();
            if (activityId >= 0) {
                ListModel<Activity> model = activity.getModel();
                for (int i=0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).getId() == activityId) {
                        this.activity.setSelectedIndex(i);
                        break;
                    }
                }
            }
            Settings.save(settings);
        } catch (IOException e) {
            this._setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            throw new RuntimeException(e);
        }
        this._setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void saveLastAccessedActivity() {
        Settings settings;
        try {
            settings = Settings.getInstance();
            Activity selectedActivity = activity.getSelectedValue();
            if (selectedActivity != null) {
                settings.setLastAccessedActivity(selectedActivity);
            }
            Settings.save(settings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JComponent addBorder(String title, JComponent gridComponent) {
        LineBorder lineBorder = new LineBorder(Color.GRAY, 1, true);
        TitledBorder clientTitle = new TitledBorder(lineBorder, title);
        gridComponent.setBorder(clientTitle);

        return gridComponent;
    }

    // adds desired JComponent to specific row/column in GridBagLayout
    private void addToGridBagLayout(int x, int y, GridBagConstraints c, JComponent component) {

        c.gridx = x;
        c.gridy = y;

        this.add(component, c);

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

            Component topLevelFrame = ConfigPanel.getParentFrame(this);
            if (topLevelFrame != null) {
                topLevelFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
            if (TimeSheet.postTimeSheet(timesheet) != null) {
                this.statusPanel.setText("Time entry created.");
            } else {
                this.statusPanel.setText("");
            }
            if (topLevelFrame != null) {
                topLevelFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
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
