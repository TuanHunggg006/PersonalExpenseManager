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

    public Transaction findById(String transactionId) {
        for (Transaction transaction : transactionList) {
            if (transaction.getTransactionId().equalsIgnoreCase(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    // --- TÍNH TOÁN TỔNG TOÀN BỘ TÀI SẢN ---

    public double getTotalIncome() {
        double total = 0;
        for (Transaction t : transactionList) {
            if (t.getType().equalsIgnoreCase("Income")) total += t.getAmount();
        }
        return total;
    }

    public double getTotalExpense() {
        double total = 0;
        for (Transaction t : transactionList) {
            if (t.getType().equalsIgnoreCase("Expense")) total += t.getAmount();
        }
        return total;
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    // --- TÍNH TOÁN RIÊNG CHO TỪNG TÀI KHOẢN (Ví / Ngân hàng) ---

    public double getIncomeByWallet(String walletName) {
        double total = 0;
        for (Transaction t : transactionList) {
            if (t.getWalletName().equalsIgnoreCase(walletName) && t.getType().equalsIgnoreCase("Income")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getExpenseByWallet(String walletName) {
        double total = 0;
        for (Transaction t : transactionList) {
            if (t.getWalletName().equalsIgnoreCase(walletName) && t.getType().equalsIgnoreCase("Expense")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getBalanceByWallet(String walletName) {
        return getIncomeByWallet(walletName) - getExpenseByWallet(walletName);
    }

    // --- CẬP NHẬT DỮ LIỆU MẪU KHỚP 8 THAM SỐ ---
    private void loadSampleData() {
        // Tham số: id, title, amount, type, category, date, note, walletName
        
        // Giao dịch thuộc "Ví"
        transactionList.add(new Transaction("T01", "Thưởng", 1000000, "Income", "Thưởng", "08/04/2026", "Thưởng dự án", "Ví"));
        transactionList.add(new Transaction("T02", "Ăn tiệm", 55562, "Expense", "Ăn uống", "08/04/2026", "", "Ví"));
        
        // Giao dịch thuộc "Ngân hàng"
        transactionList.add(new Transaction("T03", "Lương", 5000000, "Income", "Lương", "05/04/2026", "Lương tháng 3", "Ngân hàng"));
        transactionList.add(new Transaction("T04", "Tiền túi", 200000, "Expense", "Khác", "07/04/2026", "", "Ngân hàng"));
        transactionList.add(new Transaction("T05", "Con cái", 100000, "Expense", "Con cái", "07/04/2026", "Mua sữa", "Ngân hàng"));
        transactionList.add(new Transaction("T06", "Sức khỏe", 100000, "Expense", "Sức khỏe", "07/04/2026", "", "Ngân hàng"));
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
}