import java.util.*;

public class CSharpLexemes {
    public static class Constants {
        public static HashSet<String> keywords = new HashSet<>() {{
            add("abstract");
            add("as");
            add("base");
            add("bool");
            add("break");
            add("byte");
            add("case");
            add("catch");
            add("char");
            add("checked");
            add("class");
            add("const");
            add("continue");
            add("decimal");
            add("default");
            add("delegate");
            add("do");
            add("double");
            add("else");
            add("enum");
            add("event");
            add("explicit");
            add("extern");
            add("false");
            add("finally");
            add("fixed");
            add("float");
            add("for");
            add("foreach");
            add("goto");
            add("if");
            add("implicit");
            add("in");
            add("int");
            add("interface");
            add("internal");
            add("is");
            add("lock");
            add("long");
            add("namespace");
            add("new");
            add("null");
            add("object");
            add("operator");
            add("out");
            add("override");
            add("params");
            add("private");
            add("protected");
            add("public");
            add("readonly");
            add("ref");
            add("return");
            add("sbyte");
            add("sealed");
            add("short");
            add("sizeof");
            add("stackalloc");
            add("static");
            add("string");
            add("struct");
            add("switch");
            add("this");
            add("throw");
            add("true");
            add("try");
            add("typeof");
            add("uint");
            add("ulong");
            add("unchecked");
            add("unsafe");
            add("ushort");
            add("using");
            add("var");
            add("virtual");
            add("void");
            add("volatile");
            add("while");
            add("add");
            add("alias");
            add("async");
            add("await");
            add("dynamic");
            add("get");
            add("global");
            add("nameof");
            add("partial");
            add("remove");
            add("set");
            add("value");
            add("when");
            add("where");
            add("yield");
            add("ascending");
            add("by");
            add("descending");
            add("equals");
            add("from");
            add("group");
            add("into");
            add("join");
            add("let");
            add("on");
            add("orderby");
            add("select");
        }};

        public static Symbols.OperatorCharacters operatorSymbols = Symbols.getOperatorsMap(new String[] {
                "+", "-", "*", "/", "%", "++", "--",
                "&", "&&", "|", "||", "^", "!", "~",
                ">>", "<<", "==", "!=", ">", "<", ">=", "<=",
                "=",
                "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", ">>=", "<<=",
                "*", "->",
                "(", ")", "[", "]", "{", "}",
                ".", ",", "...", "?", "??", ";", ":"
        });
    }

    public static LexemePattern[] patterns = new LexemePattern[] {
            // Space
            new LexemePattern(
                c -> Symbols.isSpace(c) || Symbols.isNewLine(c),
                (c, reader) -> LexemeType.Space
            ),
            // Preprocessor Directive
            new LexemePattern(
                c -> c == '#',
                (c, reader) -> {
                    while (reader.hasNext()) {
                        if (Symbols.isNewLine(reader.peek())) {
                            return LexemeType.PreprocessorDirective;
                        }
                        reader.next();
                    }
                    return LexemeType.Error;
                }
            ),
            // Char Literal
            new LexemePattern(
                c -> c == '\'',
                (c, reader) -> {
                    var charVal = reader.next();
                    if (charVal == '\'')
                        return LexemeType.Error;

                    if (charVal == '\\')
                        reader.next();

                    var closingQuote = reader.next();
                    if (closingQuote == '\'')
                        return LexemeType.CharLiteral;

                    return LexemeType.Error;
                }
            ),
            // String Literal
            new LexemePattern(
                c -> c == '"' || c == '@' || c =='$',
                (c, reader) -> {
                    var isMultiline = false;
                    if (c != '"') {
                        if (c == '$' && reader.peek() == '@' || c == '@' && reader.peek() == '$') {
                            isMultiline = true;
                            reader.next();
                            if (reader.next() != '"') {
                                return LexemeType.Error;
                            }
                        }
                        else {
                            isMultiline = c == '@';
                            if (reader.next() != '"') {
                                return LexemeType.Error;
                            }
                        }
                    }

                    while(reader.hasNext()) {
                        c = reader.next();
                        if (c == '\\') {
                            reader.next();
                            continue;
                        }
                        if (c == '"') {
                            return isMultiline ? LexemeType.RawStringLiteral : LexemeType.StringLiteral;
                        }
                        if (!isMultiline && Symbols.isNewLine(c)) {
                            return LexemeType.Error;
                        }
                    }
                    return LexemeType.Error;
                }
            ),
            // /
            new LexemePattern(
                c -> c == '/',
                (bracket, reader) -> {
                    // Single Line Comment
                    if (reader.peek() == '/') {
                        reader.next();
                        while (reader.hasNext()) {
                            if (Symbols.isNewLine(reader.next())) {
                                return LexemeType.SingleLineComment;
                            }
                        }
                        return LexemeType.Error;
                    }
                    // Multi Line Comment
                    if (reader.peek() == '*') {
                        reader.next();
                        while (reader.hasNext()) {
                            if (reader.next() == '*' && reader.next() == '/') {
                                return LexemeType.MultiLineComment;
                            }
                        }
                        return LexemeType.Error;
                    }
                    return LexemeType.Operator;
                }
            ),
            // Number Literal
            new LexemePattern(
                Symbols::isNumeric,
                (c, reader) -> {
                    var isDecimal = true;
                    while (reader.hasNext()) {
                        if (Symbols.isNumericModifier(reader.peek())) {
                            reader.next();
                            return LexemeType.NumberLiteral;
                        }
                        if (reader.peek() == '.') {
                            reader.next();
                            if (!isDecimal) return LexemeType.Error;
                            else isDecimal = false;
                        }
                        if (Symbols.isNumeric(reader.peek()) || reader.peek() == 'e') {
                            reader.next();
                            continue;
                        }
                        if (Symbols.isLetter(reader.peek())) {
                            return LexemeType.Error;
                        }
                        return LexemeType.NumberLiteral;
                    }
                    return LexemeType.Error;
                }
            ),
            // Identifier
            new LexemePattern(
                Symbols::isIdentifierSymbol,
                (c, reader) -> {
                    var identifierBuffer = new StringBuilder();
                    identifierBuffer.append(c);
                    while (reader.hasNext()) {
                        if (Symbols.isIdentifierSymbol(reader.peek()) || Symbols.isNumeric(reader.peek())) {
                            identifierBuffer.append(reader.next());
                            continue;
                        }
                        var identifier = identifierBuffer.toString();
                        var isKeyword = Constants.keywords.contains(identifier);
                        return isKeyword ? LexemeType.Keyword : LexemeType.Identifier;
                    }
                    return LexemeType.Error;
                }
            ),
            // Operator
            new LexemePattern(
                c -> Constants.operatorSymbols.containsKey(c),
                (c, reader) -> {
                    var curr = Constants.operatorSymbols.get(c);
                    while (curr.containsKey(reader.peek())) {
                        c = reader.next();
                        curr = curr.get(c);
                    }
                    return LexemeType.Operator;
                }
            )
    };
}

