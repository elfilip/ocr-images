package org.elias.ocr;

import org.elias.util.OCRProcessingException;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * OCR processing
 */
@Component
public interface TesseractOCR {

    /**
     * Performs ORC processing
     *
     * @param input Image to be converted to text
     * @return String
     * @throws OCRProcessingException
     */
    String doOCR(File input) throws OCRProcessingException;

}
