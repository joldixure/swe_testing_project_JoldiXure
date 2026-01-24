
package First;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginService {

    private final String credentialsFile;

    // 👇 THIS CONSTRUCTOR IS REQUIRED
    public LoginService(String credentialsFile) {
        this.credentialsFile = credentialsFile;
    }

    public boolean validateLogin(String enteredUsername, String enteredPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2
                        && parts[0].equals(enteredUsername)
                        && parts[1].equals(enteredPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
         
        }
        return false;
    }
}
