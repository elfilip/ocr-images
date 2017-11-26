package org.elias.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * OCR processing using Java JNA wrapper for Teserract. Bad performance.
 */
public class TesseractTess4J implements TesseractOCR {

    Logger logger = LoggerFactory.getLogger(TesseractTess4J.class);


    @Override
    public String doOCR(File input) {
        logger.debug("Doing OCR for file: {}", input.getAbsolutePath());
        ITesseract instance = new Tesseract();
        instance.setLanguage("eng");
        instance.setOcrEngineMode(3);

        try {
            String result = instance.doOCR(input);
            logger.info("text je {}", result);
            return result;
        } catch (TesseractException e) {
            logger.error("Exception when parsing image: ", e);
            return null;

        }
    }
}
