package uk.co.neontribe.kimai;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;
import uk.co.neontribe.kimai.desktop.KimaiUiFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class Main {
    static KimaiUiFrame frame;

    /**
     * The entry point. This will start our application.
     *
     * @param args String[]
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException                   Thrown if we can't read from the file system
     */
    public static void main(String[] args) throws IOException {
        // Create a new application frame and show it.
        // UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        frame = new KimaiUiFrame();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        PopupMenu menu = new PopupMenu();
        MenuItem messageItem = new MenuItem("Open Kimai");
        messageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Settings settings = Settings.getInstance();
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URL(settings.getKimaiUri()).toURI());
                    }
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        menu.add(messageItem);
        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(closeItem);

        TrayIcon icon = null;
        try {
            InputStream is = new BufferedInputStream(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("images/kimai.png")));
            Image image = ImageIO.read(is);
            icon = new TrayIcon(image, "Kimai Tool", menu);
            icon.setImageAutoSize(true);
            icon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    frame.setVisible(true);
                }
            });
            tray.add(icon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }
}