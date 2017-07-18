package org.jabref.logic.importer.fileformat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.jabref.logic.util.FileExtensions;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class RISImporterTest {

    private RisImporter importer;

    @Test
    public void testImportEntries() throws IOException, URISyntaxException {
        Path file = Paths.get(BibtexImporterTest.class.getResource("RisImporterTest4b.ris").toURI());
        BufferedReader utf8Reader = importer.getReader(file, StandardCharsets.UTF_8);
        List<BibEntry> bibEntries = importer.importDatabase(utf8Reader).getDatabase().getEntries();

        assertEquals(1, bibEntries.size());

        for (BibEntry entry : bibEntries) {
            assertEquals("book", entry.getType());
            assertEquals(Optional.of("Robinson, W. F."), entry.getField(FieldName.AUTHOR));
            assertEquals(Optional.of("Huxtable, C. R. R."), entry.getField(FieldName.EDITOR));
            assertEquals(Optional.of("Clinicopathologic Principles For Veterinary Medicine"), entry.getField(FieldName.TITLE));
            assertEquals(Optional.of("Cambridge University Press"), entry.getField(FieldName.PUBLISHER));
            assertEquals(Optional.of("1988"), entry.getField(FieldName.YEAR));
            assertEquals(Optional.of("Cambridge"), entry.getField(FieldName.ADDRESS));
            assertEquals(Optional.of("robinson"), entry.getField("refid"));
        }
    }

    @Before
    public void setUp() {
        importer = new RisImporter();
    }

    @Test
    public void testGetFormatName() {
        Assert.assertEquals(importer.getName(), "RIS");
    }

    @Test
    public void testGetCLIId() {
        Assert.assertEquals(importer.getId(), "ris");
    }

    @Test
    public void testsGetExtensions() {
        Assert.assertEquals(FileExtensions.RIS, importer.getExtensions());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Imports a Biblioscape Tag File.", importer.getDescription());
    }

    @Test
    public void testIfNotRecognizedFormat() throws IOException, URISyntaxException {
        Path file = Paths.get(RISImporterTest.class.getResource("RisImporterCorrupted.ris").toURI());
        Assert.assertFalse(importer.isRecognizedFormat(file, StandardCharsets.UTF_8));
    }

}
