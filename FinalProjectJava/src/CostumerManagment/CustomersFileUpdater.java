package CostumerManagment;

import java.io.*;
import java.util.*;

public class CustomersFileUpdater {

    public static void main(String[] args) {
        File csv = new File("customers_readable.csv");
        File out = new File("customers.txt");

        List<Customer> customers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (first) {
                    first = false;
                    if (line.toLowerCase().startsWith("name,")) continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                String name = parts[0].trim();
                String contact = parts[1].trim();
                String history = parts[2].trim();

                customers.add(new Customer(name, contact, history));
            }

        } catch (Exception e) {
            System.out.println("CSV READ ERROR:");
            e.printStackTrace();
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out))) {
            oos.writeObject(customers);
            System.out.println("UPDATED customers.txt -> " + out.getAbsolutePath());
            System.out.println("Total customers written: " + customers.size());
        } catch (Exception e) {
            System.out.println("WRITE ERROR:");
            e.printStackTrace();
        }
    }
}
