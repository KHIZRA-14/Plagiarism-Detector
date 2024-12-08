package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class PlagiarismDetectorTest {
    private PlagiarismDetector plagiarismDetector;

    @BeforeEach
    public void setUp() {
        plagiarismDetector = new PlagiarismDetector();
    }

    @Test
    public void testCalculateTextSimilarity() {
        String text1 = "The quick brown fox";
        String text2 = "The quick brown fox jumps over the lazy dog";

        double similarity = Double.parseDouble(plagiarismDetector.calculateTextSimilarity(text1, text2));
        assertTrue(similarity > 0.0, "Similarity should be greater than 0 for some match.");
    }

    @Test
    public void testEmptyTextInputSimilarity() {
        String text1 = "";
        String text2 = "";

        double similarity = Double.parseDouble(plagiarismDetector.calculateTextSimilarity(text1, text2));

        assertEquals(100.0, similarity, "Similarity of two empty texts should be 100%");
    }

    @Test
    public void testCompletelyDifferentTextSimilarity() {
        String text1 = "Hello world";
        String text2 = "Goodbye world";

        double similarity = Double.parseDouble(plagiarismDetector.calculateTextSimilarity(text1, text2));
        assertTrue(similarity < 50.0, "Similarity of two completely different texts should be low.");
    }

}
