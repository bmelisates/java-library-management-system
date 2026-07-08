package com.library.book;

public enum SubjectType {
    MATHEMATICS("Matematik"),
    LAW("Hukuk"),
    COMPUTER_SCIENCE("Bilgisayar Bilimleri"),
    HISTORY("Tarih"),
    MEDICINE("Tıp");

    private final String displayName;

    SubjectType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}