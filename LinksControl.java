package crawler;

import javafx.beans.InvalidationListener;

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
    private String protocol = "https";
    public LinksControl() {
        super();
    }


    public void search(String url) throws IOException {
        baseUrl = "index.html";
        Optional<String> page = getPage(url);
        if (page.isEmpty())
            return;

        String title = findTitle(page.get());
        setChanged();

        notifyObservers(new Payload(TITLE,title));

        setChanged();
        notifyObservers(new EntityUrl(url,title));

        findLinks(page.get());
    }


    public Optional<String> getPage(String url) throws IOException {
       try(InputStream inputStream = new URL(url).openStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );) {

           StringBuilder builder = new StringBuilder();
           String line;

           while ((line = reader.readLine()) != null) {
               builder.append(line);
               builder.append(System.lineSeparator());
           }

           String result = builder.toString();
           findTitle(result);
           return Optional.of(result);
       }
       catch (MalformedURLException e){
           return Optional.empty();
       }
    }



    public String  findTitle(String page) {
        Pattern titlePattern = Pattern.compile("<title>(.+)</title>", Pattern.CASE_INSENSITIVE);

        Matcher matcher = titlePattern.matcher(page);
        String title = null;
        if (matcher.find()) {
              title = matcher.group(1).trim();
           }
       return title;
    }




    public void findLinks(String page) {

        Pattern linkPattern = Pattern.compile("<a.*\\s+href=(\"|\')(.+?)\\1\\s*.*>");
        Matcher matcher = linkPattern.matcher(page);
        List<String> results = new ArrayList<>();
        while (matcher.find()) {
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

            FinderWorker worker = new FinderWorker(link);
            worker.execute();

        }


    }

    private boolean isValidUrl(String link) {
        return Pattern.matches("https?://.+",link);
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

            Optional<String> result =  finder.run();
            if (result.isPresent()){
               return new EntityUrl(url,result.get());
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

