package net.mbmedia.intellinote.util.transportobjects;

import java.io.Serializable;

public class DataTO implements Serializable {

    private final String[] todoItems;
    private final String[] doneItems;
    private final String text;

    public DataTO(String[] todoItems, String[] doneItems, String text) {
        this.todoItems = todoItems;
        this.doneItems = doneItems;
        this.text = text;
    }

    public String[] getTodoItems() {
        return todoItems;
    }

    public String[] getDoneItems() {
        return doneItems;
    }

    public String getText() {
        return text;
    }
}
