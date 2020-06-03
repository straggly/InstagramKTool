package xyz.probablynotkai;

import org.apache.http.HttpHost;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType;
import org.brunocvcunha.instagram4j.requests.payload.*;

import java.io.File;
import java.util.*;

public class Main
{
    public static UI ui = new UI();

    public static HashMap<String, String> db = new HashMap<String, String>();

    @lombok.SneakyThrows
    public static void main(String[] args) {
        File dataFile = new File("accounts.txt");
        if (!dataFile.exists()){
            dataFile.createNewFile();
        }

        for (String entry : DataManagement.readFullFile(dataFile)){
            String[] arr = entry.split(";");

            String username = arr[0];
            String password = arr[1];

            db.put(username, password);
        }

        ui.runGUI(dataFile);
    }

    public static void saveNewEntries(File file){
        for (Map.Entry<String, String> map : db.entrySet()){
            String value = map.getKey() + ";" + map.getValue();

            boolean dupeFound = false;
            for (String entry : DataManagement.readFullFile(file)){
                if (entry.equals(value)){
                    dupeFound = true;
                }
            }

            if (!dupeFound){
                DataManagement.writeTo(file, value);
            }
        }
    }

    public static String getRandomEntry(String username, String password){
        Random random = new Random();

        String[] values = db.values().toArray(new String[0]);
        String randomSet = values[random.nextInt(values.length)];

        String u = "";
        String p = "";

        for (Map.Entry<String, String> map : db.entrySet()){
            String pas = map.getValue();
            String user = map.getKey();

            if (pas.equals(randomSet)){
                p = map.getValue();
                u = map.getKey();
            }
        }

        return u + ";" + p;
    }
}
