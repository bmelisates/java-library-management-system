package com.library.book;

import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;

public class Magazine extends Book implements Categorizable {
    private MagazineType magazineType;

    public Magazine(String title, double price, String edition, LocalDate dateOfPurchase, MagazineType magazineType, Author... authors) {
        super(title, price, edition, dateOfPurchase, authors);
        this.magazineType = magazineType;
    }

    public Magazine(String title, double price, Status status, String edition, LocalDate dateOfPurchase, MemberRecord owner, MagazineType magazineType, Author... authors) {
        super(title, price, status, edition, dateOfPurchase, owner, authors);
        this.magazineType = magazineType;
    }

    @Override
    public void display() {
        super.display();
        System.out.println(" -> Dergi Türü (Magazine Type): " + magazineType);
    }

    @Override
    public String getCategory() {
        return this.magazineType.toString();
    }

    public MagazineType getMagazineType() {
        return magazineType;
    }

    public void setMagazineType(MagazineType magazineType) {
        this.magazineType = magazineType;
    }


}
