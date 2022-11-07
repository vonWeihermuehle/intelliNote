package net.mbmedia.intellinote.util;

import com.google.gson.Gson;
import net.mbmedia.intellinote.Gui;
import net.mbmedia.intellinote.util.transportobjects.DataTO;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;

import static java.nio.file.Files.newBufferedReader;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.*;
import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;

public class DataLoader {

    private final Gui gui;

    public DataLoader(Gui gui) {
        this.gui = gui;
    }

    public void createNewFile() {
        if (this.gui.isEdited()) {
            Object[] options = {
                    getResourceString("option_save"),
                    getResourceString("option_dont_save"),
                    getResourceString("option_cancel")
            };
            int n = showOptionDialog(gui,
                    getResourceString("question_save_current_file"),
                    getResourceString("question_save_current_file_title"),
                    YES_NO_CANCEL_OPTION,
                    QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]
            );
            if (n == 0) {
                saveFile();
            }

        }
        this.gui.clear();
        this.gui.resetEditState();
        this.gui.setChosenFile(null);
    }

    public void loadFile(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new RuntimeException("File doesnt exist: " + filepath);
        }
        gui.setChosenFile(file);
        try {
            loadData(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openFile() {
        if (this.gui.isEdited()) {
            saveFile();
        }

        JFileChooser open = new JFileChooser();
        int option = open.showOpenDialog(gui);
        if (option != APPROVE_OPTION) {
            return;
        }
        this.gui.clear();
        try {
            gui.setChosenFile(open.getSelectedFile());
            loadData(gui.getChosenFile().getPath());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void loadData(String filepath) throws IOException {
        DataTO to = readDataTo(filepath);
        gui.getTextArea().setText(to.getText());
        for (String s : to.getDoneItems()) {
            gui.getBoardGui().addDone(s);
        }
        for (String s : to.getTodoItems()) {
            gui.getBoardGui().addTodo(s);
        }
    }

    public void saveFile() {
        try {
            int option = 0;
            if (gui.getChosenFile() == null) {
                JFileChooser fileChoose = new JFileChooser();
                option = fileChoose.showSaveDialog(gui);
                if (option == APPROVE_OPTION) {
                    gui.setChosenFile(fileChoose.getSelectedFile());
                }
            }
            writeDataToFile(gui.getChosenFile().getPath());

            if (option == APPROVE_OPTION) {
                gui.resetEditState();
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    private void writeDataToFile(String filepath) throws IOException {
        String text = gui.getTextArea().getText();
        String[] todoItems = gui.getTodoItems();
        String[] doneItems = gui.getDoneItems();

        DataTO to = new DataTO(todoItems, doneItems, text);

        Gson gson = new Gson();
        FileWriter writer = new FileWriter(filepath);
        gson.toJson(to, writer);
        writer.flush();
    }

    private DataTO readDataTo(String filepath) throws IOException {
        Gson gson = new Gson();
        Reader reader = newBufferedReader(Paths.get(filepath));
        return gson.fromJson(reader, DataTO.class);
    }


}
