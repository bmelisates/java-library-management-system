package com.library.book;

public enum MagazineType {
    FASHION("Moda"),
    SCIENCE("Bilim"),
    HEALTH("Sağlık"),
    SPORTS("Spor"),
    TECH("Teknoloji");

    private final String displayName;

    MagazineType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}