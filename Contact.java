public class Contact {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String birthDate;
    private String group;
    private Company company;  

    public Contact(String fullName, String phoneNumber, String email, String address,
                   String birthDate, String group, Company company) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
        this.group = group;
        this.company = company;
    }

   
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getBirthDate() { return birthDate; }
    public String getGroup() { return group; }
    public Company getCompany() { return company; }

  
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setGroup(String group) { this.group = group; }
    public void setCompany(Company company) { this.company = company; }

    @Override
    public String toString() {
        return "Name: " + fullName +
               " | Phone: " + phoneNumber +
               " | Email: " + email +
               " | Address: " + address +
               " | Birth Date: " + birthDate +
               " | Group: " + group +
               " | Company: " + company;
    }

    Object getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
