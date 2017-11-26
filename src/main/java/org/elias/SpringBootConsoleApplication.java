package org.elias;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elias.gui.FileManager;
import org.elias.ocr.TesseractOCR;
import org.elias.ocr.TesseractProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Main app class. App uses SpringBoot to run the app. Mainly because of DI and testing
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan({"org.elias.dao", "org.elias.gui", "org.elias.processor", "org.elias.service", "org.elias"})
@Import({org.elias.configuration.BeanConfiguration.class})
public class SpringBootConsoleApplication implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(SpringBootConsoleApplication.class);

    @Autowired
    private FileManager manager;

    @Autowired
    private JFrame mainFrame;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplicationBuilder(SpringBootConsoleApplication.class).headless(false).build();
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        mainFrame.setContentPane(manager.getMainPanel());
        manager.init();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

}