package org.elias.util;

/**
 * ORCProcessing exception used for OCR errors
 */
public class OCRProcessingException extends Exception {

    public OCRProcessingException(String message) {
        super(message);
    }

    public OCRProcessingException(String message, Throwable e) {
        super(message, e);
    }

}
