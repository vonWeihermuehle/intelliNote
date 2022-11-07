package net.mbmedia.intellinote.kanban.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.JOptionPane.*;
import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;

public class RightClickListener extends MouseAdapter {

    private final JList<String> list;
    private final Frame parent;

    public RightClickListener(JList<String> list, Frame parent) {
        this.list = list;
        this.parent = parent;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!e.isPopupTrigger()) {
            return;
        }

        int clicked = list.locationToIndex(e.getPoint());
        if (clicked != -1 && list.getCellBounds(clicked, clicked).contains(e.getPoint())) {
            list.setSelectedIndex(clicked);
            JPopupMenu menu = createPopMenu(clicked);
            menu.show(list, e.getX(), e.getY());
        } else {
            JPopupMenu menu = createPopMenu(e);
            menu.show(list, e.getX(), e.getY());
        }
    }

    private JPopupMenu createPopMenu(MouseEvent mouseEvent) {
        return createPopMenu(-1, false, mouseEvent);
    }

    private JPopupMenu createPopMenu(int index) {
        return createPopMenu(index, true, null);
    }

    private JPopupMenu createPopMenu(int index, boolean withRemoveOption, MouseEvent mouseEvent) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem(getResourceString("add_label"));
        addItem.addActionListener(e -> {
            JDialog dialog = createAddWindow();
            if (mouseEvent != null) {
                dialog.setLocation(mouseEvent.getX(), mouseEvent.getY());
            }
            dialog.setVisible(true);
        });

        JMenuItem clearAll = new JMenuItem(getResourceString("clear_all_label"));
        clearAll.addActionListener(e -> {
            Object[] options = {getResourceString("option_yes"), getResourceString("option_cancel")};
            int question = showOptionDialog(parent, getResourceString("question_remove_all_entries"), getResourceString("question_remove_all_title"),
                    YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, options, options[1]);
            if(question == OK_OPTION){
                ((DefaultListModel<?>) list.getModel()).clear();
            }
        });


        menu.add(addItem);
        menu.add(clearAll);

        if (!withRemoveOption) {
            return menu;
        }

        JMenuItem removeItem = new JMenuItem(getResourceString("remove_label"));
        removeItem.addActionListener(e -> ((DefaultListModel<?>) list.getModel()).remove(index));
        menu.add(removeItem);

        return menu;
    }

    private JDialog createAddWindow() {
        JDialog dialog = new JDialog(parent);
        dialog.setTitle(getResourceString("add_dialog_label"));
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextArea textArea = new JTextArea();
        textArea.setTabSize(1);
        panel.add(textArea);
        JButton save = new JButton(getResourceString("button_label_save"));
        save.addActionListener(e -> {
            if (!textArea.getText().isEmpty()) {
                ((DefaultListModel<String>) list.getModel()).add(list.getModel().getSize(), textArea.getText());
            }
            dialog.dispose();
        });
        panel.add(save);

        dialog.add(panel);
        dialog.pack();
        dialog.setSize(400, 400);
        return dialog;
    }

}
