package net.mbmedia.intellinote.util.actionlistener;

import net.mbmedia.intellinote.Gui;
import net.mbmedia.intellinote.util.DataLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileMenuActionListener implements ActionListener {

    public static final String NEWFILE = "NEWFILE";
    public static final String OPENFILE = "OPENFILE";
    public static final String SAVEFILE = "SAVEFILE";

    private final Gui gui;

    public FileMenuActionListener(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DataLoader dataLoader = new DataLoader(gui);
        switch (e.getActionCommand()) {
            case NEWFILE:
                dataLoader.createNewFile();
                return;
            case OPENFILE:
                dataLoader.openFile();
                return;
            case SAVEFILE:
                dataLoader.saveFile();
                return;
            default:
                throw new RuntimeException("Not Implemented yet");
        }
    }


}
