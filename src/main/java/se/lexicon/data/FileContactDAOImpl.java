package se.lexicon.data;

import se.lexicon.exception.ContactStorageException;
import se.lexicon.exception.DuplicateContactException;
import se.lexicon.model.Contact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileContactDAOImpl implements ContactDAO {

    //default filepath
    private static final Path FILE_PATH = Paths.get("contacts.txt");

    private Path filePath;

    public FileContactDAOImpl() {
        this.filePath = FILE_PATH;
    }

    //for possible testing purposes, if filepath should change
    public FileContactDAOImpl(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Contact> findAll() throws ContactStorageException {

        //throws exception if file does not exist
        if (Files.notExists(Paths.get(filePath.toString()))) {
            throw new ContactStorageException("Contact list not found");
        }

        List<Contact> contacts = new ArrayList<>();

        //try-with-resources for safety, adds the read line to the contact list if its not empty, throws exception
        //if any IO-related errors occur, then returns list of contacts
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
            String res;

            while ((res = reader.readLine()) != null) {
                if (!res.isBlank()) {
                    contacts.add(parseLine(res));
                }
            }
        } catch (IOException e) {
            throw new ContactStorageException("Failed to read contacts from file " + filePath, e);
        }
        return contacts;
    }

    @Override
    public void save(Contact contact) throws ContactStorageException, DuplicateContactException {
        //quick nullcheck
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        //checks if name already exists, since find function operates by name, go for easy check that only handles
        //names and not complete duplicates (same name + phone nr)
        if (findByName(contact.getName()) != null) {
            throw new DuplicateContactException("Contact name " + contact + " already exists");
        }

        //try-with-resources to write to file, helper methods used for extendability
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)){
            writer.write(toLine(contact));
            writer.newLine();
        } catch (IOException e) {
            throw new ContactStorageException("Failed to save contact to " + filePath, e);
        }
    }

    //fast namecheck, null return could be spooky but used to make sure no contact is found when saving
    public Contact findByName(String name) throws ContactStorageException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        for (Contact contact : findAll()) {
            if (name.equals(contact.getName())) {
                return contact;
            }
        }
        return null;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    //helper method to split and parse line when using findAll(), splits name and phonenumber with comma
    //and checks for possible invalid formats, try-catch block also makes sure to trim any whitespaces to
    //avoid any user mistakes during input
    private Contact parseLine(String line) throws ContactStorageException {
        String[] split = line.split(",");
        if (split.length < 2) {
            throw new ContactStorageException("Invalid line format found: " + filePath + ": " + line);
        }
        try {
            return new Contact(split[0].trim(), split[1].trim());
        } catch (IllegalArgumentException e) {
            throw new ContactStorageException("Invalid contact found: " + filePath + ": " + line, e);
        }
    }


    //helper method to join and parse Contact when using save(), joins a contact to get correct format to file when
    //saving new contact
    private String toLine(Contact contact) {
        return String.join(",", contact.getName(), contact.getPhoneNumber());
    }
}
