package com.library.person;

import com.library.book.Book;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Author extends Person {
    private Set<Book> books; // Aynı kitap tekrar eklenemez
    private static int idCounter = 1; // Otomatik ID sayacı
    private final int authorId;      // Her yazar için benzersiz ve değişmez ID

    public Author(String name) {
        super(name);
        this.authorId = idCounter++; // Yeni nesne oluştuğunda ID'yi ata ve sayacı artır
        this.books = new HashSet<>();
    }

    public void addBook(Book newBook) {
        if (newBook == null) {
            System.out.println("Hata: Geçersiz (null) bir kitap eklenemez!");
            return;
        }
        books.add(newBook);
    }

    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("Bu yazarın henüz kütüphanede bir kitabı yok.");
            return;
        }
        System.out.println(getName() + " İsimli Yazarın Kitapları:");
        for (Book book : books) {
            book.display();
        }
    }

    @Override
    public void whoYouAre() {
        System.out.println("Yazar ismi: " + getName());
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public int getAuthorId() {
        return authorId;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authorId);
    }
}
