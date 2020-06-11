package crawler;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel  extends AbstractTableModel {

    private String[] columnsName = {"URL" , "Title"};
    private List<EntityUrl> data;

    public TableModel() {
        super();
        data = new ArrayList<>();
    }

    public void setData(List<EntityUrl> data) {
        if (data == null){
            this.data = data;
        }
        else{
            this.data.addAll(data);
        }
    }

    public List<EntityUrl> getData(){
        return data;
    }





    public void refresh(){
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        EntityUrl entityUrl = data.get(row);
        if (col == 0){
            return entityUrl.getUrl();
        }
        else {
            return entityUrl.getTitle();
        }
    }

    @Override
    public String getColumnName(int column) {
         return columnsName[column];
    }

    public void append(EntityUrl entity) {
        data.add(entity);
    }

    public void reset() {
        data = new ArrayList<>();
    }
}
