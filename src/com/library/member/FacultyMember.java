package com.library.member;

import com.library.book.Book;
import com.library.person.Reader;

import java.util.List;

public class FacultyMember extends MemberRecord {
    private static int facultyCount = 0;
    private static final int FACULTY_LIMIT = 10;

    // SENARYO A: Sıfırdan gelen yeni akademisyen üye
    public FacultyMember(Reader reader, String address, String phoneNo) {
        super("FAC-" + (++facultyCount), MemberType.FACULTY, FACULTY_LIMIT, reader, address, phoneNo);
    }

    // SENARYO B: Eski sistemden elindeki kitaplarla gelen akademisyen üye
    public FacultyMember(Reader reader, String address, String phoneNo, List<Book> migrationBooks) {
        super("FAC-" + (++facultyCount), MemberType.FACULTY, FACULTY_LIMIT, reader, address, phoneNo, migrationBooks);
    }
}