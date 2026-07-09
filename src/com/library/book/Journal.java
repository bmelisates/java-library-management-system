package com.library.book;

import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;

public class Journal extends Book {
    private FieldOfStudy fieldOfStudy;
    private String publisherName;

    public Journal(String title, double price, String edition, LocalDate dateOfPurchase, FieldOfStudy fieldOfStudy, String publisherName, Author... authors) {
        super(title, price, edition, dateOfPurchase, authors);
        this.fieldOfStudy = fieldOfStudy;
        this.publisherName = publisherName;
    }

    public Journal(String title, double price, String edition, LocalDate dateOfPurchase, MemberRecord owner, FieldOfStudy fieldOfStudy, String publisherName, Author... authors) {
        super(title, price, edition, dateOfPurchase, owner, authors);
        this.fieldOfStudy = fieldOfStudy;
        this.publisherName = publisherName;
    }


    @Override
    public void display() {
        super.display();
        System.out.println(" -> Bilim Dalı (Field of Study): " + fieldOfStudy);
    }

    @Override
    public String getCategory() {
        return this.fieldOfStudy.toString();
    }

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
