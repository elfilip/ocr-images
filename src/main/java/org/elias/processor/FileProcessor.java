package org.elias.processor;

import org.elias.entity.ResultTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

/**
 * Processor for basic file attributes like name, size, date
 */
public class FileProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(FileProcessor.class);

    public static String SIZE_NAME = "Size(MB)";
    public static String FILENAME_NAME = "Name";
    public static String CREATED_NAME = "Created";


    /**
     * @return size of a file
     */
    public double getSize(File target) {
        double megabytes = target.length() / (double) (1024 * 1024);
        megabytes = (double) Math.round(megabytes * 1000d) / 1000d;
        return megabytes;
    }

    /**
     * @return name of a file
     */
    public String getName(File target) {
        return target.getName();
    }

    /**
     * @return creation date of a file
     * @throws IOException
     */
    public String getCreatedDate(File target) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(target.toPath(), BasicFileAttributes.class);
        return LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault()).toString();
    }

    @Override
    public List<ResultTuple> process(File file) {
        logger.debug("Processing file using {} processor", FileProcessor.class.getName());
        List<ResultTuple> result = new LinkedList<>();
        result.add(new ResultTuple(FILENAME_NAME, getName(file)));
        result.add(new ResultTuple(SIZE_NAME, getSize(file)));
        try {
            result.add(new ResultTuple(CREATED_NAME, getCreatedDate(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
