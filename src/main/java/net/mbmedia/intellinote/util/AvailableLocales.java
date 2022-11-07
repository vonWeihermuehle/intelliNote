package net.mbmedia.intellinote.util;

import java.util.Locale;

public enum AvailableLocales {

    EN("English", Locale.ENGLISH),
    DE("Deutsch", Locale.GERMANY);

    private final String name;
    private final Locale locale;

    AvailableLocales(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    @Override
    public String toString() {
        return name;
    }

    public Locale getLocale() {
        return locale;
    }

}
