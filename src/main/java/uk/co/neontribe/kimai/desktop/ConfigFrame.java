package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigFrame extends JDialog implements ActionListener {

    private static ConfigFrame configFrame;
    private Settings settings;

    private final JTextField uri;
    private final JTextField username;
    private final JTextField password;
//    private final JTextField fontSize;

    public ConfigFrame(Settings settings) {
        this.settings = settings;
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Settings");

        this.add(new Header("Config Panel"), BorderLayout.NORTH);


        JPanel gridRight = new JPanel(new GridLayout(0, 1, 5, 5));
        gridRight.add(uri = new JTextField(this.settings.getKimaiUri(), 30));
        gridRight.add(username = new JTextField(this.settings.getKimaiUsername(), 30));
        gridRight.add(password = new JPasswordField("", 30));
//        gridRight.add(fontSize = new JTextField("", settings.getFontSize()));
        this.add(gridRight, BorderLayout.CENTER);

        JPanel gridLeft = new JPanel(new GridLayout(0, 1, 5, 5));
        gridLeft.add(new JLabel("Your Kimai Uri:"));
        gridLeft.add(new JLabel("Username:"));
        gridLeft.add(new JLabel("Password:"));
//        gridLeft.add(new JLabel("Font size:"));
        this.add(gridLeft, BorderLayout.WEST);

        JButton save = new JButton("Save");
        this.add(save, BorderLayout.SOUTH);
        save.addActionListener(this);
    }

    public Settings getSettings() {
        return this.settings;
    }

    public static ConfigFrame makeFrame(Component parent, Settings settings) {
        if (configFrame == null) {
            Frame topWindow = ConfigFrame.getParentFrame(parent);
            configFrame = new ConfigFrame(settings);
            configFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            configFrame.pack();
            configFrame.setLocationRelativeTo(topWindow);
            configFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            configFrame.setModal(true);
        }
        return configFrame;
    }

    public static Frame getParentFrame(Component me) {
        if (me instanceof Frame) {
            return (Frame) me;
        }

        if (me == null) {
            return null;
        }

        return getParentFrame(me.getParent());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            settings.setKimaiUri(this.uri.getText());
            settings.setKimaiUsername(this.username.getText());
            if (this.password.getText().length() > 0) {
                settings.setKimaiPassword(this.password.getText());
            }
//            settings.setFontSize(Integer.parseInt(this.fontSize.getText()));
            configFrame.setVisible(false);
        } catch (Exception ex) {
            System.err.println("Unreachable");
        }
    }
}
