package crawler;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class WebCrawler extends JFrame implements Observer {
    private final LinksPanel linksPanel;
    private final TablePanel tablePanel;
    private final ExportPanel exportPanel;
    private JTextField input;
    private JTextArea textArea;
    private String[] labelColumns = { "URL" , "Title"};
    private String[][] data;
    private ControlPanel controlPanel;
    private LinksControl linksControl;

    public WebCrawler() {

        linksControl = new LinksControl();
        linksControl.addObserver(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setBackground(Color.blue);
        setLocationRelativeTo(null);
        setTitle("Web Crawler");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        tablePanel = new TablePanel();
        controlPanel = new ControlPanel();
        linksPanel = new LinksPanel();
        controlPanel.setTextEmitter( e -> {

            try {
                tablePanel.reset();


                linksControl.search(e);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.gridy=0;
        c.gridx=0;
        c.gridwidth=5;
        add(controlPanel,c);




        c.gridy=1;
        c.ipady = 8;
        c.gridx=0;
        c.gridwidth=5;
        add(linksPanel,c);

        c.gridx=0;
        c.gridy=2;
        c.gridwidth=5;
        c.gridheight=5;
        c.fill=GridBagConstraints.BOTH;




        add(tablePanel,c);

         exportPanel = new ExportPanel();
         exportPanel.setEmitter(new TextEmitter() {
             @Override
             public void send(String nameFile) {
                 saveTable(nameFile);
             }
         });
        c.gridx = 0;
        c.gridy= 8;
        c.gridheight=1;
        add(exportPanel ,c );
        setVisible(true);
    }

    private void saveTable(String nameFile) {
        tablePanel.save(nameFile);
    }


    @Override
    public void update(Observable observable, Object o) {


       if (o instanceof  Payload ) {
           Payload payload = (Payload) o;
           if (payload.getDataType() == DataType.TITLE) {
               linksPanel.setTitle(payload.getData());
           }
           else if(payload.getDataType() == DataType.COMPLETE){
               exportPanel.toggleButton(true);
           }

       }

       if (o instanceof EntityUrl){
           EntityUrl entityUrl = (EntityUrl)o;
           tablePanel.append(entityUrl);
       }



    }
}

