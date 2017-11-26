package org.elias.processor;

import org.elias.entity.ResultTuple;

import java.io.File;
import java.util.List;

/**
 * Interface for processing image. MUST BE THREAD SAFE.
 */
public interface Processor {

    /**
     * Process image and extract arbitrary number of parameters
     * @param file file to be processed
     * @return Processing results
     */
    List<ResultTuple> process(File file);


}
