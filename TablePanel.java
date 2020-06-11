package crawler;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    public void save(String nameFile) {
        List<EntityUrl> rows = tableModel.getData();
        try(PrintWriter writer = new PrintWriter(new FileWriter(new File(nameFile)))){
            for(EntityUrl row : rows){
                writer.println(row.getUrl());
                writer.println(row.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
