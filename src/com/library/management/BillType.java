package com.library.management;

public enum BillType {
    LENDING("Kitap Ödünç Alma"),
    DEPOSIT("Depozito Kesme"),
    DEPOSIT_RETURN("Depozito İadesi");

    private final String description;

    BillType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
