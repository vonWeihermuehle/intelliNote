package net.mbmedia.intellinote.util;

import com.formdev.flatlaf.*;

public enum ThemeMapping {

    FLATLAFLIGHT("Light", new FlatLightLaf()),
    FLATLAFINTELLIJ("Intellij Light", new FlatIntelliJLaf()),
    FLATLAFDARK("Dark", new FlatDarkLaf()),
    FLATLAFDARCULA("Darcula", new FlatDarculaLaf());

    private final String title;
    private final FlatLaf flatlaf;
    ThemeMapping(String title, FlatLaf flatlaf){
        this.title = title;
        this.flatlaf = flatlaf;
    }

    public FlatLaf getFlatlaf(){
        return this.flatlaf;
    }

    @Override
    public String toString() {
        return title;
    }

    public static ThemeMapping getThemeByTitle(String title) {
        for (ThemeMapping theme : values()) {
            if (theme.title.equals(title)) {
                return theme;
            }
        }
        throw new IllegalArgumentException(title);
    }
}
