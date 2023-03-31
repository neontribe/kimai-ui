package uk.co.neontribe.kimai.desktop;

import javax.swing.*;
import java.awt.*;

public class TimeEntryPanel extends JPanel {

    private JComboBox customer;
    private JComboBox project;
    private JComboBox activity;

    public TimeEntryPanel() {
        this.setLayout(new BorderLayout());

        JPanel left = new JPanel(new GridLayout(0, 2, 5, 5));
        left.add(new JLabel("Client"));
        left.add(new JComboBox<String>());
        left.add(new JLabel("Project"));
        left.add(new JComboBox<String>());
        left.add(new JLabel("Activity"));
        left.add(new JComboBox<String>());

        this.add(left, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(5, 5));

        JPanel datePicker = new JPanel(new GridLayout(3,1, 2, 2));
        datePicker.add(this.customer = new JComboBox<String>());
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
