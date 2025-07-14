import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager manager = new ContactManager();

        while (true) {
            System.out.println("\n===== Contact Management System =====");
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Sort Contacts");
            System.out.println("5. Filter Contacts ");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Phone (+994XXXXXXXXX): ");
                    String phone = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Address: ");
                    String address = scanner.nextLine();
                    System.out.print("Birth Date: ");
                    String birthDate = scanner.nextLine();
                    System.out.print("Group: ");
                    String group = scanner.nextLine();
                    System.out.print("Company name: ");
                    String cname = scanner.nextLine();
                    System.out.print("Department: ");
                    String dept = scanner.nextLine();

                    manager.addContact(new Contact(name, phone, email, address, birthDate, group, new Company(cname, dept)));
                }

                case 2 -> {
                    List<Contact> contacts = manager.getAllContacts();
                    System.out.println("Total: " + contacts.size());
                    contacts.forEach(System.out::println);
                }

                case 3 -> {
                    System.out.print("Search by name: ");
                    String name = scanner.nextLine();
                    Contact c = manager.searchByName(name);
                    if (c != null) {
                        System.out.println(c);
                        System.out.print("Update? (y/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("y")) {
                            manager.updateContact(c, scanner);
                        }
                        System.out.print("Delete? (y/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("y")) {
                            manager.deleteContact(c);
                        }
                    } else {
                        System.out.println("Contact not found.");
                    }
                }

                case 4 -> {
                    System.out.print("Sort by (name/phone/email/address/birthdate/group/company/department): ");
                    String key = scanner.nextLine();
                    manager.sortBy(key);
                    System.out.println("Sorted.");
                }

                case 5 -> {
                    Map<String, String> filters = new HashMap<>();
                    System.out.print("Name (optional): "); filters.put("name", scanner.nextLine());
                    System.out.print("Phone (optional): "); filters.put("phone", scanner.nextLine());
                    System.out.print("Email (optional): "); filters.put("email", scanner.nextLine());
                    System.out.print("Address (optional): "); filters.put("address", scanner.nextLine());
                    System.out.print("Birth Date (optional): "); filters.put("birthdate", scanner.nextLine());
                    System.out.print("Group (optional): "); filters.put("group", scanner.nextLine());
                    System.out.print("Company (optional): "); filters.put("company", scanner.nextLine());
                    System.out.print("Department (optional): "); filters.put("department", scanner.nextLine());

                    List<Contact> results = manager.filter(filters);
                    if (results.isEmpty()) {
                        System.out.println("No contacts matched the filter criteria.");
                    } else {
                        results.forEach(System.out::println);
                    }
                }

                case 6 -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }

                default -> System.out.println("Invalid option!");
            }
        }
    }
}
