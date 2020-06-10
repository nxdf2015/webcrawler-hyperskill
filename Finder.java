package crawler;

import javafx.util.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {


    String url;

    public Finder(String url) {
        this.url = url;

    }




    public Optional<String>  run() {


             Optional<String> result = getPage();

             if (result.isPresent()){
                 String page = result.get();
                  Optional<String> titleResult  = findTitle(page);
                  String title = null;
                  if(titleResult.isPresent()){
                        title = titleResult.get();
                        return Optional.of(title);
                  }


             }


        return Optional.empty();
    }

    private Optional<String>  findTitle(String page){
        Pattern titlePattern = Pattern.compile("<title>(.+)</title>",Pattern.CASE_INSENSITIVE);

        Matcher matcher = titlePattern.matcher(page);
        if (matcher.find()) {
            String title = matcher.group(1).trim();

            return Optional.of(title);
        }
        else {
            return Optional.empty();
        }

    }

    private Optional<String> getPage()   {

        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException e) {
            return Optional.empty();
        }
        System.out.println(url);
        if(!connection.getContentType().contains("text/html")){
            return Optional.empty();
        }
        InputStream in = null;
        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            return Optional.empty();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line=null ;
        StringBuilder builder = new StringBuilder();
        while(true){
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                return Optional.empty();
            }
            builder.append(line);

        }

        return Optional.of(builder.toString());
    }
}
