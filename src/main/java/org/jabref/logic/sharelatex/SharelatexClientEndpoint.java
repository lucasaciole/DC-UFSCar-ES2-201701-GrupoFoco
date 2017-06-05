package org.jabref.logic.sharelatex;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SharelatexClientEndpoint {

    @OnMessage
    public void processMessageFromServer(String message, Session session) {
        System.out.println("Message came from the server ! " + message);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Session opened");

    }

}
