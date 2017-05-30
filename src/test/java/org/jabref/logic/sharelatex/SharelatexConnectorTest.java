package org.jabref.logic.sharelatex;

import java.io.IOException;
import org.junit.Test;

public class SharelatexConnectorTest {

    @Test
    public void test() throws IOException {
        SharelatexConnector connector = new SharelatexConnector();
        connector.connectToServer("http://192.168.1.248", "joe@example.com", "test");
        connector.getProjects();
        //  connector.uploadFile("591188ed98ba55690073c29e", Paths.get("X:\\Users\\Christoph Schwentker\\Uni\\git.txt"));
    }

}
