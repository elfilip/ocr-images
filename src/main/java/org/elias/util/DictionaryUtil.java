package org.elias.util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper dict class
 */
public class DictionaryUtil {

    /**
     * Loads list of words from classpath
     * @param path
     * @return set
     * @throws IOException
     */
    public static Set<String> getWordList(String path) throws IOException {
        Set<String> set = new HashSet<>();
        File file = new File(DictionaryUtil.class.getClassLoader().getResource(path).getFile());
        Files.lines(file.toPath()).forEach(line -> set.add(line));
        return set;
    }


}
