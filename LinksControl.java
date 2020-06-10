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

    private List<SwingWorker<EntityUrl,EntityUrl>> workers ;
    private String baseUrl;
    private String protocol = "https";
    private boolean cancel = false;

    public LinksControl() {
        super();
        workers = new ArrayList<>();
    }


    public void search(String url) throws IOException {
        cancel = false;
        baseUrl = "index.html";
        Finder finder = new Finder(url);

        Optional<String> page = finder.getPage();

        if (page.isEmpty())
            return;

        Optional<String> title = finder.findTitle(page.get());
        if (title.isEmpty())
            return;
        setChanged();

        notifyObservers(new Payload(TITLE,title.get()));

        setChanged();
        notifyObservers(new EntityUrl(url,title.get()));

        findLinks(page.get());
    }




    public void findLinks(String page) {

        Pattern linkPattern = Pattern.compile("<a.*\\s+href=(\"|\')(.+?)\\1\\s*.*>");
        Matcher matcher = linkPattern.matcher(page);
        List<String> results = new ArrayList<>();

        while (matcher.find()) {

            if (cancel){
                return;
            }
            String link = matcher.group(2);
            if (!isValidUrl(link)){

                if(link.endsWith(".html")){
                    int id = baseUrl.lastIndexOf("/");
                    link =  baseUrl.substring(0,id+1) + link;
                }
                if (link.startsWith("//")){
                    link = protocol +":"+ link;
                }
                else if (!link.startsWith("http://") || !link.startsWith("https://") ) {
                    link = protocol +"://"+ link;
                }
            }
            System.out.println(link);
            FinderWorker worker = new FinderWorker(link);
            worker.execute();
            workers.add(worker);
        }


    }

    private boolean isValidUrl(String link) {
        return Pattern.matches("https?://.+",link);
    }

    public void cancelAll() {
        cancel = true;
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

                Optional<String> result = finder.run();
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
                    notifyObservers(get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

