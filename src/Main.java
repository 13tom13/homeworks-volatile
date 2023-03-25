import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger beautifulWordsWithLength3 = new AtomicInteger();
    static AtomicInteger beautifulWordsWithLength4 = new AtomicInteger();
    static AtomicInteger beautifulWordsWithLength5 = new AtomicInteger();

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        List<Thread> threads = new ArrayList<>();
        Thread palindrome =new Thread(() -> {
            for (String word : texts) {
                boolean palind = true;
                char letter = word.charAt(0);
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) != letter) {
                        palind = true;
                        break;
                    }
                }
                for (int i = 0, j = word.length() - 1; i < j; i++, j--) {
                    if (word.charAt(i) != word.charAt(j)) {
                        palind = false;
                        break;
                    }
                }
                if (palind) {
                    if (word.length() == 3) beautifulWordsWithLength3.getAndIncrement();
                    if (word.length() == 4) beautifulWordsWithLength4.getAndIncrement();
                    if (word.length() == 5) beautifulWordsWithLength5.getAndIncrement();
                }
            }
        });
        threads.add(palindrome);

        Thread oneLetter = new Thread(() -> {
            for (String word : texts) {
                char letter = word.charAt(0);
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) != letter) break;
                    if (i == word.length() - 1) {
                        if (word.length() == 3) beautifulWordsWithLength3.getAndIncrement();
                        if (word.length() == 4) beautifulWordsWithLength4.getAndIncrement();
                        if (word.length() == 5) beautifulWordsWithLength5.getAndIncrement();
                    }
                }
            }
        });
        threads.add(oneLetter);

        Thread inOrder = new Thread(() -> {
            for (String word : texts) {
                char letterStart = word.charAt(0);
                char letter = letterStart;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == letter || word.charAt(i) == letter + 1) {
                        if (word.charAt(i) != letter) letter = word.charAt(i);
                        if (i == word.length() - 1 && letter != letterStart) {
                            if (word.length() == 3) beautifulWordsWithLength3.getAndIncrement();
                            if (word.length() == 4) beautifulWordsWithLength4.getAndIncrement();
                            if (word.length() == 5) beautifulWordsWithLength5.getAndIncrement();
                        }
                    } else {
                        break;
                    }
                }
            }
        });
        threads.add(inOrder);

        Thread finish = new Thread(() -> {
            System.out.println("Красивых слов с длиной 3: " + beautifulWordsWithLength3.get() + " шт");
            System.out.println("Красивых слов с длиной 4: " + beautifulWordsWithLength4.get() + " шт");
            System.out.println("Красивых слов с длиной 5: " + beautifulWordsWithLength5.get() + " шт");
        });
        threads.add(finish);

        for (Thread thread : threads){
            thread.start();
            thread.join();
        }
    }
}
