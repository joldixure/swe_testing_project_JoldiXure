package First;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginApplication extends Application {

    private static final String CREDENTIALS_FILE = "credentials.txt";
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login Page");

        GridPane grid = createLoginGrid();
        addBackground(grid);

        loginButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            if (validateLogin(enteredUsername, enteredPassword)) {
                openNextPage();
            } else {
                showAlert("Login Error", "Invalid username or password. Please try again.");
            }
        });

        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> showAddUserDialog());

        grid.add(addUserButton, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        this.primaryStage.setScene(scene);

        this.primaryStage.show();
    }

    private GridPane createLoginGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        loginButton = new Button("Login");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        return grid;
    }

    private void addBackground(GridPane grid) {
        Image backgroundImage = new Image("file:/C:/Users/endri/OneDrive/Desktop/img.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        grid.setBackground(new Background(background));
    }

    private boolean validateLogin(String enteredUsername, String enteredPassword) {

        final int MIN_PASSWORD_LENGTH = 6;
        final int MAX_PASSWORD_LENGTH = 20;
        final String SPECIAL_CHARS = "!@#$%^&*()";

        // ---- Added validation (for ECT testability) ----
        if (enteredUsername == null || enteredUsername.trim().isEmpty()) {
            return false;
        }

        if (enteredPassword == null) {
            return false;
        }

        if (enteredPassword.length() < MIN_PASSWORD_LENGTH ||
            enteredPassword.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : enteredPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (SPECIAL_CHARS.indexOf(c) >= 0) {
                hasSpecialChar = true;
            }
        }

        if (!hasDigit || !hasSpecialChar) {
            return false;
        }
        // -----------------------------------------------

        try (BufferedReader br = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 &&
                    parts[0].equals(enteredUsername) &&
                    parts[1].equals(enteredPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



    private void openNextPage() {
        MainPage mainPage = new MainPage();
        mainPage.showMainPage(primaryStage, usernameField.getText());  
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void initializeCredentialsFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.write("admin,adminpass");
            writer.newLine();
            writer.write("user,userpass");
            writer.newLine();
            System.out.println("Credentials file initialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        Path credentialsPath = Paths.get(CREDENTIALS_FILE);
        if (!Files.exists(credentialsPath)) {
            initializeCredentialsFile();
        }
    }

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    private void showAddUserDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.setHeaderText("Enter new username and password:");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField newUsernameField = new TextField();
        PasswordField newPasswordField = new PasswordField();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(newUsernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(newPasswordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(newUsernameField.getText(), newPasswordField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
                writer.write(pair.getKey() + "," + pair.getValue());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
