package crawler;

import javafx.util.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
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

    public Optional<String>  findTitle(String page){
        Pattern titlePattern = Pattern.compile("<\\s*title\\s*>(.+?)</\\s*title\\s*>",Pattern.CASE_INSENSITIVE);

        Matcher matcher = titlePattern.matcher(page);
        if (matcher.find()) {
            String title = matcher.group(1).strip();

            return Optional.of(title);
        }
        else {
            return Optional.empty();
        }

    }

    public Optional<String> getPage()   {


        URLConnection connection = null;


        InputStream in = null;

        try {
            connection = new URL(url).openConnection();

        } catch (MalformedURLException  e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

        if (connection == null)
            return Optional.empty();
        try {
            if (!connection.getContentType().contains("text/html")) {
                return Optional.empty();
            }
        }
        catch (NullPointerException e){
            return Optional.empty();
        }


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
                if (((line = reader.readLine()) == null)) break;
            } catch (IOException e) {
                return Optional.empty();
            }
            builder.append(line);
            builder.append(System.lineSeparator());
        }

        return Optional.of(builder.toString());

    }
}
