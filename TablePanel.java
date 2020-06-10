package crawler;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TablePanel extends JPanel {

    private final JTable table;
    private TableModel tableModel;
    public TablePanel() {
        super();

        tableModel = new TableModel();

        setLayout(new BorderLayout());
        Border border  = BorderFactory.createLineBorder(Color.BLUE,3);
        Border emptyBorder = BorderFactory.createEmptyBorder(15,15,15,15);
        setBorder(BorderFactory.createCompoundBorder(border,emptyBorder));
        setPreferredSize(new Dimension(300,600));

        table = new JTable(tableModel);
        table.setName("TitlesTable");
        table.setSize(300,400);
        table.setEnabled(false);

        add(table.getTableHeader(),BorderLayout.NORTH);
        add(new JScrollPane(table),BorderLayout.CENTER);

    }

    public void append(EntityUrl data){
        tableModel.append(data);
        tableModel.refresh();
    }

    public void appendText(String text){

    }


    public void reset() {
        tableModel.reset();
        tableModel.refresh();
    }
}
