import java.util.*;
//The class for the Bank Side
class Bank{
    private Map<Long,User>users;
    private Map<Long,Boolean>accNo;
    
    public Bank(){
        this.users=new HashMap<>();
        this.accNo=new HashMap<>();
    }
    public void addUser(User user){
        users.put(user.getAccNo(),user);
        accNo.put(user.getAccNo(),true);
    }
    public boolean isUniqueAccNo(long accNum){
        return !accNo.containsKey(accNum);
    }
    public User authUser(long accNo,int pin){
        User user=users.get(accNo);
        if(user!=null && user.verifyPIN(pin)){
            return user;
        }
        return null;
    }
}
//The class that deals with the User Data
class User{
    private String name;
    private String address;
    private String phoneNum;
    private long accNo;
    private int pin;
    private double bal;
    public User(int id,String name,String address,String phoneNum,long accNo,int pin,double bal){
        this.name=name;
        this.address=address;
        this.phoneNum=phoneNum;
        this.accNo=accNo;
        this.pin=pin;
        this.bal=bal;
    }
    public String getname(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPhoneNumber(){
        return phoneNum;
    }
    public long getAccNo(){
        return accNo;
    }
    public double getBalance(){
        return bal;
    }
    public boolean verifyPIN(int enterPIN){
        return this.pin==enterPIN;
    }
    public void deposit(double amount){
        bal+=amount;
    }
    public boolean withdraw(double amount){
        if(bal>=amount){
            bal-=amount;
            return true;
        }
        return false;
    }
}

//The Class that deals with the UI
class UserInterface{
    private User user;
    private Scanner scanner;

    public UserInterface(Bank bank,User user){
        this.user=user;
        this.scanner=new Scanner(System.in);
    }
    public void handleUserTasks(){
        System.out.println("Welcome, "+user.getname());
        int ch;
        do {
        System.out.println("\nWhat do you want to do?");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            ch = scanner.nextInt();
            switch (ch) {
                case 1:
                    displayBalance();
                    break;
                case 2:
                    depoMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    System.out.println("Thank you for using our services. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid ch. Please enter a valid option.");
            }
        } while (ch != 4);
    }
    private void displayBalance() {
        System.out.println("Name: " + user.getname());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Phone Number: " + user.getPhoneNumber());
        System.out.println("Account Number: " + user.getAccNo());
        System.out.println("Available Balance: Rs" + user.getBalance());
    }

    private void depoMoney() {
        System.out.print("Enter amount to deposit: Rs");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            user.deposit(amount);
            System.out.println("Deposit successful!");
            System.out.println("Updated Balance: Rs" + user.getBalance());
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
    }

    private void withdrawMoney() {
        System.out.print("Enter amount to withdraw: Rs");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            if (user.withdraw(amount)) {
                System.out.println("Withdrawal successful!");
                System.out.println("Updated Balance: Rs" + user.getBalance());
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
    }
}

//The class for the management side
class Bank_Operations{
    private Scanner scanner;
    private Bank bank;

    public Bank_Operations(){
        this.scanner=new Scanner(System.in);
        this.bank=new Bank();
    }
    public void startSystem() {
        System.out.println("\n------------------------WELCOME TO THE BANK------------------------\n");
        int numberOfUsers = getUserInput("Enter the number of users to create an Account: ");
        for (int i = 0; i < numberOfUsers; i++) {
            AddUser(i + 1);
        }
        System.out.println("\n------------------------TASK AREA------------------------\n");
        long accountNumber;
        User user;
        do {
            accountNumber = getUserInput("Please enter your Account Number: ");
            scanner.nextLine(); 
            int pin = getUserInput("Please enter your PIN: ");
            user = bank.authUser(accountNumber, pin);
            if (user == null) {
                System.out.println("Invalid account number or PIN. Please try again.");
            }
        } while (user == null);
        UserInterface userInterface = new UserInterface(bank, user);
        userInterface.handleUserTasks();
    }
    
    
    private void AddUser(int id) {
        System.out.println("ID No: " + id);
        System.out.print("Enter your full name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter your address: ");
        String address = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();
        long accNo;
        do {
            System.out.print("Enter your account no: ");
            while (!scanner.hasNextLong()) { 
                System.out.println("Invalid input. Please enter a valid number.\n\n");
                scanner.next(); 
            }
            accNo = scanner.nextLong();
            scanner.nextLine(); 
            if (!bank.isUniqueAccNo(accNo)) {
                System.out.println("Account number already exists. Please choose a unique account number.\n\n");
            }
        } while (!bank.isUniqueAccNo(accNo));
        int pin = getUserInput("Set your PIN: ");
        double balance;
        do {
            balance = getUserInput("Enter your initial deposit amount (minimum Rs1000): ");
            if (balance < 1000) {
                System.out.println("Minimum balance required is Rs1000. Please enter a higher amount.\n");
            }
        } while (balance < 1000);
        User newUser = new User(id, name, address, phoneNumber, accNo, pin, balance);
        bank.addUser(newUser);
    }
    
    private int getUserInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
public class ATMService{
    public static void main(String[] args) {
       Bank_Operations bank=new Bank_Operations();
       bank.startSystem();
    }

}