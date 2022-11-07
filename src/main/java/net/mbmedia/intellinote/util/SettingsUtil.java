package net.mbmedia.intellinote.util;

import com.google.gson.Gson;
import net.mbmedia.intellinote.Gui;
import net.mbmedia.intellinote.util.transportobjects.SettingsTO;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.nio.file.Files.newBufferedReader;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import static javax.swing.UIManager.setLookAndFeel;
import static net.mbmedia.intellinote.util.ResourceUtil.getResourceString;

public class SettingsUtil {

    public static SettingsTO loadSettings() {
        try {
            Gson gson = new Gson();
            Reader reader = newBufferedReader(Paths.get(getFilepath()));
            return gson.fromJson(reader, SettingsTO.class);
        } catch (IOException e) {
            return new SettingsTO();
        }
    }

    public static void setSettingWindowSize(Dimension dimension){
        SettingsTO to = loadSettings();
        to.setWindowSize(dimension);
        setSettings(to);
    }

    public static void setSettingDividerPosition(int position){
        SettingsTO to = loadSettings();
        to.setDividerPosition(position);
        setSettings(to);
    }

    public static void setSettingLastFilePath(String filepath){
        SettingsTO to = loadSettings();
        to.setLastFilepath(filepath);
        setSettings(to);
    }

    public static void setSettingsThemeName(String themeName) {
        SettingsTO to = loadSettings();
        to.setThemeName(themeName);
        setSettings(to);
    }

    public static void setSettingsLocale(Locale locale) {
        SettingsTO to = loadSettings();
        to.setLocale(locale);
        setSettings(to);
    }

    public static void setSettings(SettingsTO to) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(getFilepath());
            gson.toJson(to, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilepath() {
        return System.getProperty("user.home") + "/.intelliNote";
    }

    public static JDialog getSettingsDialog(Gui gui) {
        JDialog dialog = new JDialog(gui);
        dialog.setTitle(getResourceString("menu_settings_label"));
        dialog.setSize(new Dimension(295, 200));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(295, 200));
        panel.setLayout(null);

        JLabel themeLabel = new JLabel(getResourceString("menu_themes_label"));

        JComboBox<ThemeMapping> themeBox = new JComboBox<>();
        themeBox.setModel(new DefaultComboBoxModel<>(ThemeMapping.values()));

        themeBox.addActionListener(e -> {
            ThemeMapping selectedItem = (ThemeMapping) themeBox.getModel().getSelectedItem();
            try {
                setLookAndFeel(selectedItem.getFlatlaf());
                updateComponentTreeUI(gui);
                setSettingsThemeName(selectedItem.toString());
            } catch (Exception ex) {
                System.err.println("Failed to initialize Theme");
            }
        });

        JLabel languageLabel = new JLabel(getResourceString("menu_language_label"));

        JComboBox<AvailableLocales> languageBox = new JComboBox<>();
        languageBox.setModel(new DefaultComboBoxModel<>(AvailableLocales.values()));

        languageBox.addActionListener(e -> {
            AvailableLocales selectedItem = (AvailableLocales) languageBox.getModel().getSelectedItem();
            Locale.setDefault(selectedItem.getLocale());
            ResourceBundle.clearCache();
            updateComponentTreeUI(gui);
            setSettingsLocale(selectedItem.getLocale());
            showMessageDialog(gui, getResourceString("message_please_restart"));
        });

        JButton btnOk = new JButton(getResourceString("option_ok"));
        btnOk.addActionListener(e -> dialog.dispose());


        panel.add(themeLabel);
        panel.add(themeBox);
        panel.add(languageLabel);
        panel.add(languageBox);
        panel.add(btnOk);

        themeLabel.setBounds(15, 30, 100, 25);
        themeBox.setBounds(120, 30, 100, 25);
        languageLabel.setBounds(15, 65, 100, 25);
        languageBox.setBounds(120, 65, 100, 25);
        btnOk.setBounds(10, 125, 100, 25);

        dialog.add(panel);
        return dialog;
    }
}
