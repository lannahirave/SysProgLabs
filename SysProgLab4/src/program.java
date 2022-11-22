import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class program {


    private static String readFileContent(String fileName) throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static void writeFileContent(String fileName, String content) throws Exception {
        try (var out = new PrintWriter(fileName)) {
            out.println(content);
        }
    }

    public static void main(String[] args) throws Exception {
        var input = readFileContent("./src/LearnCSharp.cs");

        var lexer = new Lexer(CSharpLexemes.patterns);

        var result = lexer.process(input);
        /*
        System.out.println("Result:");
        for(var item : result) {
            if (item.type == LexemeType.Space)
                continue;
            System.out.printf("[%s] \"%s\" at %d\n", item.type.name(), item.text, item.start);
        } */

        var content = result.stream()
                .filter((item) -> item.type != LexemeType.Space && item.type != LexemeType.SingleLineComment && item.type != LexemeType.MultiLineComment)
                .map((item) -> String.format("%s - %s", item.type.name(), item.text))
                .toArray(String[]::new);
        writeFileContent("./src/example.txt", String.join("\n", content));
    }
}