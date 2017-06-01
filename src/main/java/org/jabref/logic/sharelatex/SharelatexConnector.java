package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SharelatexConnector {

    private final String contentType = "application/json; charset=utf-8";
    private final JsonParser parser = new JsonParser();
    private Map<String, String> loginCookies = new HashMap<>();
    private String server;
    private String loginUrl;
    private String csrfToken;
    private String projectUrl;

    public String connectToServer(String server, String user, String password) throws IOException {

        this.server = server;
        this.loginUrl = server + "/login";
        Connection.Response crsfResponse;

        crsfResponse = Jsoup.connect(loginUrl).method(Method.GET)
                .execute();

        Document welcomePage = crsfResponse.parse();
        Map<String, String> welcomCookies = crsfResponse.cookies();

        csrfToken = welcomePage.select("input[name=_csrf]").attr("value");

        String json = "{\"_csrf\":" + JSONObject.quote(csrfToken)
                + ",\"email\":" + JSONObject.quote(user) + ",\"password\":" + JSONObject.quote(password) + "}";

        Connection.Response loginResponse = Jsoup.connect(loginUrl)
                .header("Content-Type", contentType)
                .header("Accept", "application/json, text/plain, */*")
                .cookies(welcomCookies)
                .method(Method.POST)
                .requestBody(json)
                .followRedirects(true)
                .ignoreContentType(true)
                .execute();

        System.out.println(loginResponse.body());
        ///Error handling block
        if (contentType.equals(loginResponse.contentType())) {

            if (loginResponse.body().contains("message")) {
                JsonElement jsonTree = parser.parse(loginResponse.body());
                JsonObject obj = jsonTree.getAsJsonObject();
                JsonObject message = obj.get("message").getAsJsonObject();
                String errorMessage = message.get("text").getAsString();
                System.out.println(errorMessage);

                return errorMessage;
            }

        }

        loginCookies = loginResponse.cookies();

        return "";
    }

    public Optional<JsonObject> getProjects() throws IOException {
        projectUrl = server + "/project";
        Connection.Response projectsResponse = Jsoup.connect(projectUrl)
                .referrer(loginUrl).cookies(loginCookies).method(Method.GET).execute();

        System.out.println("");

        Optional<Element> scriptContent = Optional
                .of(projectsResponse.parse().select("script#data").first());

        if (scriptContent.isPresent()) {

            String data = scriptContent.get().data();
            JsonElement jsonTree = parser.parse(data);

            JsonObject obj = jsonTree.getAsJsonObject();

            return Optional.of(obj);

        }
        return Optional.empty();
    }

    public void uploadFile(String projectId, Path path) {

        String activeProject = projectUrl + "/" + projectId + "/upload";
        InputStream str;
        try {
            String urlWithParms = activeProject + "?folder_id=" + projectId + "&_csrf=" + csrfToken
                    + "&qquuid=a71abbbe-d4ba-4918-b52e-b2b4221851e7" + "&qqtotalfilesize="
                    + Long.toString(Files.size(path));

            str = Files.newInputStream(path);

            Connection.Response fileResp = Jsoup.connect(urlWithParms).cookies(loginCookies)
                    .header("Host", "192.168.1.248")
                    .header("Accept", "*/*")
                    .data("qqfile", path.getFileName().toString(), str)
                    .cookies(loginCookies).ignoreContentType(true)
                    .method(Method.POST).execute();

            //TOD: Investigate why they also get send as multipart form request
            System.out.println(fileResp.body());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub

        /*
        try {


            //  System.out.println(loginCookies);
            loginCookies.forEach((x, y) -> System.out.println(x + " " + y));



            BasicCookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("sharelatex.sid", loginCookies.get("sharelatex.sid"));
            cookie.setDomain("*.192.168.1.248");
            cookie.setPath("/");
            cookieStore.addCookie(cookie);


            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore)
                    .setDefaultRequestConfig(RequestConfig).build();

            HttpPost httpPost = new HttpPost(urlWithParms);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("qqfile", path.toFile(),
                    ContentType.APPLICATION_OCTET_STREAM, path.getFileName().toString());

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);


            CloseableHttpResponse response = client.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */

    }
}

/*  for (JsonElement elem : projectArray) {

      System.out.println("ID " + elem.getAsJsonObject().get("id").getAsString());
      System.out.println("Name " + elem.getAsJsonObject().get("name").getAsString());

  }


  long millis = System.currentTimeMillis();
  System.out.println(millis);
  try {
      Connection.Response webSocketresponse = Jsoup.connect("http://192.168.1.248/socket.io/1")
              .cookies(loginCookies)
              .data("t", String.valueOf(millis)).method(Method.GET).execute();

      System.out.println(webSocketresponse.body());

      String resp = webSocketresponse.body();
      String channel = resp.substring(0, resp.indexOf(":"));
      System.out.println("Channel " + channel);


      WebSocketClientWrapper.createAndConnect(channel);
      // MqttPublishSample.connect(channel);

  } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
  }

});

//script tag parsen
//Json parsen mit den Projects
*/
