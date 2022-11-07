package net.mbmedia.intellinote;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.mbmedia.intellinote.editor.EditorGui;
import net.mbmedia.intellinote.kanban.BoardGui;
import net.mbmedia.intellinote.util.DataLoader;
import net.mbmedia.intellinote.util.SettingsUtil;
import net.mbmedia.intellinote.util.actionlistener.FileMenuActionListener;
import net.mbmedia.intellinote.util.transportobjects.SettingsTO;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.formdev.flatlaf.util.StringUtils.isEmpty;
import static javax.swing.JOptionPane.*;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import static javax.swing.UIManager.setLookAndFeel;
import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;
import static net.mbmedia.intellinote.util.SettingsUtil.*;
import static net.mbmedia.intellinote.util.ThemeMapping.getThemeByTitle;

public class Gui extends JFrame {

    public static final String NAME = "intelliNote";
    private final EditorGui editorGui;
    private final BoardGui boardGui;
    private final JSplitPane splitpane;

    private File chosenFile;

    private Thread autosavingThread;

    public Gui() {
        SettingsTO settingsTO = SettingsUtil.loadSettings();
        FlatLaf theme = getThemeByTitle(settingsTO.getThemeName()).getFlatlaf();
        try {
            setLookAndFeel(theme);
            updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            FlatLightLaf.setup();
            System.out.println("Error setting theme: " + settingsTO.getThemeName());
        }

        Locale.setDefault(settingsTO.getLocale());


        editorGui = new EditorGui();
        boardGui = new BoardGui();

        String lastFilepath = settingsTO.getLastFilepath();
        if (!isEmpty(lastFilepath)) {
            load(lastFilepath);
        } else {
            setTitle(NAME);
        }

        splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitpane.setLeftComponent(editorGui);
        splitpane.setRightComponent(boardGui);

        if (settingsTO.getDividerPosition() != null) {
            splitpane.setDividerLocation(settingsTO.getDividerPosition());
        }

        this.add(splitpane);
        this.setJMenuBar(createMenuBar());

        if (settingsTO.getWindowSize() != null) {
            setSize(settingsTO.getWindowSize());
        } else {
            setSize(1000, 700);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        startAutoSavingThread();
    }

    public JMenuBar createMenuBar() {

        FileMenuActionListener fileMenuActionListener = new FileMenuActionListener(this);
        JMenuItem newFile = new JMenuItem(getResourceString("menu_new_label"));
        newFile.addActionListener(fileMenuActionListener);
        newFile.setActionCommand(FileMenuActionListener.NEWFILE);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

        JMenuItem openFile = new JMenuItem(getResourceString("menu_open_label"));
        openFile.addActionListener(fileMenuActionListener);
        openFile.setActionCommand(FileMenuActionListener.OPENFILE);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        JMenuItem saveFile = new JMenuItem(getResourceString("menu_save_label"));
        saveFile.addActionListener(fileMenuActionListener);
        saveFile.setActionCommand(FileMenuActionListener.SAVEFILE);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        JMenu menuFile = new JMenu(getResourceString("menu_file_label"));

        JMenu menuSettings = new JMenu(getResourceString("menu_settings_label"));
        Gui gui = this;
        menuSettings.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                JDialog settingsDialog = SettingsUtil.getSettingsDialog(gui);
                settingsDialog.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuSettings);

        menuFile.add(newFile);
        menuFile.add(openFile);
        menuFile.add(saveFile);

        return menuBar;
    }


    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() != WindowEvent.WINDOW_CLOSING) {
            return;
        }
        int dividerLocation = this.splitpane.getDividerLocation();
        setSettingDividerPosition(dividerLocation);

        setSettingWindowSize(this.getSize());

        if (this.editorGui.isEdited()) {
            String[] options = {
                    getResourceString("option_save_and_exit"),
                    getResourceString("option_dont_save_and_exit"),
                    getResourceString("option_cancel")
            };
            int n = showOptionDialog(
                    this,
                    getResourceString("question_save_file"),
                    getResourceString("question_save_file_title"),
                    YES_NO_CANCEL_OPTION,
                    QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (n == 0) {
                save();
                this.dispose();
            } else if (n == 1) {
                this.dispose();
            }
        } else {
            System.exit(99);
        }
        autosavingThread.stop();
    }


    private void startAutoSavingThread() {
        autosavingThread = new Thread(() -> {
            while (true) {
                if (this.getChosenFile() != null) {
                    save();
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        autosavingThread.start();
    }


    private void save() {
        DataLoader dataLoader = new DataLoader(this);
        dataLoader.saveFile();
        resetEditState();
    }

    private void load(String filepath) {
        DataLoader dataLoader = new DataLoader(this);
        dataLoader.loadFile(filepath);
        resetEditState();
    }

    public boolean isEdited() {
        return this.editorGui.isEdited() || this.boardGui.isEdited();
    }

    private void clearTextArea() {
        this.editorGui.clearTextArea();
    }

    public File getChosenFile() {
        return this.chosenFile;
    }

    public void setChosenFile(File file) {
        if (file == null) {
            setSettingLastFilePath("");
            setWindowTitle("");
        } else {
            setSettingLastFilePath(file.getAbsolutePath());
            setWindowTitle(file.getName());
        }
        this.chosenFile = file;
    }

    private void setWindowTitle(String s) {
        this.setTitle(NAME + " - " + s);
    }

    public JTextArea getTextArea() {
        return this.editorGui.getTextArea();
    }

    private void clearBoardArea() {
        this.boardGui.clearBoard();
    }

    public void resetEditState() {
        this.editorGui.resetEditState();
        this.boardGui.resetEditState();
    }

    public void clear() {
        clearBoardArea();
        clearTextArea();
    }

    public String[] getTodoItems() {
        return this.boardGui.getTodoItems();
    }

    public String[] getDoneItems() {
        return this.boardGui.getDoneItems();
    }

    public BoardGui getBoardGui() {
        return boardGui;
    }
}
