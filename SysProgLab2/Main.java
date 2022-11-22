import javax.swing.*;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        var fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
        int result = fileChooser.showDialog(null, "Choose test file");

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("No file - No output!!1!");
            return;
        }

        var file = fileChooser.getSelectedFile();
        try {
            var nfa = NFA.createFromTxt(file);

            System.out.format("Enter the word to process: ");
            Scanner scanner = new Scanner(System.in);
            String word = scanner.next();

            Set<Integer> states = nfa.processWord(word);
            System.out.println("Processed word " + word + ", processing ended in the following states: ");
            printIterableInline(states, "\t", " ");
        } catch (FileNotFoundException ex) {
            System.out.println("Invalid file pathname");
        }

    }

    private static <T> void printIterableInline(Iterable<T> iterable, String indent, String separator) {
        System.out.print(indent);
        for (T t : iterable) {
            System.out.print(t + separator);
        }
        System.out.println();
    }

}
