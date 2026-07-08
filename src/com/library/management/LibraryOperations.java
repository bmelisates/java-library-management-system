package com.library.management;

import com.library.book.Book;
import com.library.member.MemberRecord;

public interface LibraryOperations {
    void purchaseBook(Book book);

    void lendBook(MemberRecord member, Book book);      // 👈 İsmi lendBook yapıyoruz

    void takeBackBook(MemberRecord member, Book book);  // 👈 İsmi takeBackBook yapıyoruz

    void showBooksOfMember(MemberRecord member);
}