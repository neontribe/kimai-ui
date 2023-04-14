package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.api.Customer;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;

public class ConfigPanel extends JPanel implements ActionListener {

    private static JDialog configFrame;

    private final JTextField uri;
    private final JTextField username;
    private final JTextField password;
    private final JList customer;

    public ConfigPanel(Settings settings) throws ConfigNotInitialisedException, IOException {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.add(new Header("Config Panel"), BorderLayout.NORTH);


        JPanel gridRight = new JPanel(new GridLayout(0, 1, 5, 5));
        customer = new JList<>(Customer.getCustomers());
        customer.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        int start = 0;
        int end = 1;
        customer.setSelectionInterval(start, end);

        gridRight.add(uri = new JTextField(settings.getKimaiUri(), 30));
        gridRight.add(new JScrollPane(customer));
        gridRight.add(username = new JTextField(settings.getKimaiUsername(), 30));
        gridRight.add(password = new JTextField(settings.getKimaiPassword(), 30));
        this.add(gridRight, BorderLayout.CENTER);

        JPanel gridLeft = new JPanel(new GridLayout(0, 1, 5, 5));
        gridLeft.add(new JLabel("Your Kimai Uri:"));
        gridLeft.add(new JLabel("Select clients:"));
        gridLeft.add(new JLabel("Username:"));
        gridLeft.add(new JLabel("Password:"));

        this.add(gridLeft, BorderLayout.WEST);

        JButton save = new JButton("Save");
        this.add(save, BorderLayout.SOUTH);
        save.addActionListener(this);

    }

    public static JDialog makeFrame(Component parent, Settings settings)
            throws ConfigNotInitialisedException, IOException {
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

            Object values = customer.getSelectedValuesList();

            Settings settings = new Settings();
            settings.setKimaiUri(this.uri.getText());
            settings.setKimaiUsername(this.username.getText());
            settings.setKimaiPassword(this.password.getText());
            settings.setFilteredCustomers(values);
            configFrame.setVisible(false);

        } catch (Exception ex) {
            System.err.println("Unreachable");
        }
    }
}
