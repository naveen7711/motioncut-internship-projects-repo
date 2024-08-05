import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

class User implements Serializable {
    private String username;
    private String hashedPassword;

    public User(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return hashedPassword.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

class Expense implements Serializable {
    private String description;
    private double amount;
    private String category;
    private Date date;

    public Expense(String description, double amount, String category, Date date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("%s - %s: %.2f on %s", category, description, amount, sdf.format(date));
    }
}

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.dat";
    private static Map<String, User> users = new HashMap<>();
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        while (loggedInUser == null) {
            System.out.println("1. Register\n2. Login");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                if (users.containsKey(username)) {
                    System.out.println("Username already exists.");
                } else {
                    users.put(username, new User(username, password));
                    System.out.println("Registration successful.");
                }
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                User user = users.get(username);
                if (user != null && user.checkPassword(password)) {
                    loggedInUser = user;
                    System.out.println("Login successful. Welcome, " + user.getUsername());
                } else {
                    System.out.println("Invalid username or password.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        }

        while (true) {
            System.out.println("1. Add Expense\n2. View Expenses\n3. Category-wise Total\n4. Save and Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter description: ");
                String description = scanner.nextLine();
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter category: ");
                String category = scanner.nextLine();
                System.out.print("Enter date (dd/MM/yyyy): ");
                String dateString = scanner.nextLine();
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
                    Expense expense = new Expense(description, amount, category, date);
                    expenses.add(expense);
                    System.out.println("Expense added.");
                } catch (Exception e) {
                    System.out.println("Invalid date format.");
                }
            } else if (choice == 2) {
                System.out.println("Expenses:");
                expenses.forEach(System.out::println);
            } else if (choice == 3) {
                Map<String, Double> categoryTotals = new HashMap<>();
                for (Expense expense : expenses) {
                    categoryTotals.put(expense.getCategory(),
                            categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
                }
                System.out.println("Category-wise totals:");
                categoryTotals.forEach((category, total) -> 
                        System.out.printf("%s: %.2f\n", category, total));
            } else if (choice == 4) {
                saveExpenses();
                System.out.println("Expenses saved. Exiting...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            expenses = (List<Expense>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved data found or error loading data.");
        }
    }

    private static void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            System.out.println("Error saving expenses.");
        }
    }
}
