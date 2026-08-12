package se.lexicon;

import se.lexicon.controller.ContactController;
import se.lexicon.data.ContactDAO;
import se.lexicon.data.FileContactDAOImpl;
import se.lexicon.view.ContactView;

public class App {

    //Simple start, inits all needed parts of app and starts the controller
    static void main(String[] args) {
        ContactDAO contactDAO = new FileContactDAOImpl();
        ContactView contactView = new ContactView();
        ContactController contactController = new ContactController(contactDAO, contactView);
        contactController.run();
    }

}
