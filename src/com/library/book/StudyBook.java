package com.library.book;

import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;

public class StudyBook extends Book {
    private SubjectType subjectType;

    public StudyBook(String title, double price, String edition, LocalDate dateOfPurchase, SubjectType subjectType, Author... authors) {
        super(title, price, edition, dateOfPurchase, authors);
        this.subjectType = subjectType;
    }

    public StudyBook(String title, double price, String edition, LocalDate dateOfPurchase, MemberRecord owner, SubjectType subjectType, Author... authors) {
        super(title, price, edition, dateOfPurchase, owner, authors);
        this.subjectType = subjectType;
    }

    @Override
    public void display() {
        super.display();
        System.out.println(" -> Ders Konusu (Subject Type): " + subjectType);
    }

    @Override
    public String getCategory() {
        return this.subjectType.toString();
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

}
