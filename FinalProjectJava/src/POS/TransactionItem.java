package POS;

import inventory.Product;

public class TransactionItem {
    private Product item;
    private int quantity;

    public TransactionItem(Product item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Product getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return item.getPrice() * quantity;
    }

	public Object getDate() {
		// TODO Auto-generated method stub
		return null;
	}
}
