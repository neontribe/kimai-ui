package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ConfigPanel extends JPanel implements ActionListener {

    JTextField uri;
    JTextField username;
    JTextField password;

    public ConfigPanel() {
        this(new Settings());
    }

    public ConfigPanel(Settings settings) {
        this.setLayout(new BorderLayout(5,5));
        this.setBorder(new EmptyBorder(5,5,5,5));
        // Set layout (pst grid layout)
        JPanel gridRight = new JPanel(new GridLayout(0,1, 5,5));
        this.add(gridRight, BorderLayout.CENTER);
        // Add label and text field for kimaiUri

        gridRight.add(uri = new JTextField(50));
        // Add label and text field for kimaiUsername

        gridRight.add(username = new JTextField(50));
        // Add label and text field for kimaiPassword

        gridRight.add(password = new JTextField(50));
        // Add a save button
        JButton save = new JButton("Save");
        this.add(save, BorderLayout.SOUTH);
        save.addActionListener(this);
        // ?? How could we centre the save button.
        // GridBagLayout or nested panels and layouts

        JPanel gridLeft = new JPanel(new GridLayout(0,1,5,5));
        gridLeft.add(new JLabel("Your Kimai Uri:"));
        gridLeft.add(new JLabel("Username:"));
        gridLeft.add(new JLabel("Password:"));
        this.add(gridLeft, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Settings settings = new Settings();
            settings.setKimaiUri(this.uri.getText());
            settings.setKimaiUsername(this.username.getText());
            settings.setKimaiPassword(this.password.getText());
            Settings.save(settings);
        } catch (Exception ex) {
            System.err.println("Unreachable");
        }
    }
}
