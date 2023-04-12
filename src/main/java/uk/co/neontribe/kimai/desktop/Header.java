package uk.co.neontribe.kimai.desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Header extends JPanel {

    public Header(String pageTitle) {
        BorderLayout headerGrid = new BorderLayout();
        this.setLayout(headerGrid);
        this.setBackground(Color.decode("#202A44"));

        EmptyBorder emptyBorder = new EmptyBorder(0, 8, 0, 0);
        JLabel kimaiTitle = new JLabel("Kimai");
        kimaiTitle.setBorder(emptyBorder);
        kimaiTitle.setFont(new Font("Arial", Font.BOLD, 13));
        kimaiTitle.setForeground(Color.WHITE);

        JLabel timeTrackingTitle = new JLabel(pageTitle);
        EmptyBorder timeTrackingBorder = new EmptyBorder(0, 0, 0, 30);

        timeTrackingTitle.setBorder(timeTrackingBorder);
        timeTrackingTitle.setHorizontalAlignment(JLabel.CENTER);

        timeTrackingTitle.setFont(new Font("Arial", Font.BOLD, 20));
        timeTrackingTitle.setForeground(Color.WHITE);

        this.add(kimaiTitle, BorderLayout.WEST);
        this.add(timeTrackingTitle, BorderLayout.CENTER);

    }

}
