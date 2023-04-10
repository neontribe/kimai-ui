package uk.co.neontribe.kimai.desktop;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DurationPanel extends JPanel implements ActionListener {
    protected JTextField duration = new JTextField();

    public DurationPanel() {
        this.setLayout(new BorderLayout());

        JPanel centre = new JPanel(new BorderLayout());
        centre.add(new JLabel("Duration"), BorderLayout.NORTH);
        centre.add(duration, BorderLayout.CENTER);
        this.add(centre, BorderLayout.CENTER);

        JButton fifteenMinutes = new JButton("15m");
        JButton thirtyMinutes = new JButton("30m");
        JButton oneHour = new JButton("1h");
        JButton twoHours = new JButton("2h");
        JButton fourHours = new JButton("4h");
        JButton oneDay = new JButton("1d");

        fifteenMinutes.setBorder(new BevelBorder(BevelBorder.RAISED));
        thirtyMinutes.setBorder(new BevelBorder(BevelBorder.RAISED));
        oneHour.setBorder(new BevelBorder(BevelBorder.RAISED));
        twoHours.setBorder(new BevelBorder(BevelBorder.RAISED));
        fourHours.setBorder(new BevelBorder(BevelBorder.RAISED));
        oneDay.setBorder(new BevelBorder(BevelBorder.RAISED));

        fifteenMinutes.addActionListener(this);
        thirtyMinutes.addActionListener(this);
        oneHour.addActionListener(this);
        twoHours.addActionListener(this);
        fourHours.addActionListener(this);
        oneDay.addActionListener(this);

        JPanel quickPanel = new JPanel(new GridLayout(2,3));
        quickPanel.add(fifteenMinutes);
        quickPanel.add(thirtyMinutes);
        quickPanel.add(oneHour);
        quickPanel.add(twoHours);
        quickPanel.add(fourHours);
        quickPanel.add(oneDay);

        this.add(quickPanel, BorderLayout.EAST);
    }

    public String getDuration() {
        return this.duration.getText();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "15m":
                this.duration.setText("0:15");
                break;
            case "30m":
                this.duration.setText("0:30");
                break;
            case "1h":
                this.duration.setText("1:00");
                break;
            case "2h":
                this.duration.setText("2:00");
                break;
            case "4h":
                this.duration.setText("4:00");
                break;
            case "1d":
                this.duration.setText("7:30");
                break;
        }
    }
}
