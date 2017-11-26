package org.elias.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Helper String class
 */
public class StringUtils {


    /**
     * Converts file to String
     *
     * @param path
     * @return converted file
     * @throws IOException
     */
    static public String convertFileToString(File path) throws IOException {
        return new String(Files.readAllBytes(path.toPath()), Charset.defaultCharset());
    }

    /**
     * Converts stream to string
     *
     * @param inputStream
     * @return
     */
    static public String convertStreamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String res = s.hasNext() ? s.next() : "";
        return res;
    }

}
