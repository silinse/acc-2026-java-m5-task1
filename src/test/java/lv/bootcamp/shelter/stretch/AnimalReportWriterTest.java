package lv.bootcamp.shelter.stretch;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Stretch goal: Testing file output
 * <p>
 * Practice:
 * - Writing to temp files and reading them back
 * - String content assertions
 * - Cleanup with Files.deleteIfExists
 * <p>
 * Instructions:
 * These tests verify that AnimalReportWriter produces correct output.
 * This task is optional — attempt it after completing tasks 1–6.
 */
@DisplayName("AnimalReportWriter (stretch)")
class AnimalReportWriterTest {

    private final AnimalReportWriter writer = new AnimalReportWriter();

    @Test
    @DisplayName("writes report file that contains total count")
    void shouldWriteTotalCount() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Rex", "Dog", 3, true, LocalDate.now()),
                new Animal("Pluto", "Dog", 3, false, LocalDate.now()),
                new Animal("Spot", "Cat", 9, true, LocalDate.now())
        );
        Path output = Files.createTempFile("report-test", ".txt");
        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);

        assertThat(content).contains("Total animals: 3");

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes per-species breakdown in alphabetical order")
    void shouldWriteSpeciesBreakdown() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Pluto", "Dog", 3, false, LocalDate.now()),
                new Animal("Spot", "Cat", 9, true, LocalDate.now())
        );
        Path output = Files.createTempFile("report-test", ".txt");
        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);

        assertThat(content).containsSubsequence("Cat:", "Dog:")
                .contains("Cat: 1 total, 1 vaccinated", "Dog: 1 total, 0 vaccinated");

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes oldest animal per species")
    void shouldWriteOldestPerSpecies() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Pluto", "Dog", 3, true, LocalDate.now()),
                new Animal("Max", "Dog", 5, false, LocalDate.now()),
                new Animal("Spot", "Cat", 2, true, LocalDate.now())
        );
        Path output = Files.createTempFile("report-test", ".txt");
        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);

        assertThat(content).contains("Dog: Max (age 5)");

        Files.deleteIfExists(output);
    }
}
