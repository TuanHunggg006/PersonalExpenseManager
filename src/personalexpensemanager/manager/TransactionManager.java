package personalexpensemanager.manager;

import personalexpensemanager.model.Transaction;
import java.util.ArrayList;

public class TransactionManager {
    private ArrayList<Transaction> transactionList;

    public TransactionManager() {
        transactionList = new ArrayList<>();
        loadSampleData();
    }

    public ArrayList<Transaction> getAllTransactions() {
        return transactionList;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionList.add(transaction);
        }
    }

    public boolean removeTransaction(String transactionId) {
        Transaction transaction = findById(transactionId);
        if (transaction != null) {
            transactionList.remove(transaction);
            return true;
        }
        return false;
    }

    public boolean updateTransaction(Transaction updatedTransaction) {
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).getTransactionId().equalsIgnoreCase(updatedTransaction.getTransactionId())) {
                transactionList.set(i, updatedTransaction);
                return true;
            }
        }
        return false;
    }

    public Transaction findById(String transactionId) {
        for (Transaction transaction : transactionList) {
            if (transaction.getTransactionId().equalsIgnoreCase(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public ArrayList<Transaction> findByTitle(String keyword) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterByType(String type) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase(type)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterByCategory(String category) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equalsIgnoreCase(category)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterByDate(String date) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().equals(date)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public ArrayList<Transaction> filterByMonth(String month) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().startsWith(month)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public double getTotalIncome() {
        double total = 0;
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public double getTotalExpense() {
        double total = 0;
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase("Expense")) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    private void loadSampleData() {
        transactionList.add(new Transaction("T01", "Salary", 8000000, "Income", "Work", "2026-04-01", "Monthly salary"));
        transactionList.add(new Transaction("T02", "Lunch", 50000, "Expense", "Food", "2026-04-02", "Lunch with friends"));
        transactionList.add(new Transaction("T03", "Bus", 10000, "Expense", "Transport", "2026-04-02", "Go to school"));
        transactionList.add(new Transaction("T04", "Bonus", 1000000, "Income", "Bonus", "2026-04-03", "Project reward"));
        transactionList.add(new Transaction("T05", "Milk Tea", 35000, "Expense", "Food", "2026-03-28", "Evening drink"));
        transactionList.add(new Transaction("T06", "Freelance", 2000000, "Income", "Work", "2026-03-25", "Freelance payment"));
        transactionList.add(new Transaction("T07", "Book", 120000, "Expense", "Study", "2026-04-05", "Buy Java book"));
    }
    
}