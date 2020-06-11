package crawler;

import java.util.List;

public class Payload  {
    private DataType dataType;
    String  data;


    public  Payload(DataType dataType, String  value) {
        this.dataType= dataType;
        this.data = value;
    }

    public Payload(DataType type) {
        this.dataType = type;
    }


    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String  getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "dataType=" + dataType +
                ", data='" + data + '\'' +
                '}';
    }

    public void setData(String   data) {
        this.data = data;
    }
}
