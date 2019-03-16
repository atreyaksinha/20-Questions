import java.util.*;
import java.io.*;

public class QuestionsGame {
    private QuestionNode rootTree; // Questions tree
    private Scanner console; // Reads output from files

    // This constructor initializes a new QuestionsGame object with a single leaf
    // node representing the object “computer”.
    public QuestionsGame() {
        rootTree = new QuestionNode("computer");
        console = new Scanner(System.in);
    }

    // This method is called if the client wants to replace the current tree by
    // reading another tree from a file. The method passes a Scanner that is linked to the
    // file and replaces the current tree with a new tree using the information in the file.
    // Assuming the file is legal and in standard format it reads entire lines of
    // input using calls on Scanner’s nextLine.
    public void read(Scanner input) {
        rootTree = newRootTree(input);
    }

    // Uses scanner input as parameter
    // The tree from the file replaces the current tree
    // Returns Questions tree
    private QuestionNode newRootTree(Scanner input) {
        if (!input.nextLine().equals("A:")) {
            return new QuestionNode(input.nextLine(), readHelp(input), readHelp(input));
        } else {
            return new QuestionNode(input.nextLine());
        }
    }

    // This method stores the current questions tree to an output file represented
    // by the given PrintStream. This method is used to later play another game with 
    // the computer using questions from this one.
    public void write(PrintStream output) {
        write(output, rootTree);
    }

    // Uses PrintStream output & QuestionNode current as parameters
    // Stores the current tree to the output file
    private void write(PrintStream output, QuestionNode current) {
        if (current.back != null || current.front != null) {
            output.println("Q:" + current.data);
            write(output, current.back);
            write(output, current.front);
        } else {
            output.println("A:" + current.text);
        }
    }

    // This method uses the current question tree to play one complete guessing game
    // with the user, asking yes/no questions until reaching an answer object to guess. 
    // The game begins with the root node of the tree and ends upon reaching an answer 
    // leaf node. If the computer wins the game, this method prints a message saying so.
    // Otherwise, this method asks the user for the following:
    // • what object they were thinking of,
    // • a question to distinguish that object from the player’s guess, and
    // • whether the player’s object is the yes or no answer for that question.
    public void askQuestions() {
        rootTree = askQuestions(rootTree);
    }

    // Uses QuestionNode current as a parameter
    // Returns a new question tree
    // Completes on game with the user
    private QuestionNode askQuestions(QuestionNode current) {
        if (current.back != null || current.front != null) {
            if (yesTo(current.data)) {
                current.back = askQuestions(current.back);
            } else {
                current.front = askQuestions(current.front);
            }
        } else {
            if (yesTo("Would your object happen to be " + current.data + "?")) {
                System.out.println("Great, I got it right!");
            } else {
                System.out.print("What is the name of your object? ");
                String answer = console.nextLine();
                System.out.println("Please give me a yes/no question that");
                System.out.println("distinguishes between your object");
                System.out.println("and mine--> ");
                String question = console.nextLine();
                if (yesTo("And what is the answer for your object?")) {
                    current = new QuestionNode(question, new QuestionNode(answer), current);
                } else {
                    current = new QuestionNode(question, current, new QuestionNode(answer));
                }
            }
        }
        return current;
    }

    // This method asks the given question until the user types “y” or “n”.
    // Returns true if “y”, false if “n”.
    public boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }

    // QuestionNode class have at least one constructor used by the tree.
    // It represent a single node of the tree.
    // Stores a single node of a binary tree in a game similar to 20 questions.
    private static class QuestionNode {
        public String data; // Stores text at this node
        public QuestionNode back; // Left sub-tree 
        public QuestionNode front; // Right sub-tree

        // Constructs a Question Node with text set and the subtrees set to null.
        public QuestionNode(String textSet) {
            this(textSet, null, null);
        }

        // Constructs a Question Node with text set to 'a',
        // the yes subtree set to 'yes' and the no subtree set to 'no'.
        public QuestionNode(String textSet, QuestionNode yesString, QuestionNode noString) {
            data = textSet;
            back = yesString;
            front = noString;
        }
    }
}