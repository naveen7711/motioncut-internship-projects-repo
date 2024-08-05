package com.expensetracker.main;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.service.ExpenseManager;
import com.expensetracker.service.UserManager;
import com.expensetracker.dao.ExpenseDAO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        ExpenseManager expenseManager = new ExpenseManager();
        ExpenseDAO expenseDAO = new ExpenseDAO();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Load previously saved expenses
        try {
            List<Expense> loadedExpenses = expenseDAO.loadExpenses();
            loadedExpenses.forEach(expenseManager::addExpense);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved data found.");
        }

        User loggedInUser = null;
        while (loggedInUser == null) {
            System.out.println("1. Register");
            System.out.println("2. Login");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    if (userManager.register(username, password)) {
                        System.out.println("Registration successful.");
                    } else {
                        System.out.println("Username already exists.");
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    loggedInUser = userManager.login(loginUsername, loginPassword);
                    if (loggedInUser != null) {
                        System.out.println("Login successful. Welcome, " + loggedInUser.getUsername());
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Expenses by Category");
            System.out.println("4. Total by Category");
            System.out.println("5. Save and Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
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
                        Date date = sdf.parse(dateString);
                        Expense expense = new Expense(description, amount, category, date);
                        expenseManager.addExpense(expense);
                        System.out.println("Expense added.");
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }
                    break;

                case 2:
                    System.out.println("Expenses:");
                    expenseManager.getExpenses().forEach(System.out::println);
                    break;

                case 3:
                    System.out.print("Enter category: ");
                    String filterCategory = scanner.nextLine();
                    List<Expense> categoryExpenses = expenseManager.getExpensesByCategory(filterCategory);
                    System.out.println("Expenses for category " + filterCategory + ":");
                    categoryExpenses.forEach(System.out::println);
                    break;

                case 4:
                    System.out.print("Enter category: ");
                    String sumCategory = scanner.nextLine();
                    double total = expenseManager.getTotalByCategory(sumCategory);
                    System.out.println("Total for category " + sumCategory + ": " + total);
                    break;

                case 5:
                    try {
                        expenseDAO.saveExpenses(expenseManager.getExpenses());
                        System.out.println("Expenses saved. Exiting...");
                    } catch (IOException e) {
                        System.out.println("Error saving expenses.");
                    }
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
