
package First;

import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private Path credentialsPath =
            Paths.get(System.getProperty("user.dir"), "credentials.txt");

    private LoginService service;

    @BeforeEach
    void setUp() throws Exception {
        credentialsPath = Files.createTempFile("credentials", ".txt");
        Files.writeString(credentialsPath,
                "admin,1234\nuser,pass\n",
                StandardOpenOption.TRUNCATE_EXISTING
        );
        service = new LoginService(credentialsPath.toString());
    }


    @AfterEach
    void tearDown() throws Exception {
        // Delete credentials file after each test
        Files.deleteIfExists(credentialsPath);
    }

    @Test
    void validateLogin_returnsTrue_forCorrectCredentials() {
        assertTrue(service.validateLogin("admin", "1234"));
        assertTrue(service.validateLogin("user", "pass"));
    }

    @Test
    void validateLogin_returnsFalse_forWrongPassword() {
        assertFalse(service.validateLogin("admin", "wrong"));
    }

    @Test
    void validateLogin_returnsFalse_forUnknownUser() {
        assertFalse(service.validateLogin("noone", "1234"));
    }

    @Test
    void validateLogin_returnsFalse_whenFileHasBadLines() throws Exception {
        // Overwrite file with bad and good lines
        Files.writeString(credentialsPath,
                "badline\nadmin,1234\nx,y,z\n",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        assertTrue(service.validateLogin("admin", "1234"));
        assertFalse(service.validateLogin("x", "y"));
    }
    
    @Test
    void validateLogin_returnsFalse_whenFileIsEmpty() throws Exception {
        Files.writeString(credentialsPath, "",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        assertFalse(service.validateLogin("admin", "1234"));
    }

    @Test
    void validateLogin_returnsFalse_whenCredentialsFileDoesNotExist() throws Exception {
        Files.deleteIfExists(credentialsPath);

        // Service reads file; IOException happens; method should return false
        assertFalse(service.validateLogin("admin", "1234"));
    }

    @Test
    void validateLogin_isCaseSensitive() {
        // In file: admin,1234
        assertFalse(service.validateLogin("Admin", "1234"));
        assertFalse(service.validateLogin("admin", "1234 ")); // space makes it different
    }

    @Test
    void validateLogin_doesNotTrimWhitespace_soExactMatchRequired() throws Exception {
        // Add spaces around username and password in file
        Files.writeString(credentialsPath,
                " admin , 1234 \n",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        // Because your implementation does NOT trim, these should be false
        assertFalse(service.validateLogin("admin", "1234"));

        // Exact match with spaces would be required
        assertTrue(service.validateLogin(" admin ", " 1234 "));
    }

    @Test
    void validateLogin_returnsTrue_ifUserAppearsLaterInFile() throws Exception {
        Files.writeString(credentialsPath,
                "someone,1111\nadmin,1234\n",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        assertTrue(service.validateLogin("admin", "1234"));
    }

    @Test
    void validateLogin_returnsTrue_withDuplicateUserIfAnyLineMatches() throws Exception {
        Files.writeString(credentialsPath,
                "admin,wrong\nadmin,1234\n",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        assertTrue(service.validateLogin("admin", "1234"));
    }
    
    
}
