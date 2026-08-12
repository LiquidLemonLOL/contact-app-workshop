package se.lexicon.exception;

public class ExceptionHandler {

    public static void handle(Exception e) {

        switch (e) {
            case IllegalArgumentException iae ->
                System.out.println(iae.getMessage());

            case ContactStorageException cse ->
                System.out.println(cse.getMessage());

            case DuplicateContactException dce ->
                System.out.println(dce.getMessage());

            default -> {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }

    }

}
