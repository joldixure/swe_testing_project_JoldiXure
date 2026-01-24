package Sales;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SalesReportManager {
    public static class SalesRecord {
        private String itemName;
        int quantity;
        double totalCost;
        private Date saleDate;

        public SalesRecord(String itemName, int quantity, double totalCost, Date saleDate) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.totalCost = totalCost;
            this.saleDate = saleDate;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public Date getSaleDate() {
            return saleDate;
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "Item: " + itemName +
                    ", Quantity: " + quantity +
                    ", Total Cost: $" + totalCost +
                    ", Sale Date: " + dateFormat.format(saleDate);
        }
    }

    private List<SalesRecord> salesRecords = new ArrayList<>();

    public double calculateTotalSalesForWeek() {

        final double MAX_WEEKLY_TOTAL = 10_000.0;
        double total = 0.0;

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        for (SalesRecord record : salesRecords) {

            if (record == null) {
                continue;
            }

            Date saleDate = record.getSaleDate();

            if (saleDate.after(startOfWeek) && saleDate.before(now)) {

                double cost = record.getTotalCost();

                if (cost > 0 && cost <= MAX_WEEKLY_TOTAL) {
                    total += cost;
                }

                if (total >= MAX_WEEKLY_TOTAL) {
                    total = MAX_WEEKLY_TOTAL;
                    break;
                }
            }
        }

        return total;
    }


    public double calculateTotalSalesForMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date startOfMonth = calendar.getTime();
        Date endOfMonth = new Date();

        return calculateTotalSalesInRange(startOfMonth, endOfMonth);
    }

    private double calculateTotalSalesInRange(Date startDate, Date endDate) {
        return salesRecords.stream()
                .filter(record -> record.getSaleDate().after(startDate) && record.getSaleDate().before(endDate))
                .mapToDouble(SalesRecord::getTotalCost)
                .sum();
    }

    public void addSalesRecord(String itemName, int quantity, double totalCost, Date saleDate) {
        SalesRecord salesRecord = new SalesRecord(itemName, quantity, totalCost, saleDate);
        salesRecords.add(salesRecord);
    }

    public List<SalesRecord> getSalesRecords() {
        return salesRecords;
    }

    public void saveSalesRecordsToFile(File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (SalesRecord record : salesRecords) {
                writer.println(record.toString());
            }
            System.out.println("Sales records saved to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSalesRecordsFromFile(String filePath) {
        salesRecords.clear(); // Clear the current list before loading new data
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String itemName = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    double totalCost = Double.parseDouble(parts[2]);
                    Date saleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parts[3]);

                    addSalesRecord(itemName, quantity, totalCost, saleDate);
                }
            }
            System.out.println("Sales records loaded from file: " + filePath);
        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }
}

