public class Contact {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String birthDate;
    private String group;

    public Contact(String fullName, String phoneNumber, String email, String address, String birthDate, String group) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
        this.group = group;
    }

    public String toString() {
        return "Name: " + fullName + ", Phone: " + phoneNumber + ", Email: " + email +
                ", Address: " + address + ", Birth Date: " + birthDate + ", Group: " + group;
    }
}

