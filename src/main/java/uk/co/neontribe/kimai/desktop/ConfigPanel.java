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

    private final JTextField uri;
    private final JTextField username;
    private final JTextField password;

    public ConfigPanel(Settings settings) {
        this.setLayout(new BorderLayout(5,5));
        this.setBorder(new EmptyBorder(5,5,5,5));

        JPanel gridRight = new JPanel(new GridLayout(0,1, 5,5));
        gridRight.add(uri = new JTextField(settings.getKimaiUri(), 30));
        gridRight.add(username = new JTextField(settings.getKimaiUsername(), 30));
        gridRight.add(password = new JTextField(settings.getKimaiPassword(), 30));
        this.add(gridRight, BorderLayout.CENTER);

        JPanel gridLeft = new JPanel(new GridLayout(0,1,5,5));
        gridLeft.add(new JLabel("Your Kimai Uri:"));
        gridLeft.add(new JLabel("Username:"));
        gridLeft.add(new JLabel("Password:"));
        this.add(gridLeft, BorderLayout.WEST);

        JButton save = new JButton("Save");
        this.add(save, BorderLayout.SOUTH);
        save.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Settings settings = new Settings();
            settings.setKimaiUri(this.uri.getText());
            settings.setKimaiUsername(this.username.getText());
            settings.setKimaiPassword(this.password.getText());
            if (Settings.save(settings)) {
                frame.setVisible(false);
            }
        } catch (Exception ex) {
            System.err.println("Unreachable");
        }
    }

    private static JFrame frame;

    public static JFrame makeFrame(Settings settings) {
        if (frame != null) {
            return frame;
        }
        JPanel config = new ConfigPanel(settings);
        frame = new JFrame("Config");
        frame.add(config);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        return frame;
    }

}
