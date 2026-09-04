package git.artdeell.substgen.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONParser {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T jsonFromFile(Class<T> type, File file) throws IOException {
        try(FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, type);
        }
    }

    public static <T> T jsonFromUrl(Class<T> type, URL url) throws IOException {
        try(InputStreamReader reader = new InputStreamReader(url.openStream())) {
            return GSON.fromJson(reader, type);
        }
    }

    public static void jsonToFile(Object input, File file) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(JSONParser.GSON.toJson(input).getBytes(StandardCharsets.UTF_8));
        }
    }
}
