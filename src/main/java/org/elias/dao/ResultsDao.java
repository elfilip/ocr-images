package org.elias.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elias.entity.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * DAO class for saving processing results to JSON document
 */
@Component
public class ResultsDao {

    private Logger logger = LoggerFactory.getLogger(ResultsDao.class);
    public static final String filePath = "./data.json";
    public static final File fileStore = new File(filePath);


    @Autowired
    ObjectMapper objectMapper;


    /**
     * Saves all results to json document. Previous results are discarded
     *
     * @param results List with results to be saved
     */
    public void saveResults(List<ProcessResult> results) {
        logger.info("Saving results");
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(fileStore, results);

        } catch (JsonProcessingException e) {
            logger.error("Can't parse Json", e);
        } catch (IOException e) {
            logger.error("Can't load file", e);
        }

    }

    /**
     * Loads all results from JSON file.
     *
     * @return list of all results
     */
    public List<ProcessResult> loadResults() {
        logger.info("Loading all results");
        if (!fileStore.exists()) {
            return new LinkedList<>();
        }
        TypeReference<List<ProcessResult>> mapType = new TypeReference<List<ProcessResult>>() {
        };
        try {
            List<ProcessResult> jsonToPersonList = objectMapper.readValue(fileStore, mapType);
            logger.info("Loaded {} results", jsonToPersonList.size());
            return jsonToPersonList;
        } catch (IOException e) {
            logger.error("Can't save file", e);
        }
        return null;
    }

    /**
     * Deletes all results by creating empty JSON doc
     *
     */
    public void deleteAll() {
        logger.info("Deleting all results");
        if (fileStore.exists()) {
            saveResults(new LinkedList<>());
        }
        logger.info("All results deleted");
    }

}
