package tabian.com.instagramclone.Utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FileSearch {


    /**
     * Search Directory and return a list of all Directories contatined inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for(int i=0;i<listFiles.length;i++){
            if (listFiles[i].isDirectory()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search Directory and return a list of all Files contatined inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){


        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for(int i=0;i<listFiles.length;i++){
            if (listFiles[i].isFile()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
