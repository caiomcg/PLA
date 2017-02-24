package sample.Utils;

import java.io.*;

/**
 * Created by caiomcg on 24/02/2017.
 */
public final class FileManager {
    public static boolean saveFile(String content, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public static String openFile(File file) {
        String lineRead = "";
        String fileContent = "";


        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((lineRead = bufferedReader.readLine()) != null) {
                fileContent += lineRead + "\n";
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
        return fileContent;
    }
}
