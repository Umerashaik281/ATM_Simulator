import java.util.*;

class Account {
    private long cardNumber;
    private int pin;
    private String name;
    private double balance;
    private Deque<String> miniStatement = new LinkedList<>();

    public Account(long cardNumber, int pin, String name, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.name = name;
        this.balance = balance;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        addTransaction("Withdrawn: " + amount);
        return true;
    }

    public void transfer(Account receiver, double amount) {
        if (withdraw(amount)) {
            receiver.deposit(amount);
            addTransaction("Transferred " + amount + " to " + receiver.getName());
        }
    }

    public void addTransaction(String detail) {
        if (miniStatement.size() == 5) {
            miniStatement.pollFirst();
        }
        miniStatement.addLast(detail + " | Balance: " + balance);
    }

    public void printMiniStatement() {
        System.out.println("----- Mini Statement -----");
        for (String t : miniStatement) {
            System.out.println(t);
        }
        System.out.println("--------------------------");
    }
}

public class ATMSimulator {
    private static Map<Long, Account> accounts = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== ATM Simulator =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Thank you for using ATM. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Card Number: ");
        long cardNumber = sc.nextLong();
        if (accounts.containsKey(cardNumber)) {
            System.out.println("⚠️ Account with this card number already exists!");
            return;
        }
        System.out.print("Enter 4-digit PIN: ");
        int pin = sc.nextInt();
        System.out.print("Enter Initial Deposit: ");
        double balance = sc.nextDouble();

        Account newAcc = new Account(cardNumber, pin, name, balance);
        accounts.put(cardNumber, newAcc);
        System.out.println("✅ Account created successfully!");
    }

    private static void login() {
        System.out.print("Enter Card Number: ");
        long cardNumber = sc.nextLong();
        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        Account acc = accounts.get(cardNumber);
        if (acc != null && acc.getPin() == pin) {
            System.out.println("✅ Login Successful! Welcome " + acc.getName());
            atmMenu(acc);
        } else {
            System.out.println("❌ Invalid card number or PIN!");
        }
    }

    private static void atmMenu(Account acc) {
        while (true) {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Mini Statement");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Your Balance: " + acc.getBalance());
                case 2 -> {
                    System.out.print("Enter amount to deposit: ");
                    double amount = sc.nextDouble();
                    acc.deposit(amount);
                    System.out.println("✅ Deposited Successfully!");
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double amount = sc.nextDouble();
                    if (acc.withdraw(amount)) {
                        System.out.println("✅ Withdrawn Successfully!");
                    } else {
                        System.out.println("❌ Insufficient Balance!");
                    }
                }
                case 4 -> {
                    System.out.print("Enter receiver card number: ");
                    long receiverCard = sc.nextLong();
                    Account receiver = accounts.get(receiverCard);
                    if (receiver == null) {
                        System.out.println("❌ Receiver account not found!");
                        break;
                    }
                    System.out.print("Enter amount to transfer: ");
                    double amount = sc.nextDouble();
                    if (acc.getBalance() < amount) {
                        System.out.println("❌ Insufficient Balance!");
                    } else {
                        acc.transfer(receiver, amount);
                        System.out.println("✅ Transferred Successfully!");
                    }
                }
                case 5 -> acc.printMiniStatement();
                case 6 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
