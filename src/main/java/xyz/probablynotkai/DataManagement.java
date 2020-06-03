package xyz.probablynotkai;

import java.io.*;
import java.util.ArrayList;

public class DataManagement
{
    public static void writeTo(File file, String data){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getCanonicalFile(), true))){
            writer.write(data + "\n");
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public static String readFromFile(File file, int line){
        try(BufferedReader reader = new BufferedReader(new FileReader(file.getCanonicalFile()))){
            int lineCount = 0;
            String lineStr = reader.readLine();

            while (lineCount != line){
                lineCount++;
                lineStr = reader.readLine();
            }

            return lineStr;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> readFullFile(File file){
        ArrayList<String> fullFile = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(file.getCanonicalFile()))){
            String line = reader.readLine();
            while (line != null){
                fullFile.add(line);
                line = reader.readLine();
            }
            return fullFile;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
