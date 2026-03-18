import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


abstract class Transaction {
    String date;
    int amount;
    String category;

    abstract void input();
    abstract String toFileString();
}


class Income extends Transaction {
    String source;
    int counter = 0;

    void input() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the amount of income:");
        amount = sc.nextInt();

        System.out.println("Enter the source of income:");
        source = sc.next();

        System.out.println("Enter the date of income (DD-MM-YYYY):");
        date = sc.next();

        category = source;
        counter++;
    }

    String toFileString() {
        return date + "\tIncome\t" + amount + "\t" + category;
    }
}


class Expense extends Transaction {

    String description;

    void input() {
        Scanner sc = new Scanner(System.in);

        System.out.println("----------------------------------------");
        System.out.println("Enter expense category:");
        System.out.println("1. Food");
        System.out.println("2. Travel");
        System.out.println("3. Shopping");
        System.out.println("4. Bills");
        System.out.println("5. Other");

        System.out.print("Choose category: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: category = "Food"; break;
            case 2: category = "Travel"; break;
            case 3: category = "Shopping"; break;
            case 4: category = "Bills"; break;
            case 5: category = "Other"; break;
            default: category = "Other";
        }

        System.out.print("Enter amount: ");
        amount = sc.nextInt();
        sc.nextLine(); // fix buffer

        System.out.print("Enter description: ");
        description = sc.nextLine();

        System.out.print("Enter date (DD-MM-YYYY): ");
        date = sc.next();
    }

    String toFileString() {
        return date + "\tExpense\t" + amount + "\t" + category + "\t" + description;
    }
}


class FileManager {
    static void saveTransaction(String data) {
        try {
            FileWriter fw = new FileWriter("transactions.txt", true);
            fw.write(data + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving transaction");
        }
    }
}


class ViewTransactions {
    static void view() {
        try {
            File file = new File("transactions.txt");
            Scanner sc = new Scanner(file);

            System.out.println("TRANSACTIONS");
            System.out.println("--------------------------------------------------");
            System.out.println("Date\tType\tAmount\tCategory");

            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("No transactions found.");
        }
    }
}


class monthlySummary {

    static void show() {
        int totalIncome = 0;
        int totalExpense = 0;

        try {
            File file = new File("transactions.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String parts[] = line.split("\t");

                String type = parts[1];
                int amount = Integer.parseInt(parts[2]);

                if (type.equals("Income")) {
                    totalIncome += amount;
                } else if (type.equals("Expense")) {
                    totalExpense += amount;
                }
            }

            sc.close();

            int balance = totalIncome - totalExpense;
            double savingsRate = (totalIncome == 0) ? 0 :
                    ((double) balance / totalIncome) * 100;

            System.out.println("----------------------------------------");
            System.out.println("MONTHLY SUMMARY\n");

            System.out.println("Total Income    : Rs. " + totalIncome);
            System.out.println("Total Expenses  : Rs. " + totalExpense);
            System.out.println("Remaining Balance : Rs. " + balance);
            System.out.printf("Savings Rate    : %.2f%%\n", savingsRate);

        } catch (Exception e) {
            System.out.println("No data available.");
        }
    }
}


class categoryAnalysis {

    static void Analyse() {

        int food = 0, travel = 0, shopping = 0, bills = 0, other = 0;

        try {
            File file = new File("transactions.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String parts[] = sc.nextLine().split("\t");

                if (parts[1].equals("Expense")) {
                    String cat = parts[3];
                    int amount = Integer.parseInt(parts[2]);

                    if (cat.equals("Food")) food += amount;
                    else if (cat.equals("Travel")) travel += amount;
                    else if (cat.equals("Shopping")) shopping += amount;
                    else if (cat.equals("Bills")) bills += amount;
                    else other += amount;
                }
            }

            sc.close();

            System.out.println("\nCATEGORY ANALYSIS");
            System.out.println("----------------------------------------");

            System.out.println("Food     : Rs. " + food);
            System.out.println("Travel   : Rs. " + travel);
            System.out.println("Shopping : Rs. " + shopping);
            System.out.println("Bills    : Rs. " + bills);
            System.out.println("Other    : Rs. " + other);

        } catch (Exception e) {
            System.out.println("No data available.");
        }
    }
}


public class Test {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String continueChoice;

        do {
            System.out.println("\n----------------------------------------");
            System.out.println("Welcome to Personal Expense Tracker!");
            System.out.println("----------------------------------------");

            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View all transactions");
            System.out.println("4. Monthly Summary");
            System.out.println("5. Category analysis");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            System.out.println("----------------------------------------");

            switch (choice) {

                case 1:
                    Income income = new Income();
                    System.out.println("You chose to add income.");
                    income.input();
                    FileManager.saveTransaction(income.toFileString());
                    System.out.println("Income added successfully!");
                    break;

                case 2:
                    System.out.println("You chose to add an expense.");
                    Expense expense = new Expense();
                    expense.input();
                    FileManager.saveTransaction(expense.toFileString());
                    System.out.println("\nExpense added successfully!");
                    break;

                case 3:
                    System.out.println("You chose to view all transactions.");
                    ViewTransactions.view();
                    break;

                case 4:
                    System.out.println("You chose to view the monthly summary.");
                    monthlySummary.show();
                    break;

                case 5:
                    System.out.println("You chose to view category analysis.");
                    categoryAnalysis.Analyse();
                    break;

                case 6:
                    System.out.println("Thank you for using Personal Expense Tracker. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }

            System.out.println("\nDo you want to perform another action? (yes/no)");
            continueChoice = sc.next();

        } while (continueChoice.equalsIgnoreCase("yes"));
    }
}
