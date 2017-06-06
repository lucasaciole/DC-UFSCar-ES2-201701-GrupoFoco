package org.jabref.logic.sharelatex;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;

import org.glassfish.tyrus.core.coder.CoderAdapter;

/**
 * @author Danny Coward (danny.coward at oracle.com)
 */
public class JsonDecoder extends CoderAdapter implements Decoder.Text<JsonObject> {

    @Override
    public JsonObject decode(String s) throws DecodeException {
        try {
            JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
            return jsonObject;
        } catch (JsonException je) {
            throw new DecodeException(s, "JSON not decoded", je);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}