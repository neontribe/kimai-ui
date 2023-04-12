package uk.co.neontribe.kimai;

import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;
import uk.co.neontribe.kimai.desktop.KimaiUiFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

public class Main {
    static KimaiUiFrame frame;

    /**
     * The entry point. This will start our application.
     *
     * @param args String[]
     * @throws ConfigNotInitialisedException Thrown when config location cannot be created.
     * @throws IOException                   Thrown if we can't read from the file system
     */
    public static void main(String[] args) throws ConfigNotInitialisedException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, AWTException {
        // Create a new application frame and show it.
        // UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        frame = new KimaiUiFrame();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setVisible(true);

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Image image = toolkit.getImage("trayIcon.jpg");
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

        TrayIcon icon = new TrayIcon(image, "Kimai Tool", menu);
        icon.setImageAutoSize(true);
        icon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.setVisible(true);
            }
        });

        tray.add(icon);
    }
}