package org.elias.test.processor;

import org.elias.ocr.TesseractProcess;
import org.elias.processor.SemanticProcessor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Filip Elias
 */
public class SemanticProcessorTest {

    SemanticProcessor semanticProcessor = new SemanticProcessor(new TesseractProcess());

    @Test
    public void testLinesZero() {
        int lines=semanticProcessor.getNumberOfLines("");
        Assert.assertEquals(0, lines);
    }

    @Test
    public void testLinesOne() {
        int lines=semanticProcessor.getNumberOfLines("a");
        Assert.assertEquals(1, lines);
    }

    @Test
    public void testLinesX() {
        int lines=semanticProcessor.getNumberOfLines("aa\nbb\ncc\nvv");
        Assert.assertEquals(4, lines);
    }

    @Test
    public void testFromRegards() {
        String text="regards,\n Filip";
        String name = semanticProcessor.getFrom(text);
        Assert.assertEquals("Filip", name);
    }

    @Test
    public void testFromCapital() {
        String text="REGARDS,\n Filip";
        String name = semanticProcessor.getFrom(text);
        Assert.assertEquals("Filip", name);
    }

    @Test
    public void testFromNegative() {
        String text="regarding,\n Filip";
        String name = semanticProcessor.getFrom(text);
        Assert.assertNull(name);
    }

    @Test
    public void testFromMoreLines() {
        String text="regards,\n\n\n Filip";
        String name = semanticProcessor.getFrom(text);
        Assert.assertEquals("Filip", name);
    }

    @Test
    public void testTo() {
        String text = "Dear Filip";
        String name = semanticProcessor.getTo(text);
        Assert.assertEquals("Filip", name);
    }

    @Test
    public void testToComma() {
        String text = "Dear Filip,";
        String name = semanticProcessor.getTo(text);
        Assert.assertEquals("Filip", name);
    }

    @Test
    public void testToNegative() {
        String text = "Dearing Filip,";
        String name = semanticProcessor.getTo(text);
        Assert.assertNull(name);
    }


}
