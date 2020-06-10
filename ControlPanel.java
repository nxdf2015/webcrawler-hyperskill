package crawler;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JTextField input;
    private JButton urlBtn;
    private TextEmitter textEmitter;

    public void setTextEmitter(TextEmitter textEmitter) {
        this.textEmitter = textEmitter;
    }

    public ControlPanel() {
        super();

         setPreferredSize(new Dimension(500, 50));
         setLayout(new FlowLayout(FlowLayout.LEFT));
         add(new JLabel("URL: "));
         input=new JTextField();
         input.setName("UrlTextField");
         input.setPreferredSize(new Dimension(300, 45));

         add(input);

        urlBtn= new JButton("Parse");
        urlBtn.setPreferredSize(new Dimension(100, 45));

        urlBtn.addActionListener(null);
        urlBtn.setName("RunButton");
        urlBtn.addActionListener(e -> {

            if (textEmitter!=null)
                textEmitter.send(input.getText());
        });
        add(urlBtn);


    }
}
