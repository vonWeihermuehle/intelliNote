package net.mbmedia.intellinote.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtil {

    public static String getResourceString(String key){
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("texts.resource", locale);
        return bundle.getString(key);
    }
}
