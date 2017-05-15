package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketClientWrapper {

    private static final String SENT_MESSAGE = "Hello World";
    final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

    public static void createAndConnect(String channel)
    {



        CountDownLatch messageLatch;
        try {
            messageLatch = new CountDownLatch(1);

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

            ClientManager client = ClientManager.createClient();


            client.connectToServer(new Endpoint() {


                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    //We get as Payload back  5:::{"name":"connectionAccepted"}
                    //Not sure how I can accces that
                    // Seems like it is MQTT over websockets!
                    try {
                        System.out.println("On Open and is Open " + session.isOpen());
                        session.addMessageHandler((Whole<String>) message -> {
                            System.out.println("Received message: " + message);
                            messageLatch.countDown();
                        });
                        session.getBasicRemote().sendText(SENT_MESSAGE);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));
            messageLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
