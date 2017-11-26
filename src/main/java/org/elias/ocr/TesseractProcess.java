package org.elias.ocr;

import org.elias.util.OCRProcessingException;
import org.elias.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * OCR processing by calling external tesseract process. Mainly for Linux. Tesseract must be on classpath.
 */
public class TesseractProcess implements TesseractOCR {

    Logger logger = LoggerFactory.getLogger(TesseractProcess.class);
    private static final String TESS_COMMAND = "tesseract #1 stdout  -l eng";

    public TesseractProcess() {

    }

    public String doOCR(File image) throws OCRProcessingException {
        String command = TESS_COMMAND.replace("#1", image.getAbsolutePath());
        logger.debug("Doing ocr for file: {}", image.getAbsolutePath());
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            String res = StringUtils.convertStreamToString(p.getInputStream());
            logger.error(StringUtils.convertStreamToString(p.getErrorStream()));
            if (res == null || res.isEmpty()) {
                throw new OCRProcessingException("Can't parse image.");
            }
            return res;
        } catch (IOException e) {
            logger.error("I/O error: ", e);
            throw new OCRProcessingException("Can't load image.", e);


        } catch (InterruptedException e) {
            logger.warn("Int. exception", e);
            throw new OCRProcessingException("Interrupted.", e);
        }
    }
}
