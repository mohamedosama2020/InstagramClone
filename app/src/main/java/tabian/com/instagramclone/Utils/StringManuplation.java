package tabian.com.instagramclone.Utils;

public class StringManuplation {

    public static String expandUsername(String username){
        return username.replace("."," ");
    }

    public static String condenceUsername(String username){
        return username.replace(" ",".");
    }

}
