package org.elias.test.processor;

import org.elias.processor.FileProcessor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * Created by Filip Elias
 */
public class FileProcessorTest {

    private FileProcessor fileProcessor = new FileProcessor();
    private static final String TEST_PATH = "testFile.txt";
    private static File file;

    @BeforeClass
    public static void setupTest() {
        file = new File(FileProcessorTest.class.getClassLoader().getResource(TEST_PATH).getFile());
    }

    @Test
    public void testSize() {
        double size = fileProcessor.getSize(file);
        Assert.assertEquals(0.003, size, 0.001);
    }

}
