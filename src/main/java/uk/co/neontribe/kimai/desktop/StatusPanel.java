package uk.co.neontribe.kimai.desktop;

import uk.co.neontribe.kimai.config.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class StatusPanel extends JPanel {

    JTextField statusBar;

    public StatusPanel() {

        JPanel buttons = new JPanel(new BorderLayout(10, 0));

        JButton settings = new JButton();

        ImageIcon cogIcon = new ImageIcon("Images/cog.png");
        Image cogImg = cogIcon.getImage();

        Image newCogImg = cogImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

        ImageIcon newCogIcon = new ImageIcon(newCogImg);
        settings.setIcon(newCogIcon);

        settings.addActionListener(actionEvent -> openConfigDialog());
        buttons.add(settings, BorderLayout.EAST);

        try {
            JButton openKimai = new JButton(Settings.getInstance().getKimaiUri());
            openKimai.setForeground(Color.BLUE);
            openKimai.setBorder(null);
            openKimai.setOpaque(false);
            openKimai.setContentAreaFilled(false);
            openKimai.setBorderPainted(false);
            openKimai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            openKimai.addActionListener(actionEvent -> {
                try {
                    openWebpage(new URL(Settings.getInstance().getKimaiUri()));
                } catch (IOException e) {
                    statusBar.setText(e.getMessage());
                }
            });
            buttons.add(openKimai, BorderLayout.WEST);
        } catch (Exception e) {
            statusBar.setText(e.getMessage());
        }

        this.setLayout(new BorderLayout());
        this.add(buttons, BorderLayout.EAST);

        statusBar = new JTextField("Status:");
        statusBar.setBorder(null);
        statusBar.setEditable(false);

        this.add(statusBar, BorderLayout.CENTER);
    }

    public void setText(String text) {
        this.statusBar.setText(text);
    }

    private void openConfigDialog() {
        try {
            ConfigPanel.makeFrame(this, Settings.getInstance()).setVisible(true);
        } catch (IOException e) {
            ConfigPanel.makeFrame(this, new Settings()).setVisible(true);
        }
    }

    public static void openWebpage(URL url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(url.toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
