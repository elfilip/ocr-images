package org.elias.service;

import org.elias.dao.ResultsDao;
import org.elias.entity.ProcessResult;
import org.elias.ocr.TesseractOCR;
import org.elias.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Service for file processing
 */
@Service
public class FileService {

    private Logger logger = LoggerFactory.getLogger(FileService.class);

    private List<Processor> processors = new LinkedList<>();

    private List<ProcessResult> results = new LinkedList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private CompletionService<ProcessResult> completionService = new ExecutorCompletionService<>(executor);

    @Autowired
    private ResultsDao resultsDao;

    public void registerProcessor(Processor processor) {
        processors.add(processor);
    }

    public List<ProcessResult> loadData() {

        List<ProcessResult> processResults = resultsDao.loadResults();
        results.addAll(processResults);
        return processResults;
    }

    /**
     * Add file to queue, waiting to be processed. More files can be processed at one time
     * @param file file to be processed
     */
    public void processFile(File file) {
        logger.info("Processing new file: {}", file.getAbsolutePath());
        completionService.submit(new Callable<ProcessResult>() {
            @Override
            public ProcessResult call() throws Exception {
                ProcessResult processResult = new ProcessResult(file.getName());
                for (Processor processor : processors) {
                    processResult.addResults(processor.process(file));
                }
                synchronized (this) {
                    results.add(processResult);
                    resultsDao.saveResults(results);
                }
                return processResult;
            }
        });
    }

    /**
     * Returns one result from one queue
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public ProcessResult getOneProcessResult() throws InterruptedException, ExecutionException {
        logger.info("Waiting for another result");
        Future<ProcessResult> resultFuture = completionService.take();
        return resultFuture.get();
    }

    /**
     * Deletes all results
     */
    public synchronized void deleteAll() {
        results.clear();
        resultsDao.deleteAll();
    }
}
