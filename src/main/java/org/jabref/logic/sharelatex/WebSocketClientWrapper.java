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

            Thread.sleep(200);

            session.getBasicRemote().sendText(
                    "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"5909edaff31ff96200ef58dd\"}]}");


            Thread.sleep(200);

            session.getBasicRemote().sendText("5:2+::{\"name\":\"joinDoc\",\"args\":[\"5909edb0f31ff96200ef58df\"]}");


            //6:::1+[null,{"_id":"5909edaff31ff96200ef58dd","name":"Test","rootDoc_id":"5909edaff31ff96200ef58de","rootFolder":[{"_id":"5909edaff31ff96200ef58dc","name":"rootFolder","folders":[],"fileRefs":[{"_id":"5909edb0f31ff96200ef58e0","name":"universe.jpg"},{"_id":"59118cae98ba55690073c2a0","name":"all2.ris"}],"docs":[{"_id":"5909edaff31ff96200ef58de","name":"main.tex"},{"_id":"5909edb0f31ff96200ef58df","name":"references.bib"},{"_id":"5911801698ba55690073c29c","name":"aaaaaaaaaaaaaa.bib"}]}],"publicAccesLevel":"private","dropboxEnabled":false,"compiler":"pdflatex","description":"","spellCheckLanguage":"en","deletedByExternalDataSource":false,"deletedDocs":[],"members":[{"_id":"5912e195a303b468002eaad0","first_name":"jim","last_name":"","email":"jim@example.com","privileges":"readAndWrite","signUpDate":"2017-05-10T09:47:01.325Z"}],"invites":[],"owner":{"_id":"5909ed80761dc10a01f7abc0","first_name":"joe","last_name":"","email":"joe@example.com","privileges":"owner","signUpDate":"2017-05-03T14:47:28.665Z"},"features":{"trackChanges":true,"references":true,"templates":true,"compileGroup":"standard","compileTimeout":180,"github":false,"dropbox":true,"versioning":true,"collaborators":-1,"trackChangesVisible":false}},"owner",2]
            //Finden der ID mit der bib die der active database entspricht
            //idee: EntrySet stream

            //5:::{"name":"otUpdateApplied","args":[{"doc":"5909edb0f31ff96200ef58df","op":[{"d":"nov","p":223},{"i":"December","p":223}],"v":8,"meta":{"type":"external","source":"upload","user_id":"5912e195a303b468002eaad0","ts":1496736413706}}]}

            // Die ersten x-Zeichen sind immer code + ::
            // danach kommt das argument und wichtig die doc id
            //die doc id muss ich abgleichen
            //am anfang kommt aber ja erst noch das

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
