package com.library.book;

import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;

public class Novel extends Book implements Categorizable {
    private Genre genre; // Örn: Bilim Kurgu, Polisiye, Klasik

    public Novel(String title, double price, String edition, LocalDate dateOfPurchase, Genre genre, Author... authors) {
        super(title, price, edition, dateOfPurchase, authors);
        this.genre = genre;
    }

    public Novel(String title, double price, Status status, String edition, LocalDate dateOfPurchase, MemberRecord owner, Genre genre, Author... authors) {
        super(title, price, status, edition, dateOfPurchase, owner, authors);
        this.genre = genre;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("[ROMAN] " + getTitle() + " | Tür: " + genre + " | Baskı: " + getEdition() + " | Fiyat: " + getPrice() + " TL");
    }

    @Override
    public String getCategory() {
        return this.genre.toString();
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}