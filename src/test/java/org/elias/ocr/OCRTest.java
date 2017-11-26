package org.elias.ocr;

import org.elias.test.processor.FileProcessorTest;
import org.elias.util.OCRProcessingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * Created by Filip Elias
 */
public class OCRTest {

    private static File image;
    private static final String TEST_PATH = "translation-request.png";
    private TesseractOCR tesseractOCR = new TesseractProcess();

    @BeforeClass
    public static void loadImage() {
        image = new File(FileProcessorTest.class.getClassLoader().getResource(TEST_PATH).getFile());
    }

    @Test
    public void testOCR() throws OCRProcessingException {
        String content = tesseractOCR.doOCR(image);
        int index = content.indexOf("Subject: Translation Services Request");
        Assert.assertTrue("Substring was not found", index != -1 );
        int index2 = content.indexOf("LONDON");
        Assert.assertTrue("Substring was not found", index2 != -1 );
        int index3 = content.indexOf("4th November");
        Assert.assertTrue("Substring was not found", index3 != -1 );
    }

}
