import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class Main {

    // Entry point of the program
    public static void main(String[] args) {
        // Create an instance of the Main class and call the addExpense method
        Main program = new Main();

        System.out.println("Welcome to the Expense Tracker!");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Calculate Total Expenses");
            System.out.println("5. Show Expenses by Category");
            System.out.println("6. Save Expenses to File");
            System.out.println("7. Load Expenses from File");
            System.out.println("8. Exit");

            System.out.print("Choose an option: ");
            // Validate that the input is a valid integer
            if (!program.scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                program.scanner.nextLine();
                continue;
            }

            int choice = program.scanner.nextInt();
            program.scanner.nextLine();

            switch (choice) {
                case 1:
                    program.addExpense();
                    break;
                case 2:
                    program.viewExpenses();
                    break;
                case 3:
                    System.out.print("Enter the number of the expense to delete: ");

                    if (!program.scanner.hasNextInt()) {
                        System.out.println("Invalid input. Please enter a number.");
                        program.scanner.nextLine();
                        break;
                    }

                    int index = program.scanner.nextInt() - 1;
                    program.scanner.nextLine();
                    program.deleteExpense(index);
                    break;
                case 4:
                    program.calculateTotalExpenses();
                    break;
                case 5:
                    program.showExpensesByCategory();
                    break;
                case 6:
                    System.out.print("Enter filename to save expenses: ");
                    String saveFilename = program.scanner.nextLine();
                    program.saveExpensesToFile(saveFilename);
                    break;
                case 7:
                    System.out.print("Enter filename to load expenses: ");
                    String loadFilename = program.scanner.nextLine();
                    program.loadExpensesFromFile(loadFilename);
                    break;
                case 8:
                    System.out.println("Exiting the program.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }
    // List to store expenses
    ArrayList<Expense> expenses = new ArrayList<>();
    // Scanner to read user input
    Scanner scanner = new Scanner(System.in);

    public void addExpense() {
        
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();

        System.out.print("Enter expense amount: ");
        // Validate that the input is a valid double
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount. Please enter a number.");
            scanner.nextLine();
            System.out.print("Enter expense amount: ");
        }
        // Read the amount and consume the newline character
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter expense date: ");
        String date = scanner.nextLine();

        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();

        // Create a new Expense object and add it to the list
        Expense expense = new Expense(description, amount, date, category);
        expenses.add(expense);
        System.out.println("Expense added successfully.");
    }

    public void viewExpenses() {
    // Check if there are any expenses recorded
    if (expenses.isEmpty()) {
        System.out.println("No expenses recorded.");
        return;
    }

    System.out.println("Expenses:");
    // Loop through the list of expenses and print each one
    for (int i = 0; i < expenses.size(); i++) {
        // Print the index and the expense details
        System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    public void deleteExpense(int index) {
        // Check if the index is valid
        if (index < 0 || index >= expenses.size()) {
            System.out.println("Invalid index. No expense deleted.");
            return;
        }

        // Remove the expense at the specified index
        expenses.remove(index);
        System.out.println("Expense deleted successfully.");
    }

    public void calculateTotalExpenses() {
        double total = 0;
        // Loop through the list of expenses and sum their amounts
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        // Print the total expenses
        System.out.println("Total Expenses: $" + total);
    }

    public void showExpensesByCategory() {
        // Check if there are any expenses recorded
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        // Create a HashMap to group expenses by category
        HashMap<String, ArrayList<Expense>> categoryMap = new HashMap<>();

        // Loop through the list of expenses and group them by category
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            // If the category is not already in the map, add it with a new list
            if (!categoryMap.containsKey(category)) {
                // Initialize a new list for this category
                categoryMap.put(category, new ArrayList<>());
            }
            // Add the expense to the corresponding category list
            categoryMap.get(category).add(expense);
        }

        // Loop through the categories and print the expenses in each category
        for (String category : categoryMap.keySet()) {
            System.out.println("Category: " + category);

            double categoryTotal = 0;

            // Loop through the expenses in the current category and print them
            for (Expense expense : categoryMap.get(category)) {
                System.out.println(" - " + expense.getDescription() 
                           + ": $" + expense.getAmount());
                categoryTotal += expense.getAmount();
            }
            System.out.println("Total for " + category + ": $" + categoryTotal);
        }

    }

    public void saveExpensesToFile(String filename) {
        // Use try-with-resources to ensure the BufferedWriter is closed automatically
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Loop through the list of expenses and write each one to the file
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + "," 
                             + expense.getAmount() + "," 
                             + expense.getDate() + "," 
                             + expense.getCategory());
                writer.newLine();
            }
            System.out.println("Expenses saved to file: " + filename);
        } 
        // Catch any IOExceptions that occur during file writing
        catch (IOException e) {
            System.out.println("Error saving expenses to file: " + e.getMessage());
        }
    }

    public void loadExpensesFromFile(String filename) {
    // Use try-with-resources to ensure the BufferedReader is closed automatically
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        // Clear the current list of expenses before loading new ones
        expenses.clear();
        String line;
        // Read each line from the file and create Expense objects
        while ((line = reader.readLine()) != null) {
            // Split the line by commas to extract expense details
            String[] parts = line.split(",");
            // Check if the line has the correct number of parts (4)
            if (parts.length == 4) {
                // Parse the expense details from the parts array
                String description = parts[0];
                double amount = Double.parseDouble(parts[1]);
                String date = parts[2];
                String category = parts[3];

                // Create a new Expense object and add it to the list
                Expense expense = new Expense(description, amount, date, category);
                expenses.add(expense);
            }
        }
        System.out.println("Expenses loaded from file: " + filename);
    } catch (IOException e) {
        System.out.println("Error loading expenses from file: " + e.getMessage());
    }
}

}