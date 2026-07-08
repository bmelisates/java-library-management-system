package com.library.book;

public enum FieldOfStudy {
    MEDICINE("Tıp"),
    LAW("Hukuk"),
    ENGINEERING("Mühendislik"),
    ART_AND_DESIGN("Sanat ve Tasarım"),
    ECONOMICS("Ekonomi"),
    SOCIOLOGY("Sosyoloji");

    private final String displayName;

    FieldOfStudy(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}