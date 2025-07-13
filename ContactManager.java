import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ContactManager {
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- CONTACT MANAGEMENT APP ---");
            System.out.println("1. Add new contact");
            System.out.println("2. List all contacts");
            System.out.println("0. Exit");
            try {
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1 -> addContact();
                    case 2 -> listContacts();
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid option! Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                scanner.nextLine();
                choice = -1;
            }
        } while (choice != 0);
    }

    private static void addContact() {
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        String phoneNumber;
        while (true) {
            System.out.print("Enter phone number: ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d+")) break;
            else System.out.println("Phone number must be numeric!");
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter birth date (dd/mm/yyyy): ");
        String birthDate = scanner.nextLine();

        System.out.print("Enter group (Family/Friends/Work): ");
        String group = scanner.nextLine();

        Contact newContact = new Contact(fullName, phoneNumber, email, address, birthDate, group);
        contacts.add(newContact);
        System.out.println("Contact added successfully!");
    }

    private static void listContacts() {
        System.out.println("\n--- CONTACT LIST ---");
        System.out.println(contacts.size() + " total contacts\n");
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            for (Contact c : contacts) {
                System.out.println(c);
            }
        }
    }
}
