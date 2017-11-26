package org.elias.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Entity with contains all processing results of one file
 *
 */
public class ProcessResult {

    String filename;

    private List<ResultTuple> results = new LinkedList<>();

    public ProcessResult(String filename) {
        this.filename = filename;
    }

    public ProcessResult() {

    }

    public List<ResultTuple> getResults() {
        return results;
    }

    public void addResult(String name, Object value) {
        results.add(new ResultTuple(name, value));
    }

    public void addResults(List<ResultTuple> tuples) {
        results.addAll(tuples);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setResults(List<ResultTuple> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessResult that = (ProcessResult) o;

        if (getFilename() != null ? !getFilename().equals(that.getFilename()) : that.getFilename() != null) return false;
        return getResults() != null ? getResults().equals(that.getResults()) : that.getResults() == null;
    }

    @Override
    public int hashCode() {
        int result = getFilename() != null ? getFilename().hashCode() : 0;
        result = 31 * result + (getResults() != null ? getResults().hashCode() : 0);
        return result;
    }
}
