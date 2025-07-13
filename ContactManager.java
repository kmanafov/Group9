import java.util.*;
import java.util.stream.Collectors;

public class ContactManager {
    private List<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    public Contact searchByName(String name) {
        for (Contact c : contacts) {
            if (c.getFullName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    public List<Contact> search(String key, String value) {
        return contacts.stream().filter(c -> {
            switch (key.toLowerCase()) {
                case "name": return c.getFullName().equalsIgnoreCase(value);
                case "phone": return c.getPhoneNumber().equalsIgnoreCase(value);
                case "email": return c.getEmail().equalsIgnoreCase(value);
                case "address": return c.getAddress().equalsIgnoreCase(value);
                case "birthdate": return c.getBirthDate().equalsIgnoreCase(value);
                case "group": return c.getGroup().equalsIgnoreCase(value);
                case "company": return c.getCompany().getName().equalsIgnoreCase(value);
                case "department": return c.getCompany().getDepartment().equalsIgnoreCase(value);
                default: return false;
            }
        }).collect(Collectors.toList());
    }

    public boolean deleteContact(Contact contact) {
        return contacts.remove(contact);
    }

    public void updateContact(Contact c, Scanner scanner) {
        System.out.print("New phone: ");
        c.setPhoneNumber(scanner.nextLine());
        System.out.print("New email: ");
        c.setEmail(scanner.nextLine());
        System.out.print("New address: ");
        c.setAddress(scanner.nextLine());
        System.out.print("New birth date: ");
        c.setBirthDate(scanner.nextLine());
        System.out.print("New group: ");
        c.setGroup(scanner.nextLine());
        System.out.print("New company name: ");
        String cname = scanner.nextLine();
        System.out.print("New department: ");
        String dept = scanner.nextLine();
        c.setCompany(new Company(cname, dept));
    }

    public void sortBy(String key) {
        Comparator<Contact> comparator = switch (key.toLowerCase()) {
            case "name" -> Comparator.comparing(Contact::getFullName);
            case "phone" -> Comparator.comparing(Contact::getPhoneNumber);
            case "email" -> Comparator.comparing(Contact::getEmail);
            case "address" -> Comparator.comparing(Contact::getAddress);
            case "birthdate" -> Comparator.comparing(Contact::getBirthDate);
            case "group" -> Comparator.comparing(Contact::getGroup);
            case "company" -> Comparator.comparing(c -> c.getCompany().getName());
            case "department" -> Comparator.comparing(c -> c.getCompany().getDepartment());
            default -> null;
        };
        if (comparator != null) contacts.sort(comparator);
    }

    public List<Contact> filter(Map<String, String> criteria) {
        return contacts.stream().filter(c -> {
            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (val.isEmpty()) continue;
                switch (key.toLowerCase()) {
                    case "name": if (!c.getFullName().equalsIgnoreCase(val)) return false; break;
                    case "phone": if (!c.getPhoneNumber().equalsIgnoreCase(val)) return false; break;
                    case "email": if (!c.getEmail().equalsIgnoreCase(val)) return false; break;
                    case "address": if (!c.getAddress().equalsIgnoreCase(val)) return false; break;
                    case "birthdate": if (!c.getBirthDate().equalsIgnoreCase(val)) return false; break;
                    case "group": if (!c.getGroup().equalsIgnoreCase(val)) return false; break;
                    case "company": if (!c.getCompany().getName().equalsIgnoreCase(val)) return false; break;
                    case "department": if (!c.getCompany().getDepartment().equalsIgnoreCase(val)) return false; break;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }
}
