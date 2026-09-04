package git.artdeell.substgen.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Downloader {
    public static void urlToStream(OutputStream outputStream, URL url) throws IOException {
        try(InputStream inputStream = url.openStream()) {
            byte[] buffer = new byte[15000];
            int read;
            while((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0 ,read);
            }
        }
    }

    public static void urlToFile(File file, URL url) throws IOException{
        try(FileOutputStream fileInputStream = new FileOutputStream(file)) {
            urlToStream(fileInputStream, url);
        }
    }

    public static String urlToString(URL url) throws IOException{
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            urlToStream(outputStream, url);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }
}
