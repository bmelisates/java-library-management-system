package com.library.person;

import com.library.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Author extends Person {
    private List<Book> books;
    private static int idCounter = 1; // Otomatik ID sayacı
    private final int authorId;      // Her yazar için benzersiz ve değişmez ID

    public Author(String name) {
        super(name);
        this.authorId = idCounter++; // Yeni nesne oluştuğunda ID'yi ata ve sayacı artır
        this.books = new ArrayList<>();
    }

    public void addBook(Book newBook) {
        if (newBook == null) {
            System.out.println("Hata: Geçersiz (null) bir kitap eklenemez!");
            return;
        }
        // Kitap listede zaten var mı kontrol et!
        if (!books.contains(newBook)) {
            books.add(newBook);
        } else {
            System.out.println("Bilgi: Bu kitap zaten " + getName() + " isimli yazar listesinde mevcut.");
        }
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
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
