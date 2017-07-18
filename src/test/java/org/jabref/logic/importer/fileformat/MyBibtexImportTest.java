package org.jabref.logic.importer.fileformat;

import org.jabref.logic.importer.ImportFormatPreferences;

import org.junit.Before;
import org.mockito.Answers;

import static org.mockito.Mockito.mock;

/**
 * Created by Lucas on 18/07/2017.
 */
public class MyBibtexImportTest {
    private BibtexImporter importer;


    @Before
    public void setUp() {
        importer = new BibtexImporter(mock(ImportFormatPreferences.class, Answers.RETURNS_DEEP_STUBS));
    }
}
