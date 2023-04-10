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

    private static JDialog configFrame;

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

    public static JDialog makeFrame(Component parent, Settings settings) {
        if (configFrame == null) {
            Frame topWindow = ConfigPanel.getParentFrame(parent);
            configFrame = new JDialog(topWindow, "Settings");
            configFrame.add(new ConfigPanel(settings), BorderLayout.CENTER);
            configFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            configFrame.pack();
            configFrame.setLocationRelativeTo(topWindow);
        }
        return configFrame;
    }

    private static Frame getParentFrame(Component me) {
        if (me instanceof Frame) {
            return (Frame)me;
        }

        if (me == null) {
            throw new RuntimeException("Cannot find top level frame");
        }

        return getParentFrame(me.getParent());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Settings settings = new Settings();
            settings.setKimaiUri(this.uri.getText());
            settings.setKimaiUsername(this.username.getText());
            settings.setKimaiPassword(this.password.getText());
            configFrame.setVisible(false);
        } catch (Exception ex) {
            System.err.println("Unreachable");
        }
    }
}
