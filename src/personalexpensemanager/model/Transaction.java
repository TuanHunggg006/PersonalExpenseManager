package personalexpensemanager.model;

public class Transaction {
    private String transactionId, title, type, category, date, note, walletName;
    private double amount;

    public Transaction() {}

    // Constructor chuẩn 8 tham số
    public Transaction(String id, String title, double amount, String type, String category, String date, String note, String walletName) {
        this.transactionId = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.note = note;
        this.walletName = walletName;
    }

    // Các Getter/Setter
    public String getTransactionId() { return transactionId; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getNote() { return note; }
    public String getWalletName() { return walletName; }
    
    public void setWalletName(String walletName) { this.walletName = walletName; }
}