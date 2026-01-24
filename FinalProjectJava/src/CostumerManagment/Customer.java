
package CostumerManagment;



import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;


    private String name;
    private String contactDetails;
    private String purchaseHistory;

    public Customer(String name, String contactDetails, String purchaseHistory) {

        // Name validation: only letters and single spaces
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name must contain letters only (spaces allowed).");
        }

        // Contact validation: minimum 10 digits
        if (contactDetails == null || contactDetails.trim().length() < 10) {
            // DİKKAT: testlerinle uyumlu olsun diye sondaki noktayı kaldır
            throw new IllegalArgumentException("Contact number must be at least 10 digits");
        }

        this.name = name.trim();
        // Eğer projede bazı yerler boşluklu contact saklıyorsa trim'i kaldırabilirsin.
        this.contactDetails = contactDetails.trim();
        this.purchaseHistory = purchaseHistory;
    }

    private static boolean isValidName(String name) {
        if (name == null) return false;

        String trimmed = name.trim();
        if (trimmed.isEmpty()) return false;

        // Turkish letters included, single spaces between words
        return trimmed.matches("^[A-Za-zÇĞİÖŞÜçğıöşü]+( [A-Za-zÇĞİÖŞÜçğıöşü]+)*$");
    }
    
    public static boolean isNameValid(String name) {
        return isValidName(name);
    }

    public static boolean isContactValid(String contactDetails) {
        return contactDetails != null && contactDetails.trim().length() >= 10;
    }


    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name must contain letters only (spaces allowed).");
        }
        this.name = name.trim();
    }

    public void setContactDetails(String contactDetails) {
        if (contactDetails == null || contactDetails.trim().length() < 10) {
            throw new IllegalArgumentException("Contact number must be at least 10 digits");
        }
        this.contactDetails = contactDetails.trim();
    }

    public void setPurchaseHistory(String purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }
}
