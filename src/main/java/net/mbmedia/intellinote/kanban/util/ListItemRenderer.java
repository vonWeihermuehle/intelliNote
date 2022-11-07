package net.mbmedia.intellinote.kanban.util;

import javax.swing.*;
import java.awt.*;

public class ListItemRenderer extends JTextArea implements ListCellRenderer<String> {

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setTabSize(1);
        setText(value);
        return this;
    }
}
