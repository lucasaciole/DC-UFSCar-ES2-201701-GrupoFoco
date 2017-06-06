package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
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
    private final String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:53.0) Gecko/20100101 Firefox/53.0";
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
                .userAgent(userAgent)
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
                .referrer(loginUrl).cookies(loginCookies).method(Method.GET).userAgent(userAgent).execute();

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

    public void startWebsocketListener() {
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
    }

    public void uploadFileWithWebClient(String projectId, Path path)
    {
        String activeProject = projectUrl + "/" + projectId;
        String uploadUrl = activeProject + "/upload";

        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(true);
        webClient.waitForBackgroundJavaScriptStartingBefore(100);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        webClient.getCookieManager()
                .addCookie(new Cookie("192.168.1.248", "sharelatex.sid", loginCookies.get("sharelatex.sid")));

        webClient.addRequestHeader("Referer", loginUrl);
        HtmlPage p;
        try {
            p = webClient.getPage(activeProject);
            System.out.println(p.getWebResponse().getContentAsString());


        Optional<HtmlAnchor> anchor = p.getAnchors().stream()
                .filter(a -> a.getAttribute("ng-click").equals("openUploadFileModal()")).findFirst();

        anchor.ifPresent(x -> {
            HtmlPage uploadPage;
            try {
                uploadPage = x.click();
                    webClient.waitForBackgroundJavaScript(1000);
                    System.out.println("Clickedd!\r\n");
                    System.out.println(uploadPage.getWebResponse().getContentAsString());
                    //  System.out.println(uploadPage.getElementByName("file"));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        webClient.close();
        } catch (FailingHttpStatusCodeException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }



    public void uploadFile(String projectId, Path path) {
        String activeProject = projectUrl + "/" + projectId;
        String uploadUrl = activeProject + "/upload";

        try {
            InputStream str;

            String urlWithParms = uploadUrl + "?folder_id=" + projectId + "&_csrf=" + csrfToken
                    + "&qquuid=28774ed2-ae25-44f1-9388-c78f1c6b8286" + "&qqtotalfilesize="
                    + Long.toString(Files.size(path));

            str = Files.newInputStream(path);

            Connection.Response fileResp = Jsoup.connect(urlWithParms).cookies(loginCookies)
                    .header("Host", "192.168.1.248")
                    .header("Accept", "*/*")
                    .header("Accept-Language", "Accept-Language: de,en-US;q=0.7,en;q=0.3")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Cache-Control", "no-cache")
                    .data("qqfile", path.getFileName().toString(), str)
                    .cookies(loginCookies).ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0")
                    .method(Method.POST).execute();

            //TOD: Investigate why they also get send as multipart form request
            System.out.println(fileResp.body());

            /*         WebClient webClient = new WebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getCookieManager()
                    .addCookie(new Cookie("192.168.1.248", "sharelatex.sid", loginCookies.get("sharelatex.sid")));

            HtmlPage p = webClient.getPage(activeProject);

            Optional<HtmlAnchor> anchor = p.getAnchors().stream()
                    .filter(a -> a.getAttribute("ng-click").equals("openUploadFileModal()")).findFirst();

            anchor.ifPresent(x -> {
                HtmlPage uploadPage;
                try {
                    uploadPage = x.click();
                    webClient.waitForBackgroundJavaScript(10000);
                    System.out.println(uploadPage.getWebResponse().getContentAsString());
                    System.out.println(uploadPage.getElementByName("file"));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            webClient.close();
                 } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
                 }


            //this is a version 4 UUID
            qq.getUniqueId = function(){
                "use strict";

                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                 //   jslint eqeq: true, bitwise: true
                    var r = (Math.random()*16)|0, v = c == 'x' ? r : ((r&0x3)|0x8);
                    return v.toString(16);
                });};
                  */
            //window.csrfToken
            //https://github.com/sharelatex/web-sharelatex/blob/2fbc796a728871fd536487eda6cd75cf1079f913/test/UnitTests/coffee/Uploads/ProjectUploadControllerTests.coffee

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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

/*  for (JsonElement elem : projectArray) {

      System.out.println("ID " + elem.getAsJsonObject().get("id").getAsString());
      System.out.println("Name " + elem.getAsJsonObject().get("name").getAsString());

  }




});

//script tag parsen
//Json parsen mit den Projects
*/
