package CostumerManagment;

import java.io.Serializable;

class CustomerNameAndContact implements Serializable {
    private String name;
    private String contactDetails;
    private String purchaseHistory;

    public void Customer(String name, String contactDetails, String purchaseHistory) {
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

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public void setPurchaseHistory(String purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}
}