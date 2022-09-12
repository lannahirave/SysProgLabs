import javax.swing.*;
import java.io.File;

public class ConsoleApplication {
    private static final IParser Parser = new ParserBase();

    public static void main(String[] args) {
        System.out.println("Choose file: ");
        JFileChooser fileOpen = new JFileChooser();
        int ret = fileOpen.showDialog(null, "Choose file");
        while (true) {
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpen.getSelectedFile();
                System.out.println(file.toString());
                Parser.Parse(file.toString());
                break;
            }
            System.out.println("You MUST select the file.");
        }
    }
}
