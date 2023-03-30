package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;

public class ConfigPanel extends JPanel {


    public ConfigPanel() {
        this(new Settings());
    }

    public ConfigPanel(Settings settings) {
        // Set layout (pst grid layout)
        // Add label and text field for kimaiUri
        // Add label and text field for kimaiUsername
        // Add label and text field for kimaiPassword
        // Add a save button

        // ?? How could we centre the save button.
        // GridBagLayout or nested panels and layouts

        // This won't stay here
        this.add(new JLabel("ConfigPanel"));
    }

}
