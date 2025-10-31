
package warehouse;

import java.io.*;
import java.util.*;
import java.lang.reflect.Method;

/**
 * Warehouse: maintains products, observers and provides CSV load/save
 * Assumes Product has: getId(), getName(), getQuantity(), getThreshold(),
 *  setQuantity(int), constructor Product(int id, String name, int qty, int threshold)
 */
public class Warehouse {

    private final Map<Integer, Product> products = new HashMap<>();
    private final List<Object> observers = new ArrayList<>(); // keep generic so existing observer classes work

    // ----- Observer registration -----
    public void registerObserver(Object observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    // optional: if you previously used addObserver, keep that too
    public void addObserver(Object observer) {
        registerObserver(observer);
    }

    // Try to call common observer method names using reflection (so existing AlertService works)
    private void notifyObservers(Product p) {
        for (Object obs : observers) {
            if (obs == null || p == null) continue;
            // try several common method names
            String[] methodNames = {"onLowStock", "notifyLowStock", "update", "updateStock", "alert", "handleLowStock"};
            boolean invoked = false;
            for (String mName : methodNames) {
                try {
                    Method m = obs.getClass().getMethod(mName, Product.class);
                    m.invoke(obs, p);
                    invoked = true;
                    break;
                } catch (NoSuchMethodException nsme) {
                    // try next name
                } catch (Exception e) {
                    // other reflection exceptions: print stack trace for debugging
                    e.printStackTrace();
                    invoked = true; // avoid further attempts if method exists but failed
                    break;
                }
            }
            if (!invoked) {
                // no matching method found; ignore silently (or log if you want)
            }
        }
    }

    // ----- Product operations -----
    public synchronized void addProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("Product cannot be null");
        if (products.containsKey(p.getId())) {
            throw new IllegalArgumentException("Product with ID " + p.getId() + " already exists.");
        }
        products.put(p.getId(), p);
    }

    public synchronized void receiveShipment(int productId, int qty) {
        Product p = products.get(productId);
        if (p == null) {
            System.out.println("⚠ Product ID " + productId + " not found in warehouse.");
            return;
        }
        if (qty <= 0) {
            System.out.println("⚠ Quantity must be positive.");
            return;
        }
        p.setQuantity(p.getQuantity() + qty);
        System.out.println("✅ Received " + qty + " units of " + p.getName() + " (ID: " + p.getId() + "). New qty: " + p.getQuantity());
    }

    public synchronized void fulfillOrder(int productId, int qty) {
        Product p = products.get(productId);
        if (p == null) {
            System.out.println("⚠ Product ID " + productId + " not found in warehouse.");
            return;
        }
        if (qty <= 0) {
            System.out.println("⚠ Quantity must be positive.");
            return;
        }
        if (p.getQuantity() < qty) {
            System.out.println("⚠ Not enough stock to fulfill. Available: " + p.getQuantity());
            return;
        }
        p.setQuantity(p.getQuantity() - qty);
        System.out.println("✅ Fulfilled " + qty + " units of " + p.getName() + " (ID: " + p.getId() + "). Remaining qty: " + p.getQuantity());

        // if below threshold, notify observers
        if (p.getQuantity() <= p.getReorderThreshold()) {
            System.out.println("⚠ Stock for " + p.getName() + " (ID: " + p.getId() + ") is at or below threshold (" + p.getReorderThreshold() + "). Notifying observers...");
            notifyObservers(p);
        }
    }

    public synchronized void printInventory() {
        System.out.println("\n--- Inventory ---");
        if (products.isEmpty()) {
            System.out.println("[Empty]");
            return;
        }
        System.out.printf("%-10s %-25s %-10s %-10s%n", "ProductID", "Name", "Quantity", "Threshold");
        System.out.println("----------------------------------------------------------------");
        for (Product p : products.values()) {
            System.out.printf("%-10d %-25s %-10d %-10d%n", p.getId(), p.getName(), p.getQuantity(), p.getReorderThreshold());
        }
    }

    // ----- File operations (CSV) -----
    // CSV header: ProductID,ProductName,Quantity,Threshold
    public synchronized void saveToFile(String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("ProductID,ProductName,Quantity,Threshold");
            for (Product p : products.values()) {
                // escape commas in name by surrounding in quotes if needed
                String name = p.getName();
                if (name.contains(",") || name.contains("\"")) {
                    name = name.replace("\"", "\"\"");
                    name = "\"" + name + "\"";
                }
                pw.printf("%d,%s,%d,%d%n", p.getId(), name, p.getQuantity(), p.getReorderThreshold());
            }
            System.out.println("💾 Inventory saved to " + path);
        } catch (IOException e) {
            System.out.println("⚠ Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void loadFromFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            // no file to load - that's fine
            System.out.println("ℹ No existing inventory file found at " + path + " (starting fresh).");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine(); // read header
            if (line == null) return;

            int lineNo = 1;
            while ((line = br.readLine()) != null) {
                lineNo++;
                // Basic CSV parsing that supports quoted names with commas
                List<String> tokens = parseCsvLine(line);
                if (tokens.size() < 4) {
                    System.out.println("⚠ Skipping invalid CSV line " + lineNo + ": " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(tokens.get(0).trim());
                    String name = tokens.get(1).trim();
                    int qty = Integer.parseInt(tokens.get(2).trim());
                    int threshold = Integer.parseInt(tokens.get(3).trim());

                    // If product already exists in memory, update quantity/threshold or skip
                    Product existing = products.get(id);
                    if (existing == null) {
                        Product p = new Product(id, name, qty, threshold);
                        products.put(id, p);
                    } else {
                        existing.setQuantity(qty);
                        // optionally update threshold/name
                        // existing.setThreshold(threshold);
                        // existing.setName(name);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("⚠ Invalid number at CSV line " + lineNo + ": " + line);
                }
            }
            System.out.println("✅ Loaded inventory from " + path + " (" + products.size() + " products).");
        } catch (IOException e) {
            System.out.println("⚠ Error loading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Simple CSV parser for one line supporting quoted fields
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null || line.isEmpty()) return result;

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    // peek next char to check for escaped quote
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip next quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == '"') {
                    inQuotes = true;
                } else if (ch == ',') {
                    result.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(ch);
                }
            }
        }
        result.add(cur.toString());
        return result;
    }

    // Optional helper to get product (for other code)
    public Product getProduct(int id) {
        return products.get(id);
    }

    // Optional: clear inventory (useful when switching warehouses before loading)
    public synchronized void clearInventory() {
        products.clear();
    }
}


