package com.library.member;

import com.library.book.Book;
import com.library.person.Reader;

import java.util.Set;

public class StudentMember extends MemberRecord {
    private static int studentCount = 0;
    private static final int STUDENT_LIMIT = 5;

    // 1. CONSTRUCTOR : Sıfırdan gelen yeni öğrenci üye
    public StudentMember(Reader reader, String address, String phoneNo) {
        // ID'yi burada üretip "STU-1" diye üst sınıfa paslıyoruz
        super("STU-" + (++studentCount), MemberType.STUDENT, STUDENT_LIMIT, reader, address, phoneNo);
    }

    // 2. CONSTRUCTOR : Eski sistemden elindeki kitaplarla gelen öğrenci üye
    public StudentMember(Reader reader, String address, String phoneNo, Set<Book> migrationBooks) {
        // ID'yi üretip, kitap listesiyle birlikte üst sınıfa paslıyoruz
        super("STU-" + (++studentCount), MemberType.STUDENT, STUDENT_LIMIT, reader, address, phoneNo, migrationBooks);
    }
}
