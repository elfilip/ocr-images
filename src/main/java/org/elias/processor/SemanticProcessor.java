package org.elias.processor;

import org.elias.entity.ResultTuple;
import org.elias.ocr.TesseractOCR;
import org.elias.ocr.TesseractProcess;
import org.elias.util.DictionaryUtil;
import org.elias.util.OCRProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Processor extract params from the text itself.
 */
public class SemanticProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SemanticProcessor.class);

    public static String LINES_NAME = "Line Count";
    public static String FROM_NAME = "From";
    public static String TO_NAME = "To";
    public static String DATE_NAME = "Date";
    public static String TOPIC_NAME = "Topic";
    private static String PATH_SIGNOFFS = "dict/farewell_dict.txt";
    private static String PATH_STOPWORDS = "dict/stopwords.txt";
    private static String PATH_GREETINGS = "dict/greeting_dict.txt";


    private Set<String> signoffs;

    private Set<String> stopWords;


    private Pattern greeting_pattern;
    private String greeting_regex = "\\s*(#1)\\s+([^,\\s]*),*";
    private Pattern date_pattern = Pattern.compile(".*((19)|(20))\\d\\d.*");
    private TesseractOCR ocr;

    public SemanticProcessor(TesseractOCR ocr) {
        this.ocr = ocr;
    }

    @Override
    public List<ResultTuple> process(File file) throws OCRProcessingException {
        logger.debug("Processing file using {} processor", SemanticProcessor.class.getName());
        String content = null;
        content = ocr.doOCR(file);
        List<ResultTuple> resultTuples = new LinkedList<>();
        addToResult(resultTuples, LINES_NAME, getNumberOfLines(content));
        addToResult(resultTuples, FROM_NAME, getFrom(content));
        addToResult(resultTuples, TO_NAME, getTo(content));
        addToResult(resultTuples, DATE_NAME, getDate(content));
        addToResult(resultTuples, TOPIC_NAME, getTopics(content));

        return resultTuples;
    }

    /**
     * @return number of lines in the document
     */
    public int getNumberOfLines(String content) {

        int lines = 1;
        if (content == null || content.isEmpty()) {
            return 0;
        }
        String separator = System.getProperty("line.separator");

        int index = 0;
        while ((index = content.indexOf(separator, index) + 1) != 0) {
            lines++;
        }
        logger.debug("Number of lines is {}", lines);
        return lines;
    }

    /**
     * @return searches the author of the document
     */
    public String getFrom(String content) {
        try {
            synchronized (this) {
                if (signoffs == null) {

                    signoffs = DictionaryUtil.getWordList(PATH_SIGNOFFS);
                }
            }
        } catch (IOException e) {
            logger.error("Can't load dictionary", e);
            return null;
        }
        logger.debug("Getting from param");

        String[] lines = content.split(System.getProperty("line.separator"));
        int lineWithSignoff = -1;
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i];
            if (line.trim().length() > 1 && line.charAt(line.length() - 1) == ',') {
                line = line.substring(0, line.length() - 1);
            }
            if (signoffs.contains(line.trim().toLowerCase())) {
                lineWithSignoff = i;
                logger.debug("Signoff found: {}, Line: {}", line, i);
                break;
            }
        }

        if (lineWithSignoff == -1) {
            return null;
        }

        logger.debug("Looking for name");
        for (int i = lineWithSignoff + 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                logger.debug("Name found: {}", line);
                return line;
            }

        }

        return null;
    }

    /**
     * @return searches the recipient of the document
     */
    public String getTo(String content) {
        logger.debug("Looking for to using regular expression");
        try {
            synchronized (this) {
                if (greeting_pattern == null) {
                    createRegexForTo();
                }
                Matcher matcher = greeting_pattern.matcher(content);
                if (matcher.find()) {
                    return matcher.group(2);
                }
            }
        } catch (IOException e) {
            logger.error("Can't load dictionary with greetings", e);
            return null;
        }
        return null;
    }

    /**
     * Return 3 top used words in the document ignoring stopwords
     *
     * @param content
     * @return
     */
    public String getTopics(String content) {
        logger.debug("Looking for topics");
        synchronized (this) {
            if (stopWords == null) {
                try {
                    stopWords = DictionaryUtil.getWordList(PATH_STOPWORDS);
                } catch (IOException e) {
                    logger.error("Error whan loading stopwords", e);
                    return null;
                }
            }
        }

        String[] words = content.toLowerCase().split("\\W");

        if (words.length < 3) {
            return null;
        }

        Map<String, Integer> count = Arrays.stream(words).filter(word -> isWordValid(word)).
                collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(num -> 1)));

        List<String> top = count.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3).map(e -> e.getKey()).collect(Collectors.toList());

        return top.stream().reduce("", (a, b) -> a + " " + b);


    }

    private boolean isWordValid(String word) {
        if (word.trim().length() < 3) {
            return false;
        }
        else if (word.matches(".*\\d.*")) {
            return false;
        } else if (stopWords.contains(word)) {
            return false;
        }
        return true;
    }

    public String getDate(String content) {
        logger.debug("Looking for to using regular expression");
        Matcher matcher = date_pattern.matcher(content);
        if (matcher.find()) {
            String date = matcher.group(0);
            return date;
        }
        return null;

    }

    private void addToResult(List<ResultTuple> resultTuples, String name, Object value) {
        if (value == null) {
            return;
        } else {
            resultTuples.add(new ResultTuple(name, value));
        }
    }

    private void createRegexForTo() throws IOException {

        String words = DictionaryUtil.getWordList(PATH_GREETINGS).stream().reduce("", (str1, str2) -> {
            if (str1.isEmpty())
                return str2;
            else if (str2.isEmpty())
                return str1;
            else
                return str1 + '|' + str2;
        });
        greeting_pattern = Pattern.compile(greeting_regex.replace("#1", words), Pattern.CASE_INSENSITIVE);

    }

}
