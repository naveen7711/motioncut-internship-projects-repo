package com.expensetracker.dao;

import com.expensetracker.model.Expense;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final String FILE_NAME = "expenses.dat";

    public void saveExpenses(List<Expense> expenses) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Expense> loadExpenses() throws IOException, ClassNotFoundException {
        List<Expense> expenses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            expenses = (List<Expense>) ois.readObject();
        }
        return expenses;
    }
}
