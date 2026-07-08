package com.library.core;

import com.library.book.*;
import com.library.management.Library;
import com.library.member.MemberRecord;
import com.library.member.StudentMember;
import com.library.person.Author;
import com.library.person.Reader;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("====== KÜTÜPHANE SİSTEMİ SİMÜLASYONU BAŞLIYOR ======\n");

        // Yazarı oluşturuyoruz
        Author dostoyevski = new Author("Dostoyevski");
        Author ahmetYilmaz = new Author("Ahmet Yılmaz");
        Author ayseKaya = new Author("Ayşe Kaya");
        //dostoyevski.whoYouAre();
        //System.out.println(dostoyevski.getAuthorId());

        // Reader oluşturuyoruz
        //Reader reader1 = new Reader("Melis");
        //reader1.whoYouAre();


        // Kitap nesneleri oluşturuyoruz
        Book book1 = new Novel("Suç ve Ceza", 150.0, "10.Basım", LocalDate.now(), Genre.CLASSIC, dostoyevski);
        Book book2 = new Novel("Suç ve Ceza", 150.0, "10.Basım", LocalDate.now(), Genre.CLASSIC, dostoyevski);
        Book book3 = new Novel("Karamazov Kardeşler", 150.0, "10.Basım", LocalDate.now(), Genre.CLASSIC, dostoyevski);
        Book book4 = new StudyBook("Borçlar Hukuku Genel Hükümler", 300, "2.basım", LocalDate.now(), SubjectType.LAW, ahmetYilmaz, ayseKaya);

        //book1.display();

        // Library
        Library library = new Library();

        // Kitapları Library'mize ekliyoruz
        library.purchaseBook(book1);
        library.purchaseBook(book2);
        library.purchaseBook(book3);
        library.purchaseBook(book4);

        //System.out.println(library.getInventory());
        //System.out.println(library.searchBook("suç ve ceza"));

        //library.deleteBook(1);
        //library.deleteBook(book1);
        //library.deleteBook(1);

        //System.out.println(library.getInventory());
        //library.listBooksByAuthor("Dostoyevski");
        //library.listBooksByClass(StudyBook.class);
        //library.listBooksBySubCategory("hukuk");
        //library.showAllUniqueAuthors();

        // Üyelik oluşturma
        MemberRecord student1 = new StudentMember(new Reader("Melis"), "address", "05555555");

        library.lendBook(student1, book2);
        library.takeBackBook(student1, book2);
        student1.showBooks();


    }
}