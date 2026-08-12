package se.lexicon.controller;


import se.lexicon.data.ContactDAO;
import se.lexicon.exception.ContactStorageException;
import se.lexicon.exception.DuplicateContactException;
import se.lexicon.exception.ExceptionHandler;
import se.lexicon.model.Contact;
import se.lexicon.view.ContactView;

public class ContactController {

    private final ContactDAO contactDAO;
    private final ContactView contactView;

    public ContactController(ContactDAO contactDAO, ContactView contactView){
        this.contactDAO = contactDAO;
        this.contactView = contactView;
    }

    /* Keeps user in a while loop to continually go through choices, contacting helper methods whenever valid userinput
    is given, while any exception is passed down to ExceptionHandler
    */
    public void run() {
        boolean active = true;
        while(active){
            contactView.displayMenu();
            String userInput = contactView.getUserInput("Choose an option: ");

            try{
                switch (userInput){
                    case "1" ->
                        controlViewAllContacts();
                    case "2" ->
                        controlAddContact();
                    case "3" ->
                        controlFindContactByName();
                    case "4" ->
                        active = false;
                    default ->
                        contactView.displayError(userInput + " is an invalid option, try again.");
                }
            } catch (Exception e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    //Displays all contacts by calling view to display the list given by contactDAO, model and view do not interact
    // with each other, controller only gathers the information needed and calls view to display it
    public void controlViewAllContacts() throws ContactStorageException {
        contactView.displayContacts(contactDAO.findAll());
    }


    // gathers the needed user input through the user interaction part of view, controller passes this input to model
    // to save new contact, prints successful message if no errors are given, i.e. if name is added to file
    public void controlAddContact() throws ContactStorageException, DuplicateContactException {
        String name = contactView.getUserInput("Enter name of contact: ");
        String phoneNumber = contactView.getUserInput("Enter phone number of contact: ");
        Contact contact = new Contact(name, phoneNumber);
        contactDAO.save(contact);
        contactView.displayMessage("Contact: " + contact + " has been saved successfully");
    }


    //gathers needed input from user interaction in view, controller passes this to model and return result that is
    //then displayed with help from view, still no direct interaction between view and model.
    public void controlFindContactByName() throws ContactStorageException {
        String name = contactView.getUserInput("Enter name of contact: ");
        Contact contact = contactDAO.findByName(name);
        if(contact == null){
            contactView.displayMessage("Contact with name " + name + " could not be found");
        } else {
            contactView.displayMessage(contact.toString());
        }
    }


}
