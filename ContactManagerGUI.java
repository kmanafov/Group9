import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class ContactManagerGUI extends Application {

    private ContactManager manager = new ContactManager();
    private ListView<String> listView = new ListView<>();
    private Label countLabel = new Label("0 total contacts");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Contact Manager");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button addButton = new Button("Add Contact");
        Button searchButton = new Button("Search");
        Button deleteButton = new Button("Delete Selected");
        Button editButton = new Button("Edit Selected");

        HBox buttons = new HBox(10, addButton, searchButton, deleteButton, editButton);

        listView.setPrefHeight(300);

        root.getChildren().addAll(countLabel, buttons, listView);

        refreshList();

        addButton.setOnAction(e -> showAddDialog());
        searchButton.setOnAction(e -> showSearchDialog());
        deleteButton.setOnAction(e -> deleteSelected());
        editButton.setOnAction(e -> showEditDialog());

        stage.setScene(new Scene(root, 650, 450));
        stage.show();
    }

    private void refreshList() {
        countLabel.setText(manager.getAllContacts().size() + " total contacts");
        listView.getItems().clear();
        for (Contact c : manager.getAllContacts()) {
            listView.getItems().add(c.toString());
        }
    }

    private void showAddDialog() {
        Dialog<Contact> dialog = new Dialog<>();
        dialog.setTitle("Add Contact");

        GridPane grid = getContactForm();

        TextField name = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();
        TextField address = new TextField();
        TextField birthDate = new TextField();
        TextField group = new TextField();
        TextField companyName = new TextField();
        TextField department = new TextField();

        grid.addRow(0, new Label("Name:"), name);
        grid.addRow(1, new Label("Phone (+994XXXXXXXXX):"), phone);
        grid.addRow(2, new Label("Email:"), email);
        grid.addRow(3, new Label("Address:"), address);
        grid.addRow(4, new Label("Birth Date:"), birthDate);
        grid.addRow(5, new Label("Group:"), group);
        grid.addRow(6, new Label("Company Name:"), companyName);
        grid.addRow(7, new Label("Department:"), department);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
           
            if (!validateContactForm(name, phone, email, birthDate)) {
                event.consume(); 
            }
        });

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return new Contact(
                    name.getText().trim(),
                    phone.getText().trim(),
                    email.getText().trim(),
                    address.getText().trim(),
                    birthDate.getText().trim(),
                    group.getText().trim(),
                    new Company(companyName.getText().trim(), department.getText().trim())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(contact -> {
            manager.addContact(contact);
            refreshList();
        });
    }

    private void showSearchDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search by Name");
        dialog.setHeaderText("Enter full name (e.g., Mammadov_Ali):");
        dialog.showAndWait().ifPresent(name -> {
            Contact found = manager.searchByName(name.trim());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (found != null) {
                alert.setTitle("Contact Found");
                alert.setHeaderText(null);
                alert.setContentText(found.toString());
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setTitle("Not Found");
                alert.setContentText("No contact with that name.");
            }
            alert.showAndWait();
        });
    }

    private void deleteSelected() {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a contact to delete.");
            return;
        }

        Contact c = manager.getAllContacts().get(index);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete contact?");
        confirm.setContentText(c.toString());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                manager.deleteContact(c);
                refreshList();
            }
        });
    }

    private void showEditDialog() {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a contact to edit.");
            return;
        }

        Contact old = manager.getAllContacts().get(index);

        Dialog<Contact> dialog = new Dialog<>();
        dialog.setTitle("Edit Contact");

        GridPane grid = getContactForm();

        TextField name = new TextField(old.getFullName());
        TextField phone = new TextField(old.getPhoneNumber());
        TextField email = new TextField(old.getEmail());
        TextField address = new TextField(old.getAddress());
        TextField birthDate = new TextField(old.getBirthDate());
        TextField group = new TextField(old.getGroup());
        TextField companyName = new TextField(old.getCompany().getName());
        TextField department = new TextField(old.getCompany().getDepartment());

        grid.addRow(0, new Label("Name:"), name);
        grid.addRow(1, new Label("Phone (+994XXXXXXXXX):"), phone);
        grid.addRow(2, new Label("Email:"), email);
        grid.addRow(3, new Label("Address:"), address);
        grid.addRow(4, new Label("Birth Date:"), birthDate);
        grid.addRow(5, new Label("Group:"), group);
        grid.addRow(6, new Label("Company Name:"), companyName);
        grid.addRow(7, new Label("Department:"), department);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validateContactForm(name, phone, email, birthDate)) {
                event.consume();
            }
        });

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return new Contact(
                    name.getText().trim(),
                    phone.getText().trim(),
                    email.getText().trim(),
                    address.getText().trim(),
                    birthDate.getText().trim(),
                    group.getText().trim(),
                    new Company(companyName.getText().trim(), department.getText().trim())
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newContact -> {
            manager.getAllContacts().set(index, newContact);
            refreshList();
        });
    }

    private GridPane getContactForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        return grid;
    }

    private boolean validateContactForm(TextField name, TextField phone, TextField email, TextField birthDate) {
        String n = name.getText().trim();
        String p = phone.getText().trim();
        String e = email.getText().trim();
        String b = birthDate.getText().trim();

        if (n.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return false;
        }

        if (!p.matches("\\+994\\d{9}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone must be in format +994XXXXXXXXX.");
            return false;
        }

        if (!e.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        if (b.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Birth Date cannot be empty.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
