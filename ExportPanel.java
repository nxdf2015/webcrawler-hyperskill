package crawler;

import javax.swing.*;
import java.awt.*;

public class ExportPanel extends JPanel {
    private final JTextField input;
    private final JButton btn;
    private TextEmitter emitter;

    public ExportPanel() {
        super();
        setLayout(new FlowLayout());
        JLabel label = new JLabel("Export");
        label.setPreferredSize(new Dimension(80,50));
        add(label);

        input = new JTextField();
        input.setPreferredSize(new Dimension(250,50));
        input.setName("ExportUrlTextField");
        add(input);

        btn = new JButton("Save");
        btn.setName("ExportButton");
        btn.setEnabled(true);
        btn.addActionListener(e -> {
            if (emitter != null){
                emitter.send(input.getText());
            }
        });
        add(btn);
    }

    public void setEmitter(TextEmitter emitter) {
        this.emitter = emitter;
    }

    public void toggleButton(boolean state ){
        btn.setEnabled(state);
    }


}
