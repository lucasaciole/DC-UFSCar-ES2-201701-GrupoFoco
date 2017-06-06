package org.jabref.logic.sharelatex;

import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;

import org.glassfish.tyrus.core.coder.CoderAdapter;

/**
 * @author Danny Coward (danny.coward at oracle.com)
 */
public class JsonEncoder extends CoderAdapter implements Encoder.Text<JsonObject> {

    @Override
    public String encode(JsonObject o) throws EncodeException {
        return o.toString();
    }
}