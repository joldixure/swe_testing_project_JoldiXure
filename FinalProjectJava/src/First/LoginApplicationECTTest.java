package First;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class LoginApplicationECTTest {

    // Utility to invoke the private validateLogin method
    private boolean validate(LoginApplication app, String username, String password) throws Exception {
        Method m = LoginApplication.class.getDeclaredMethod("validateLogin", String.class, String.class);
        m.setAccessible(true);
        return (boolean) m.invoke(app, username, password);
    }

    @Test
    void validCredentials_shouldReturnTrue() throws Exception {
        LoginApplication app = new LoginApplication();

        // These MUST exist in credentials.txt
        assertTrue(validate(app, "user1", "Pass@123"));
        assertTrue(validate(app, "admin", "Admin#2024"));
        assertTrue(validate(app, "maria", "Maria$789"));
    }

    @Test
    void invalidEquivalenceClasses_shouldReturnFalse() throws Exception {
        LoginApplication app = new LoginApplication();

        // Username invalid
        assertFalse(validate(app, null, "Pass@123"));
        assertFalse(validate(app, "   ", "Pass@123"));

        // Password invalid (null / too short / too long)
        assertFalse(validate(app, "user1", null));
        assertFalse(validate(app, "user1", "P@1")); // too short
        assertFalse(validate(app, "user1", "Pass@123Pass@123Pass@123")); // too long

        // Missing digit / missing special char
        assertFalse(validate(app, "user1", "Pass@abcd"));  // no digit
        assertFalse(validate(app, "user1", "Password1"));  // no special char

        // Wrong credentials (even if format valid)
        assertFalse(validate(app, "user1", "Wrong@123"));
        assertFalse(validate(app, "unknown", "Pass@123"));
    }
}

