import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserBase implements IParser {
    public String[] ParseFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (FileInputStream fin = new FileInputStream(filePath)) {
            byte[] buffer = new byte[fin.available()];
            // считаем файл в буфер
            fin.read(buffer, 0, fin.available());
            for (byte b : buffer) {

                text.append((char) b);
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        String[] result = RawTextToSplitAndCutAndUnique(text.toString());
        Arrays.sort(result, Comparator.comparingInt(String::length));
        return result;
    }

    public String[] RawTextToSplitAndCutAndUnique(String text) {
        Set<String> mySet = new HashSet<>(Arrays.asList(RawTextToWordsOnly(text)));
        Set<String> wordsCut = new HashSet<>();
        Iterator<String> it = mySet.iterator();
        while (it.hasNext()) {
            String word = it.next();
            if (word.length() >= 30) {
                wordsCut.add(word.substring(0, 31));
                it.remove();
            }
        }
        mySet.addAll(wordsCut);
        return mySet.toArray(new String[0]);
    }

    public String[] RawTextToWordsOnly(String text) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(text);
        List<String> allMatches = new ArrayList<>();
        while (matcher.find()) {
            allMatches.add(matcher.group());
        }
        List<String> list = new ArrayList<>();
        for (String x : allMatches) {
            if (!Objects.equals(x, "")) {
                String s = x.toLowerCase();
                list.add(s);
            }
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void Parse(String filePath) {
        String[] text = ParseFile(filePath);
        int i = 0;
        for (String s : text) {
            System.out.print(s + " ");
            i++;
            if (i > 10){
                System.out.println();
                i = 0;
            }
        }
    }
}
