package crawler;

import javafx.beans.InvalidationListener;

import javafx.concurrent.Worker;
import kotlin.jvm.Synchronized;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static crawler.DataType.*;

public class LinksControl extends Observable {


    private String baseUrl;

    public LinksControl() {
        super();

    }


    public void search(String url) throws IOException {

        baseUrl =url ;
        Finder finder = new Finder(url);


        Optional<String> page = finder.getPage();


        if (page.isEmpty())
            return;


        Optional<String> title = finder.findTitle(page.get());
        if (title.isEmpty())
            return;
        setChanged();
        notifyObservers(new Payload(TITLE,title.get().trim()));

        setChanged();
        notifyObservers(new EntityUrl(url,title.get()));

        findLinks(page.get());
    }




    public void findLinks(String page) {

        Pattern linkPattern = Pattern.compile("<a.*\\s+href=(\"|\')(.+?)\\1\\s*.*>");
        Matcher matcher = linkPattern.matcher(page);


        while (matcher.find()) {


            String link = matcher.group(2);
            URL url = null ;
            try {
                  url = new URL(baseUrl);
            } catch (MalformedURLException e) {

            }

            String protocol = url.getProtocol();
            String base = url.getHost();
            setChanged();

            if (!isValidUrl(link)) {
                if(baseUrl.contains("localhost")){
                    int id =baseUrl.lastIndexOf("/");
                    link = baseUrl.substring(0,id)+ "/" + link;
                }
                if(link.startsWith("//")){
                    link = protocol +":"+ link;
                }
                if(link.startsWith("/")){
                    link = base + link;
                }
                else if(link.indexOf("/") == -1){
                    link = base + "/" + link;
                }

            }



            FinderWorker worker = new FinderWorker(link);
            worker.execute();

        }
        notifyObservers(new Payload(COMPLETE));


    }

    private boolean isValidUrl(String link) {
        return link.startsWith("http://") || link.startsWith("https://") ;
    }




    class FinderWorker extends SwingWorker<EntityUrl,EntityUrl> {


        private final String url;
        private Finder finder ;
        public FinderWorker(String url) {
            this.url = url;
            finder = new Finder(url);
        }

        @Override
        protected EntityUrl doInBackground() throws Exception {

                Optional<String> result  = finder.run();

                if (result.isPresent()) {

                    return new EntityUrl(url, result.get());
                }

                return null;
        }



        @Override
        protected void done() {
            super.done();

            try {
                EntityUrl entityUrl = get();
                if (entityUrl !=null) {
                    setChanged();
                    notifyObservers(entityUrl);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

