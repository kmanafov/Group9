import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager manager = new ContactManager();
        manager.loadFromFile();

        int choice;
        do {
            System.out.println("\n===== Contact Management System =====");
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Sort Contacts");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter full name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter phone number: ");
                        String phone = scanner.nextLine();

                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();

                        System.out.print("Enter birth date (dd/mm/yyyy): ");
                        String birthDate = scanner.nextLine();

                        System.out.print("Enter group: ");
                        String group = scanner.nextLine();

                        System.out.print("Enter company name: ");
                        String compName = scanner.nextLine();

                        System.out.print("Enter department: ");
                        String dept = scanner.nextLine();

                        Company company = new Company(compName, dept);
                        Contact contact = new Contact(name, phone, email, address, birthDate, group, company);
                        manager.addContact(contact);
                        manager.saveToFile();
                        System.out.println("Contact added.");
                    }

                    case 2 -> {
                        System.out.println("\nTotal: " + manager.getAllContacts().size() + " contacts.");
                        for (Contact c : manager.getAllContacts()) {
                            System.out.println(c);
                        }
                    }

                    case 3 -> {
                        System.out.print("Search by name: ");
                        String name = scanner.nextLine();
                        Contact found = manager.searchByName(name);
                        if (found != null) {
                            System.out.println("Found: " + found);
                            System.out.println("Do you want to (1) Edit or (2) Delete? (0 to cancel)");
                            int action = scanner.nextInt();
                            scanner.nextLine();
                            switch (action) {
                                case 1 -> {
                                    manager.updateContact(found, scanner);
                                    manager.saveToFile();
                                    System.out.println("Contact updated.");
                                }
                                case 2 -> {
                                    manager.deleteContact(found);
                                    manager.saveToFile();
                                    System.out.println("Contact deleted.");
                                }
                                default -> System.out.println("No changes made.");
                            }
                        } else {
                            System.out.println("Contact not found.");
                        }
                    }

                    case 4 -> {
                        System.out.print("Sort by (name/phone/email/address/group/company): ");
                        String key = scanner.nextLine();
                        manager.sortBy(key);
                        System.out.println("Contacts sorted by " + key + ".");
                    }

                    case 0 -> {
                        manager.saveToFile();
                        System.out.println("Goodbye!");
                    }

                    default -> System.out.println("Invalid choice.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine(); 
                choice = -1;
            }

        } while (choice != 0);
    }
}
