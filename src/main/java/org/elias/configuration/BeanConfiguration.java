package org.elias.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elias.ocr.TesseractOCR;
import org.elias.ocr.TesseractProcess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

/**
 * Created by Filip Elias on 26.11.17.
 */
@Configuration
public class BeanConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TesseractOCR tesseractOCR() {
        return new TesseractProcess();
    }

    @Bean
    public JFrame getFrame() {
        return new JFrame("FileManager");
    }
}
