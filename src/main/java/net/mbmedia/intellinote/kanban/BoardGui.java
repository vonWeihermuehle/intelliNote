package net.mbmedia.intellinote.kanban;

import net.mbmedia.intellinote.BaseGui;
import net.mbmedia.intellinote.kanban.util.ListItemRenderer;
import net.mbmedia.intellinote.kanban.util.ListTransferHandler;
import net.mbmedia.intellinote.kanban.util.RightClickListener;

import javax.swing.*;
import java.util.Arrays;

import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;

public class BoardGui extends BaseGui {

    private final JList<String> todoList = new JList<>(new DefaultListModel<>());
    private final JList<String> doneList = new JList<>(new DefaultListModel<>());

    public BoardGui() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

        JPanel todoPanel = new JPanel();
        todoPanel.setLayout(new BoxLayout(todoPanel, BoxLayout.Y_AXIS));
        todoPanel.add(new JLabel(getResourceString("todo_label")));
        todoList.addMouseListener(new RightClickListener(todoList, frame));
        todoList.setCellRenderer(new ListItemRenderer());
        JScrollPane todoListScrollPane = new JScrollPane(todoList);
        todoListScrollPane.createHorizontalScrollBar();
        todoListScrollPane.createVerticalScrollBar();
        todoPanel.add(todoListScrollPane);

        JPanel donePanel = new JPanel();
        donePanel.setLayout(new BoxLayout(donePanel, BoxLayout.Y_AXIS));
        donePanel.add(new JLabel(getResourceString("done_label")));
        doneList.addMouseListener(new RightClickListener(doneList, frame));
        doneList.setCellRenderer(new ListItemRenderer());
        donePanel.add(new JScrollPane(doneList));

        this.add(todoPanel);
        this.add(donePanel);

        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        todoList.setDragEnabled(true);
        doneList.setDragEnabled(true);

        todoList.setDropMode(DropMode.INSERT);
        doneList.setDropMode(DropMode.INSERT);

        todoList.setTransferHandler(new ListTransferHandler());
        doneList.setTransferHandler(new ListTransferHandler());

        todoList.addListSelectionListener(e -> edit = true);
        doneList.addListSelectionListener(e -> edit = true);
    }

    public void addTodo(String s) {
        addTodo(this.todoList.getModel().getSize(), s);
    }

    public void addTodo(int index, String s) {
        ((DefaultListModel<String>) this.todoList.getModel()).add(index, s);
    }

    public void addDone(String s) {
        addDone(this.doneList.getModel().getSize(), s);
    }

    public void addDone(int index, String s) {
        ((DefaultListModel<String>) this.doneList.getModel()).add(index, s);
    }

    public void clearBoard() {
        ((DefaultListModel<String>) this.todoList.getModel()).clear();
        ((DefaultListModel<String>) this.doneList.getModel()).clear();
    }

    public String[] getTodoItems() {
        Object[] objects = ((DefaultListModel<String>) this.todoList.getModel()).toArray();
        return Arrays.copyOf(objects, objects.length, String[].class);
    }

    public String[] getDoneItems() {
        Object[] objects = ((DefaultListModel<String>) this.doneList.getModel()).toArray();
        return Arrays.copyOf(objects, objects.length, String[].class);
    }

}
