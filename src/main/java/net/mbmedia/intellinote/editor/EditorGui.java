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
        JPanel container = new JPanel(new BorderLayout());


        textArea = new JTextArea("", 0, 0);
        textArea.setTabSize(2);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                edit = true;
            }
        });

        JTextLineNumber lineNumbers = new JTextLineNumber(textArea);

        container.add(textArea, BorderLayout.CENTER);
        container.add(lineNumbers, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setViewportView(container);
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