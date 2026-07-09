package com.library.management;

import com.library.book.Book;
import com.library.member.MemberRecord;

public interface LibraryOperations {
    void purchaseBook(Book book, Object user);

    void lendBook(MemberRecord member, Book book);

    boolean takeBackBook(MemberRecord member, Book book);

    void showBooksOfMember(MemberRecord member);
}