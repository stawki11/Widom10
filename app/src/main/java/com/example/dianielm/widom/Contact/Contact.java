package com.example.dianielm.widom.Contact;

/**
 * Created by Daniel on 16/01/2017.
 *
 * To jest klasa Data Object. Musi mieć pusty konstruktor.
 * Można tworzyć, przekazywać dane i korzystać z innych konstruktorów.
 */

public class Contact {

    String nameEditTextContact, numberEditTextContact;


    public Contact() {
    }

    public String getNameEditTextContact() {
        return nameEditTextContact;
    }

    public void setNameEditTextContact(String nameTxtContact) {
        this.nameEditTextContact = nameTxtContact;
    }

    public String getNumberEditTextContact() {
        return numberEditTextContact;
    }

    public void setNumberEditTextContact(String numberEditTextContact) {
        this.numberEditTextContact = numberEditTextContact;
    }
}