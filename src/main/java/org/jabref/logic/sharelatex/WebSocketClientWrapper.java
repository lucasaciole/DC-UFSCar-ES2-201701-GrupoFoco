package org.jabref.logic.sharelatex;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketClientWrapper {

    private static final String SENT_MESSAGE = "Hello_World";

    public static void createAndConnect(String channel) {

        CountDownLatch messageLatch;
        try {
            messageLatch = new CountDownLatch(1);

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .preferredSubprotocols(Arrays.asList("mqttt")).build();
            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    System.out.println("On Open and is Open " + session.isOpen());

                    session.addMessageHandler((Whole<String>) message -> {
                        System.out.println("Received message: " + message);
                        messageLatch.countDown();
                    });
                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));
            messageLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
