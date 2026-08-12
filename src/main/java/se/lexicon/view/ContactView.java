package se.lexicon.view;

import se.lexicon.model.Contact;

import java.util.List;

public class ContactView {

    public String getUserInput(String prompt) {
        return IO.readln(prompt);
    }

    public void displayMenu() {
        IO.println("======== CONTACT APP MENU ========");
        IO.println("1 - Display all contacts");
        IO.println("2 - Add new contact");
        IO.println("3 - Find contact by name");
        IO.println("4 - Exit application");
        IO.println("==================================");
    }

    public void displayContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            IO.println("No contacts found");
            return;
        }
        for (Contact contact : contacts) {
            IO.println(contact.toString());
        }
    }

    public void displayMessage(String message){
        IO.println(message);
    }

    public void displayError(String message){
        IO.println("Error: " + message);
    }


}
