import java.io.*;
import java.nio.charset.Charset;

public class program {
    private static void writeFileContent(String fileName, String content) throws Exception {
        try (var out = new PrintWriter(fileName)) {
            out.println(content);
        }
    }

    public static void main(String[] args) throws Exception {
        var input = "./SysProgLab3/src/input.cs";

        Charset encoding = Charset.defaultCharset();
        try (InputStream in = new FileInputStream(input);
             Reader reader = new InputStreamReader(in, encoding);
             Reader buffer = new BufferedReader(reader)) {
            var lexer = new Lexer(buffer, CSharpLexemes.patterns);
            var content = lexer.stream()
                    .filter((item) -> item.type != LexemeType.Space && item.type != LexemeType.SingleLineComment && item.type != LexemeType.MultiLineComment)
                    .map((item) -> String.format("%s - %s", item.type.name(), item.text))
                    .toArray(String[]::new);

            writeFileContent("./SysProgLab3/src/output.txt", String.join("\n", content));
        }
    }
}