package shop.model;

public class Customer extends User {
    private double balance;

    public Customer(String username, String password, double balance) {
        super(username, password, "CUSTOMER");
        this.balance = balance;
    }

    public double getBalance() { 
        return balance; }
    public void setBalance(double balance) {
         this.balance = balance; }
}

