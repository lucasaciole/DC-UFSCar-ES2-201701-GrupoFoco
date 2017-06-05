package org.jabref.logic.sharelatex;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketClientWrapper {

    public static void createAndConnect(String channel) {

        CountDownLatch messageLatch = new CountDownLatch(1);
        try {

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .preferredSubprotocols(Arrays.asList("mqttt")).build();
            ClientManager client = ClientManager.createClient();

            SharelatexClientEndpoint endpoint = new SharelatexClientEndpoint();
            Session session = client.connectToServer(endpoint,
                    new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));

            Thread.sleep(100);

            session.getBasicRemote().sendText(
                    "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"5909edaff31ff96200ef58dd\"}]}");



            session.getBasicRemote().sendText("5:2+::{\"name\":\"joinDoc\",\"args\":[\"5909edb0f31ff96200ef58df\"]}");


            /*   client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    System.out.println("On Open and is Open " + session.isOpen());

                    session.addMessageHandler(String.class, (Whole<String>) message -> {
                        System.out.println("Received message: " + message);

                    });

                    try {
                        session.getBasicRemote().sendText(
                                "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"5909edaff31ff96200ef58dd\"}]}");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("Sent");

                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));        */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
