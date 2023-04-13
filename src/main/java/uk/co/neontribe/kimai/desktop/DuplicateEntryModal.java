package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class DuplicateEntryModal extends JDialog implements ActionListener {

    public static final String LOG_ANYWAY = "Log anyway";

    private boolean shouldProceed = false;

    public DuplicateEntryModal() {
        JButton proceed = new JButton(LOG_ANYWAY);
        JButton cancel = new JButton("Cancel");
        JButton launchKimai = new JButton("Open Kimai");

        this.setTitle("Duplicate entry detected");
        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();

        JPanel label = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        label.add(new JLabel("An existing entry for this"), c);
        c.gridy = 1;
        label.add(new JLabel("time sheet exists."), c);
        c.gridy = 2;
        c.ipady = 20;
        label.add(new JLabel("Do you want to create another entry?"), c);
        this.add(label, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridBagLayout());

        c.gridy = 0;
        c.ipady = 0;
        c.ipadx = 5;
        buttons.add(proceed, c);
        c.gridx = 1;
        buttons.add(cancel, c);
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        buttons.add(launchKimai);

        this.add(buttons, BorderLayout.SOUTH);
        this.pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);


        launchKimai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Settings setting = Settings.getInstance();
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            shouldProceed = false;
                            setVisible(false);
                            desktop.browse(new URL(setting.getKimaiUri()).toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dispose();
            }
        });

        proceed.addActionListener(this);
        cancel.addActionListener(this);
    }

    public boolean getShouldProceed() {
        return this.shouldProceed;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.shouldProceed = Objects.equals(actionEvent.getActionCommand(), LOG_ANYWAY);
        this.setVisible(false);
        dispose();
    }
}
