package utils;

import javafx.stage.FileChooser;

import java.io.File;

public class FileHelper {

    public static String getFileExtension(String path) {
        int lastIndexOf = path.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return path.substring(lastIndexOf);
    }

    public static String getPath() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        return file.getAbsolutePath();
    }

}
