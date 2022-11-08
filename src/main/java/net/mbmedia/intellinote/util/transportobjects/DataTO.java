package net.mbmedia.intellinote.util.transportobjects;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataTO implements Serializable {

    private final String[] todoItems;
    private final String[] doneItems;
    private final String text;

    public DataTO(String[] todoItems, String[] doneItems, String text) {
        this.todoItems = encodeArray(todoItems);
        this.doneItems = encodeArray(doneItems);
        this.text = encode(text);
    }

    public String[] getTodoItems() {
        return decodeArray(todoItems);
    }

    public String[] getDoneItems() {
        return decodeArray(doneItems);
    }

    public String getText() {
        return decode(text);
    }

    private String[] encodeArray(String[] toEncode){
        for(int i=0; i<toEncode.length; i++){
            toEncode[i] = encode(toEncode[i]);
        }
        return toEncode;
    }

    private String[] decodeArray(String[] toDecode){
        for(int i=0; i<toDecode.length; i++){
            toDecode[i] = decode(toDecode[i]);
        }
        return toDecode;
    }

    private String encode(String toEncode){
        return Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.ISO_8859_1));
    }

    private String decode(String toDecode){
        return new String(Base64.getDecoder().decode(toDecode), StandardCharsets.ISO_8859_1);
    }
}
