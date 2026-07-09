package com.library.book;

import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Book implements Categorizable {
    private static long bookCount = 0; // Otomatik id sayacı
    private final long bookId;
    private Set<Author> author = new HashSet<>();  // 1.Birden fazla yazarı olan kitaplar için 2.Aynı yazar tekrar eklenemesin diye.
    private String title;
    private double price;
    private Status status;
    private String edition;
    private LocalDate dateOfPurchase;
    private LocalDate borrowDate; // Ödünç alınma tarihi
    private MemberRecord owner;

    // 1. CONSTRUCTOR: Kütüphanedeki kitap için
    public Book(String title, double price, String edition, LocalDate dateOfPurchase, Author... authors) {
        bookCount++;
        this.bookId = bookCount;
        this.title = title;
        this.price = price;
        this.status = Status.AVAILABLE; // Otomatik AVAILABLE başlar
        this.edition = edition;
        this.dateOfPurchase = dateOfPurchase;
        this.owner = null; // Kütüphanede, sahibi yok
        if (authors != null) {
            for (Author a : authors) {
                this.author.add(a); // Kitabın yazar setine ekle
                a.addBook(this);    // Yazarın kendi kitap listesini güncelle!
            }
        }
    }

    // 2. CONSTRUCTOR: Eski sistemden taşınan, halihazırda başkasında olan(ödünç alınmış) kitap için
    public Book(String title, double price, String edition, LocalDate dateOfPurchase, MemberRecord owner, Author... authors) {
        bookCount++;
        this.bookId = bookCount;
        this.title = title;
        this.price = price;
        this.status = Status.BORROWED;
        this.edition = edition;
        this.dateOfPurchase = dateOfPurchase;
        this.owner = owner;  // Kitap kimdeyse onun ismi
        // Eğer owner varsa, kitabı owner'ın elindeki kitaplar listesine ekle
        if (owner != null) {
            owner.getBorrowedBooks().add(this);
        }
        if (authors != null) {
            for (Author a : authors) {
                this.author.add(a); // Kitabın yazar setine ekle
                a.addBook(this);    // Yazarın kendi kitap listesini güncelle!
            }
        }
    }

    public void display() {
        System.out.println("Book ID: " + bookId + ", Name: " + title + ", Author: " + author + ", Status: " + status);
    }

    public long getBookId() {
        return bookId;
    }

    public Set<Author> getAuthor() {
        return author;
    }

    public void setAuthor(Set<Author> author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public MemberRecord getOwner() {
        return owner;
    }

    public void setOwner(MemberRecord owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", is availabe='" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookId);
    }
}
