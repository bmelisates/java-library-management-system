package com.library.person;

public class Reader extends Person {
    public Reader(String name) {
        super(name);
    }

    @Override
    public void whoYouAre() {
        System.out.println("Okur ismi: " + getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
