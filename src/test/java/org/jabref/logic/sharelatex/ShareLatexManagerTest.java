package org.jabref.logic.sharelatex;

import java.util.List;

import org.jabref.gui.sharelatex.ShareLatexProjectViewModel;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ShareLatexManagerTest {

    @Test
    public void test() {
        ShareLatexManager manager = new ShareLatexManager();
        manager.login("http://192.168.1.248", "joe@example.com", "test");

        List<ShareLatexProjectViewModel> projects = manager.getProjects();
        assertFalse(projects.isEmpty());
    }
}
