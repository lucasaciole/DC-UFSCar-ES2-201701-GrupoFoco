package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonArray;
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

    public SharelatexConnector() {
        // TODO Auto-generated constructor stub

    }

    public void connectToServer(String server, String queryString) {

        Connection.Response res1;
        try {
            res1 = Jsoup.connect("http://192.168.1.248/login").method(Method.GET)
                    .execute();

            String user = "joe@example.com";
            String pwed = "test";

            Document welcomePage = res1.parse();
            Map<String, String> welcomCookies = res1.cookies();

            String securityTokenValue = welcomePage.select("input[name=_csrf]").attr("value");



            String json = "{\"_csrf\":" + JSONObject.quote(securityTokenValue)
                    + ",\"email\":" + JSONObject.quote(user) + ",\"password\":" + JSONObject.quote(pwed) + "}";

            Connection.Response res2 = Jsoup.connect("http://192.168.1.248/login")
                    .header("Content-Type", "application/json;charset=utf-8")
                    .header("Accept", "application/json, text/plain, */*")
                    .cookies(welcomCookies)
                    .method(Method.POST)
                    .requestBody(json)
                    .followRedirects(true)
                    .ignoreContentType(true)
                    .execute();

            System.out.println(res2.body());
            Map<String,String> loginCookies = res2.cookies();

            Connection.Response resProjects = Jsoup.connect("http://192.168.1.248/project")
                    .referrer("http://192.168.1.248/login").cookies(loginCookies).method(Method.GET).execute();

            //  System.out.println(resProjects.body());

            System.out.println("");
            Optional<Element> scriptContent = Optional
                    .of(resProjects.parse().body().getElementsByTag("script").first());

            JsonParser parser = new JsonParser();

            scriptContent.ifPresent(element->{
                String data = element.data();
                JsonElement jsonTree = parser.parse(data);

                JsonObject obj = jsonTree.getAsJsonObject();
                JsonArray projectArray = obj.get("projects").getAsJsonArray();

                for (JsonElement elem : projectArray) {

                    System.out.println("ID " + elem.getAsJsonObject().get("id").getAsString());
                    System.out.println("Name " + elem.getAsJsonObject().get("name").getAsString());
                }


            });

            //script tag parsen
            //Json parsen mit den Projects

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
