package crawler;

import javafx.util.Builder;

import java.io.*;
import java.net.*;
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

    public Optional<String> run() {

        return getHeader(url);

//             Optional<String> result = getPage();
//
//             if (result.isPresent()){
//                 String page = result.get();
//                  Optional<String> titleResult  = findTitle(page);
//                  String title = null;
//                  if(titleResult.isPresent()){
//                        title = titleResult.get();
//                        return Optional.of(title);
//                  }
//
//
//             }
//


    }

    public Optional<InputStream> connect(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            if (connection.getContentType().contains("text/html")) {
                return Optional.of(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return Optional.empty();
    }

    public Optional<String> getHeader(String url) {
        Optional<InputStream> in = connect(url);
        if (in.isEmpty()) {
            return Optional.empty();
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in.get()))) {
                String line;
                Pattern titlePattern = Pattern.compile("<\\s*title\\s*>(.+?)</\\s*title\\s*>", Pattern.CASE_INSENSITIVE);
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);

                    if (line.contains("</title>")) {
                        Matcher matcher = titlePattern.matcher(builder.toString());
                        if(matcher.find())
                            return Optional.of(matcher.group(1));
                        else
                            return Optional.empty();
                    }
                    builder.append(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public Optional<String> findTitle(String page) {
        Pattern titlePattern = Pattern.compile("<\\s*title\\s*>(.+?)</\\s*title\\s*>", Pattern.CASE_INSENSITIVE);

        Matcher matcher = titlePattern.matcher(page);
        if (matcher.find()) {
            String title = matcher.group(1).strip();

            return Optional.of(title);
        } else {
            return Optional.empty();
        }

    }

    public Optional<String> getPage() {


        Optional<InputStream> in = connect(url);


        if (in.isEmpty()) {
            return Optional.empty();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in.get(), StandardCharsets.UTF_8));) {
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null){

                builder.append(line);
                builder.append(System.lineSeparator());

            }
            return Optional.of(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
