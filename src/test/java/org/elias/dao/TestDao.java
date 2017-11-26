package org.elias.dao;

import org.elias.SpringBootConsoleApplication;
import org.elias.entity.ProcessResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Filip Elias
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootConsoleApplication.class, loader = AnnotationConfigContextLoader.class)
public class TestDao {

    @Autowired
    ResultsDao resultsDao;

    @Test
    public void testLoadData() {

        List<ProcessResult> data = createData();
        resultsDao.saveResults(data);
        List<ProcessResult> actual= resultsDao.loadResults();
        Assert.assertTrue(actual.equals(data));
    }

    private List<ProcessResult> createData() {
        List<ProcessResult> data = new LinkedList<>();
        ProcessResult res1 = new ProcessResult("first.jpeg");
        res1.addResult("param1", 1);
        res1.addResult("param2", "true");

        ProcessResult res2 = new ProcessResult("second.jpeg");
        res2.addResult("param1", 2);
        res2.addResult("param2", "false");

        data.add(res1);
        data.add(res2);

        return data;
    }

}
