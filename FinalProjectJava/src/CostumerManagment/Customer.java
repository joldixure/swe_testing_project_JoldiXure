package CostumerManagment;

import java.io.Serializable;

public class Customer implements Serializable {
    private String name;
    private String contactDetails;
    private String purchaseHistory;

    public Customer(String name, String contactDetails, String purchaseHistory) {
        this.name = name;
        this.contactDetails = contactDetails;
        this.purchaseHistory = purchaseHistory;
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
        this.name = name;
    }
}
