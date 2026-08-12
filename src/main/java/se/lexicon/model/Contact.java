package se.lexicon.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {

    //regex for basic formatted Swedish phone numbers (1234-567890), could be extended to work with way more formats
    public static final Pattern VALID_PHONE_REGEX =
            Pattern.compile("^0\\d{1,3}-?\\d{5,8}$");

    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        setName(name);
        setPhoneNumber(phoneNumber);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number is not valid: " + phoneNumber);
        }
        this.phoneNumber = phoneNumber;
    }


    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        return VALID_PHONE_REGEX.matcher(phoneNumber).matches();
    }

    @Override
    public String toString() {
        return "Contact: " + name + " - "+ phoneNumber;
    }
}
