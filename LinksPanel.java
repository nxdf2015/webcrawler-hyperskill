package crawler;

import javax.swing.*;
import java.awt.*;

public class LinksPanel extends JPanel {

    private final JLabel titleField;
    private final JLabel titleLabel;

    public LinksPanel() {
        super();
       // setLayout(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel("Title: ");

        add(titleLabel);
        titleLabel.setPreferredSize(new Dimension(150, 50));


        titleField = new JLabel("");
        titleField.setName("TitleLabel");
        add(titleField);
        titleField.setPreferredSize(new Dimension(150, 50));
    }

    public void setTitle(String title){
        titleField.setText( title.trim());
    }

    public void reset() {
        titleField.setText("");
    }
}
