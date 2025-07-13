import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.util.HashMap;
import java.util.Map;

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

        // Buttons (without Search)
        Button addButton = new Button("Add Contact");
        Button deleteButton = new Button("Delete Selected");
        Button editButton = new Button("Edit Selected");

        HBox buttons = new HBox(10, addButton, deleteButton, editButton);

        // Filter input fields
        TextField filterName = new TextField();
        filterName.setPromptText("Name");
        TextField filterPhone = new TextField();
        filterPhone.setPromptText("Phone");
        TextField filterEmail = new TextField();
        filterEmail.setPromptText("Email");
        TextField filterAddress = new TextField();
        filterAddress.setPromptText("Address");
        TextField filterBirthDate = new TextField();
        filterBirthDate.setPromptText("Birth Date");
        TextField filterGroup = new TextField();
        filterGroup.setPromptText("Group");
        TextField filterCompany = new TextField();
        filterCompany.setPromptText("Company");
        TextField filterDepartment = new TextField();
        filterDepartment.setPromptText("Department");

        Button filterButton = new Button("Filter");

        HBox filterBox = new HBox(5,
                filterName, filterPhone, filterEmail, filterAddress, filterBirthDate,
                filterGroup, filterCompany, filterDepartment, filterButton);
        filterBox.setPadding(new Insets(10));

        listView.setPrefHeight(300);

        root.getChildren().addAll(countLabel, buttons, filterBox, listView);

        refreshList();

        // Button actions
        addButton.setOnAction(e -> showAddDialog());
        deleteButton.setOnAction(e -> deleteSelected());
        editButton.setOnAction(e -> showEditDialog());

        filterButton.setOnAction(e -> {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("name", filterName.getText());
            criteria.put("phone", filterPhone.getText());
            criteria.put("email", filterEmail.getText());
            criteria.put("address", filterAddress.getText());
            criteria.put("birthdate", filterBirthDate.getText());
            criteria.put("group", filterGroup.getText());
            criteria.put("company", filterCompany.getText());
            criteria.put("department", filterDepartment.getText());

            var filteredContacts = manager.filter(criteria);

            countLabel.setText(filteredContacts.size() + " contacts found");
            listView.getItems().clear();
            for (Contact c : filteredContacts) {
                listView.getItems().add(c.toString());
            }
        });

        stage.setScene(new Scene(root, 900, 500));
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
