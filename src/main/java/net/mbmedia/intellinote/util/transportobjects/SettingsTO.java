package net.mbmedia.intellinote.util.transportobjects;

import java.awt.*;
import java.util.Locale;

import static java.util.Locale.getDefault;
import static net.mbmedia.intellinote.util.ThemeMapping.FLATLAFLIGHT;

public class SettingsTO {

    private String themeName = FLATLAFLIGHT.toString();
    private Locale locale = getDefault();
    private String lastFilepath = null;
    private Integer dividerPosition;
    private Dimension windowSize = null;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLastFilepath() {
        return lastFilepath;
    }

    public void setLastFilepath(String lastFilepath) {
        this.lastFilepath = lastFilepath;
    }

    public Integer getDividerPosition() {
        return dividerPosition;
    }

    public void setDividerPosition(Integer dividerPosition) {
        this.dividerPosition = dividerPosition;
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }
}
