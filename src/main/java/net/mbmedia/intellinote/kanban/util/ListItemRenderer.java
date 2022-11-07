package net.mbmedia.intellinote.kanban.util;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ListItemRenderer extends JTextArea implements ListCellRenderer<String> {

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setTabSize(1);
        setText(value);
        Color boarderColor = new Color(242, 242, 242);
        CompoundBorder border = BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 8, 0),
                BorderFactory.createMatteBorder(0, 0, 2, 0, boarderColor)
        );
        setBorder(border);
        return this;
    }
}
