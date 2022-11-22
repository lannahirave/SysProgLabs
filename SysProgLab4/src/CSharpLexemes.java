public class CSharpLexemes {
    //CSharp keywords
    public static String[] keywords = new String[] {
        "abstract", "as", "base", "bool", "break", "byte", "case", 
        "catch", "char", "checked", "class", "const", "continue", "decimal", "default", "delegate", 
        "do", "double", "else", "enum", "event", "explicit", "extern", "false", "finally", "fixed", "float", 
        "for", "foreach", "goto", "if", "implicit", "in", "int", "interface", "internal", "is", "lock", "long", 
        "namespace", "new", "null", "object", "operator", "out", "override", "params", "private", "protected", 
        "public", "readonly", "ref", "return", "sbyte", "sealed", "short", "sizeof", "stackalloc", "static", 
        "string", "struct", "switch", "this", "throw", "true", "try", "typeof", "uint", "ulong", "unchecked", 
        "unsafe", "ushort", "using", "virtual", "void", "volatile", "while"
    };
    //CSharp operators
    public static String[] operators = new String[] {
            "+=", "-=", "*=", "/=", "%=",
            "&=", "|=", "^=", "<<=", ">>=", "&^=",
            "&&", "||", "<-", "++", "--",
            "==", "<", ">", "=", "~",
            "!=", "<=", ">=", "...",
            "&", "|", "^", "<<", ">>", "&^",
            "!", "+", "-", "*", "/", "%",
            "[", "]",
            ",", ".", ":", "?", "??", "??=",
    };

    public static LexemePattern[] patterns = new LexemePattern[] {
            // Single line comment //
            new LexemePattern("^//.*", LexemeType.SingleLineComment),
            // Multiline comments //
            new LexemePattern("^/\\*(.|\\n|\\r)+?\\*/", LexemeType.MultiLineComment),


            // Space
            new LexemePattern("^\\s+", LexemeType.Space),

            // Keywords
            new LexemePattern("^(?<!\\w)(" + String.join("|", keywords) + ")(?<!\\w)?", LexemeType.Keyword),

            // Bool literal
            new LexemePattern("^(?<!\\w)(true|false)(?<!\\w)?", LexemeType.BoolLiteral),

            // Identifier
            new LexemePattern("^[a-zA-Z_]\\w*", LexemeType.Identifier),

            // Delimiters
            new LexemePattern("^[\\(\\)\\{\\}\\;]", LexemeType.Delimiter),

            // Number literal
            new LexemePattern("^[\\+\\-]?([0-9]+([\\.][0-9]*)?|[\\.][0-9]+)", LexemeType.NumberLiteral),

            // Operators
            new LexemePattern("^(?<!\\w)(\\Q" + String.join("\\E|\\Q", operators) + "\\E)(?<!\\w)?", LexemeType.Operator),

            // Char literal
            new LexemePattern("^'(.|\\\\.)'", LexemeType.CharLiteral),

            // String literal
            new LexemePattern("^\\\"([^\\\"\\\\\\n\\r]|\\\\.)*\\\"", LexemeType.StringLiteral),

            // Raw string literal
            new LexemePattern("^@\"[^\"]*\"", LexemeType.RawStringLiteral),

            // Template string literal
            new LexemePattern("^\\$\"[^\"\n\r]*\"", LexemeType.TemplateStringLiteral),

            // Row template string literal
            new LexemePattern("^(\\$@|@\\$)\\\"[^\\\"]*\\\"", LexemeType.RowTemplateStringLiteral),

            // Directive
            new LexemePattern("^#.*", LexemeType.Directive),

            // Other
            new LexemePattern("^[\\s\\S]", LexemeType.Unknown)
    };
}
