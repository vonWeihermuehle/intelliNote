package net.mbmedia.intellinote.editor;

import net.mbmedia.intellinote.BaseGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;

public class EditorGui extends BaseGui {

    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;

    public EditorGui() {
        textArea = new JTextArea("", 0, 0);
        textArea.setTabSize(2);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                edit = true;
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setViewportView(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(getResourceString("note_label")));
        panel.add(scrollPane);
        this.add(panel);

    }

    public void clearTextArea() {
        this.textArea.setText("");
    }

    public JTextArea getTextArea() {
        return this.textArea;
    }

}