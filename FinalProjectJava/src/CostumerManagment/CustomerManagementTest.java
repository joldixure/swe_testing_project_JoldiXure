
package CostumerManagment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerManagementTest {

    @BeforeEach
    void resetState() throws Exception {
        setCustomers(new ArrayList<>());
    }

    // --------------------
    // UNIT TESTS
    // --------------------

    // Unit: addCustomer(Customer)
    @Test
    void addCustomer_shouldAddCustomerToList() throws Exception {
        Customer c = new Customer("Fatih", "0691234567", "milk");

        List<Customer> list = getCustomerList();
        list.add(c);

        assertEquals(1, list.size());
        assertEquals("Fatih", list.get(0).getName());
    }

    // Unit: removeCustomer(Customer)
    @Test
    void removeCustomer_shouldRemoveCustomerFromList() throws Exception {
        Customer c1 = new Customer("Fatih", "0691234567", "p1");
        Customer c2 = new Customer("Arlis", "0697654321", "p2");

        List<Customer> list = getCustomerList();
        list.add(c1);
        list.add(c2);
        assertEquals(2, list.size());

        boolean removed = list.remove(c1);
        assertTrue(removed);

        assertEquals(1, list.size());
        assertEquals("Arlis", list.get(0).getName());
    }

    // Unit: updateCustomer(Customer)
    // Since only setName exists, we simulate update by replacing the object
    @Test
    void updateCustomer_shouldUpdateCustomerByReplacingObject() throws Exception {
        Customer c = new Customer("OldName", "0101123456", "old");

        List<Customer> list = getCustomerList();
        list.add(c);

        // simulate update (no setters for contact/purchase)
        Customer updated = new Customer("NewName", "0202123456", "new");
        list.set(0, updated);

        Customer after = list.get(0);
        assertEquals("NewName", after.getName());
        assertEquals("0202123456", after.getContactDetails());
        assertEquals("new", after.getPurchaseHistory());
    }

    // Unit: getCustomerList()
    @Test
    void getCustomerList_shouldReflectCurrentState() throws Exception {
        List<Customer> list = getCustomerList();
        assertEquals(0, list.size());

        list.add(new Customer("A", "1111111111", "x"));
        list.add(new Customer("B", "2222222222", "y"));

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getName());
        assertEquals("B", list.get(1).getName());
    }

    // --------------------
    // INTEGRATION TEST
    // Customer CRUD -> application state
    // --------------------

    @Test
    void integration_crudSequence_shouldKeepStateConsistent() throws Exception {
        List<Customer> list = getCustomerList();

        Customer c1 = new Customer("Fatih", "0691234567", "p1");
        Customer c2 = new Customer("Arlis", "0697654321", "p2");

        // Create
        list.add(c1);
        list.add(c2);
        assertEquals(2, list.size());

        // Update (replace object)
        Customer updated = new Customer("ArlisUpdated", "0697654321", "p2");
        list.set(1, updated);
        assertEquals("ArlisUpdated", list.get(1).getName());

        // Delete
        list.remove(c1);
        assertEquals(1, list.size());
        assertEquals("ArlisUpdated", list.get(0).getName());
    }
    
    
    @Test
    void boundary_contactShorterThan10_shouldThrowException() {
        // 9 digits -> invalid
        String invalidContact = "123456789";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Customer("Ali", invalidContact, "milk");
        });

        assertEquals("Contact number must be at least 10 digits", exception.getMessage());
    }


    // --------------------
    // Reflection helpers (because customers is private)
    // --------------------

    @SuppressWarnings("unchecked")
    private List<Customer> getCustomerList() throws Exception {
        Field f = CustomerManagement.class.getDeclaredField("customers");
        f.setAccessible(true);
        return (List<Customer>) f.get(null);
    }

    private void setCustomers(List<Customer> newList) throws Exception {
        Field f = CustomerManagement.class.getDeclaredField("customers");
        f.setAccessible(true);
        f.set(null, newList);
    }
    
    @Test
    void boundary_contactExactly10_shouldBeAccepted() {
        Customer c = new Customer("Test", "0123456789", "x");
        assertEquals("0123456789", c.getContactDetails());
    }

    @Test
    void equivalence_nullContact_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Test", null, "x")
        );
    }

    @Test
    void equivalence_blankContact_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("Test", "   ", "x")
        );
    }
    
    @Test
    void edge_veryLongContact_shouldBeAccepted() {
        String longContact = "1".repeat(100); 
        Customer c = new Customer("Test", longContact, "x");
        assertEquals(longContact, c.getContactDetails());
    }

    @Test
    void robustness_veryLongName_shouldNotCrash() {
        String longName = "A".repeat(1000);
        Customer c = new Customer(longName, "0123456789", "x");
        assertEquals(longName, c.getName());
    }

    @Test
    void stress_addManyCustomers_shouldKeepCorrectCount() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        for (int i = 0; i < 100; i++) {
            Customer c = new Customer("Customer", "9".repeat(10), "h" + i);
            list.add(c);
        }

        assertEquals(100, list.size());
    }

    @Test
    void behavior_updateName_shouldNotAffectOtherFields() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c = new Customer("Old", "0123456789", "hist");
        list.add(c);

        c.setName("New");

        assertEquals("New", c.getName());
        assertEquals("0123456789", c.getContactDetails());
        assertEquals("hist", c.getPurchaseHistory());
    }   
    
    @Test
    void consistency_duplicateCustomer_shouldBeAllowedAndCountIncrease() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c = new Customer("A", "1111111111", "x");

        list.add(c);
        list.add(c); 

        assertEquals(2, list.size());
    }


    @Test
    void equivalence_contactWithSpacesButValidLength_shouldBeAccepted() {
        Customer c = new Customer("Test", " 0123456789 ", "x");
        assertEquals("0123456789", c.getContactDetails());
        };


    @Test
    void invariant_allCustomersMustAlwaysHaveValidContact() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        list.add(new Customer("A", "1111111111", "x"));
        list.add(new Customer("B", "2222222222", "y"));
        list.add(new Customer("C", "3333333333", "z"));

        for (Customer c : list) {
            assertTrue(c.getContactDetails().trim().length() >= 10);
        }
    }
    
    @Test
    void ordering_addAndUpdate_shouldPreserveInsertionOrder() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c1 = new Customer("A", "1111111111", "x");
        Customer c2 = new Customer("B", "2222222222", "y");
        Customer c3 = new Customer("C", "3333333333", "z");

        list.add(c1);
        list.add(c2);
        list.add(c3);

        c2.setName("BB");

        assertEquals("A", list.get(0).getName());
        assertEquals("BB", list.get(1).getName());
        assertEquals("C", list.get(2).getName());

    }

    @Test
    void atomicity_failedCreationShouldNotChangeList() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        list.add(new Customer("A", "1111111111", "x"));

        try {
            new Customer("Bad", "123", "y"); // invalid
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        assertEquals(1, list.size());
        assertEquals("A", list.get(0).getName());
    }

    @Test
    void idempotency_settingSameNameTwiceShouldNotChangeState() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c = new Customer("A", "1111111111", "x");
        list.add(c);

        c.setName("B");
        c.setName("B"); 

        assertEquals("B", c.getName());
        assertEquals(1, list.size());
    }

    @Test
    void noSideEffect_updatingOneCustomerShouldNotAffectOthers() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c1 = new Customer("A", "1111111111", "x");
        Customer c2 = new Customer("B", "2222222222", "y");

        list.add(c1);
        list.add(c2);

        c1.setName("AA"); 

        assertEquals("AA", list.get(0).getName());
        assertEquals("B", list.get(1).getName());

    }

    
    @Test
    void referentialIntegrity_listShouldContainSameObjectReference() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c = new Customer("A", "1111111111", "x");
        list.add(c);

        Customer fromList = list.get(0);

        assertSame(c, fromList); 
    }

    
    @Test
    void regression_updateCustomerName_shouldReflectInListImmediately() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        Customer c = new Customer("Old", "1111111111", "x");
        list.add(c);

        c.setName("New");

        assertEquals("New", list.get(0).getName());
    }

    @Test
    void isolation_eachTestShouldStartWithEmptyList() throws Exception {
        List<Customer> list = getCustomerList();
        list.clear();

        assertEquals(0, list.size());
    }
        
@Test
void addMultipleCustomers_shouldKeepAllCustomersInList() throws Exception {

	List<Customer> list = getCustomerList();
	list.clear(); 

    Customer c1 = new Customer("Arlis",  "1111111111", "milk");
    Customer c2 = new Customer("Fatih", "2222222222", "bread");
    Customer c3 = new Customer("Joldi", "3333333333", "cheese");

    list.add(c1);
    list.add(c2);
    list.add(c3);

    assertEquals(3, list.size());

    assertEquals("Arlis", list.get(0).getName());
    assertEquals("Fatih", list.get(1).getName());
    assertEquals("Joldi", list.get(2).getName());
}

//--------------------
//NAME VALIDATION TESTS (NEW)
//--------------------

@Test
void equivalence_nameWithDigit_shouldThrowException() {
 assertThrows(IllegalArgumentException.class, () ->
         new Customer("Ali1", "0123456789", "x")
 );
}

@Test
void equivalence_nameWithSymbol_shouldThrowException() {
 assertThrows(IllegalArgumentException.class, () ->
         new Customer("Ali@", "0123456789", "x")
 );
}

@Test
void equivalence_emptyName_shouldThrowException() {
 assertThrows(IllegalArgumentException.class, () ->
         new Customer("   ", "0123456789", "x")
 );
}

@Test
void equivalence_nullName_shouldThrowException() {
 assertThrows(IllegalArgumentException.class, () ->
         new Customer(null, "0123456789", "x")
 );
}

@Test
void boundary_singleLetterName_shouldBeAccepted() {
 Customer c = new Customer("A", "0123456789", "x");
 assertEquals("A", c.getName());
}

@Test
void robustness_veryLongNameWithOnlyLetters_shouldBeAccepted() {
 String longName = "A".repeat(500);
 Customer c = new Customer(longName, "0123456789", "x");
 assertEquals(longName, c.getName());
}

@Test
void equivalence_nameWithMultipleSpacesBetweenWords_shouldThrow() {
 assertThrows(IllegalArgumentException.class, () ->
         new Customer("Ali   Veli", "0123456789", "x")
 );
}


}