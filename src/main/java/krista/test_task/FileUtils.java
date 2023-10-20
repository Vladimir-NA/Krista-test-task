package krista.test_task;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

    public static ArrayList<File> getFilesList(String path){
        ArrayList<File> filesList = new ArrayList<>();
        File file = new File(path);
        if (file.isFile()) {
            if (FileUtils.checkFileFormat(file)) filesList.add(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (FileUtils.checkFileFormat(f)) filesList.add(f);
                }
            }
        } else {
            return null;
        }
        return filesList;
    }

    public static boolean checkFileFormat(File file){
        return getFileExtension(file).equals(".xml");
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) return "";
        return name.substring(lastIndexOf);
    }
}
