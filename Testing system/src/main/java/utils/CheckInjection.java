package utils;

/**
 * Created by disqustingman on 19.06.16.
 */
public class CheckInjection {
    public static String normalize(String text){
        text = text.replaceAll("&","&#38;");
        text = text.replaceAll(":","&#58;");
        text = text.replaceAll("'","&#39;");
        text = text.replaceAll("<","&#60;");
        text = text.replaceAll(">","&#62;");
        return text;
    }
}
