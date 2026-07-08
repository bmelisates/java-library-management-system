package com.library.book;

public enum Genre {
    ROMANCE("Romantik"),
    FANTASY("Fantastik"),
    SCIENCE_FICTION("Bilim Kurgu"),
    HORROR("Korku"),
    MYSTERY("Gizem"),
    CRIME("Polisiye"),
    CLASSIC("Klasik");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
