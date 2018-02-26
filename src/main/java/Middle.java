import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Middle {

    //new API
    //https://chalk.charter.com/pages/viewpage.action?pageId=115175031
    private static String postfix_add = "/ams/Reminders?req=add";
    private static String postfix_delete = "/ams/Reminders?req=delete";
    private static String postfix_modify = "/ams/Reminders?req=modify";
    private static String postfix_purge = "/ams/Reminders?req=purge";
    //old API
    @Deprecated
    private static String postfix_change = "/ams/Reminders?req=ChangeReminders";


    private static String charterapi_ = "http://spec.partnerapi.engprod-charter.net/api/pub/networksettingsmiddle/ns/settings";
    private static String charterapi_b = "http://specb.partnerapi.engprod-charter.net/api/pub/networksettingsmiddle/ns/settings";
    private static String charterapi_c = "http://specc.partnerapi.engprod-charter.net/api/pub/networksettingsmiddle/ns/settings";
    private static String charterapi_d = "http://specd.partnerapi.engprod-charter.net/api/pub/networksettingsmiddle/ns/settings";
    //"http://specd.partnerapi.engprod-charter.net/api/pub/networksettingsmiddle/ns/settings?requestor=AMS";


    private String ams_ip = "172.30.81.4";
    private int ams_port = 8080;

    private static String statuscode = "code of the reminder processing result, one of the following:" +
            "\n0 - requested action with the reminder was accomplished successfully" +
            "\n2 - reminder is set for time in the past" +
            "\n3 - reminder is set for unknown channel" +
            "\n4 - reminder is unknown, applies to reminder deletion attempts";

    private int count_iterations = 1;

    private String yyyymmdd_data = "2018-02-22";
    private String[] yyyymmdd = {"2018-02-22"};
    //RACK_DATA=( `date +%Y-%m-%d -d "tomorrow +1day"` `date +%Y-%m-%d -d "tomorrow +2day"` `date +%Y-%m-%d -d "tomorrow +3day"` `date +%Y-%m-%d -d "tomorrow +4day"` `date +%Y-%m-%d -d "tomorrow +5day"` `date +%Y-%m-%d -d "tomorrow +6day"` `date +%Y-%m-%d -d "tomorrow +7day"` `date +%Y-%m-%d -d "tomorrow +8day"` `date +%Y-%m-%d -d "tomorrow +9day"` `date +%Y-%m-%d -d "tomorrow +10day"` )

    //private int channel = 2;
    private String channel = "2";
    private String[] RACK_CHANNEL = {"2"};

    //private String startmessage="[DBG] date: NEW START: count_iterations="+count_iterations+", RACK_DATA=?, RACK_CHANNELS=?";


    void Purge(String macaddress) throws IOException {
        System.out.println("[DBG] start Purge:");

        String url = "http://" + ams_ip + ":" + ams_port + postfix_change;
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        String json_purge = "{\"deviceId\":" + macaddress + ",\"reminders\":[{\"operation\":\"Purge\"}]}";
        StringEntity entity = new StringEntity(json_purge);
        request.setEntity(entity);

        //request.setHeader("Accept", "application/json");
        //request.setHeader("Content-type", "application/json");
        //request.setHeader("Content-type", "text/plain");
        request.setHeader("charset", "UTF-8");

        System.out.println("[DBG] Request string: " + request.toString()
                //+ "[DBG] Request json string: "+json_purge
                + "[DBG] Request entity: " + request.getEntity());

        HttpResponse response = client.execute(request);
        //System.out.println("\n[DBG] Response string: " + response.toString());
        //+"\n[DBG] Response getStatusLine: "+response.getStatusLine());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder body = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null; ) {
            System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
        }
        //JSONTokener tokener = new JSONTokener(builder.toString());
        //JSONArray finalResult = new JSONArray(tokener);

        //client.close();


   /*     // Считываем json
        Object obj = new JSONParser().parse(json_purge); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        // Кастим obj в JSONObject
        JSONObject jo = (JSONObject) obj;
        // Достаём firstName and lastName
        String firstName = (String) jo.get("firstName");
        String lastName = (String) jo.get("lastName");
        System.out.println("fio: " + firstName + " " + lastName);
        // Достаем массив номеров
        JSONArray phoneNumbersArr = (JSONArray) jo.get("phoneNumbers");
        Iterator phonesItr = phoneNumbersArr.iterator();
        System.out.println("phoneNumbers:");
        // Выводим в цикле данные массива
        while (phonesItr.hasNext()) {
            JSONObject test = (JSONObject) phonesItr.next();
            System.out.println("- type: " + test.get("type") + ", phone: " + test.get("number"));
        }*/


    }

    //to check - alternative http connection there:
    public void Purge2_alternative(String macaddress) throws IOException {
        System.out.println("[DBG] start Purge2_alternative:");

        byte[] postData = postfix_change.getBytes();
        String urlstring = "http://" + ams_ip + ":" + ams_port + postfix_change;
        URL url = new URL(urlstring);
        URLConnection connection = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) connection;
        //the same in one:
        //HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setInstanceFollowRedirects(false);
        //connection.setRequestProperty("Content-type", "text/plain");
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Charset", "utf-8");
        http.setRequestProperty("Content-Length", Integer.toString(postData.length));
        //http.setRequestProperty("Accept-Charset", "UTF-8");
        //http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        http.setUseCaches(false);
        try (DataOutputStream out = new DataOutputStream(http.getOutputStream())) {
            out.write(postData);
            System.out.println("Response: " + http.getResponseCode() + " " + http.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        http.disconnect();


        byte[] out = "{\"username\":\"root\",\"password\":\"password\"}".getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();

        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        //Do something with http.getInputStream()
        System.out.println(http.getOutputStream());
        http.disconnect();


/*        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "val");

        con.setDoOutput(true);

        //send POST request
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response1 = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response1.append(inputLine);
        }
        in.close();
        System.out.println(response1.toString());
*/

/*
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        System.out.println(con.getResponseCode());
        in.close();
        con.disconnect();*/



 /*
        String rawData = "id=10";
        String type = "application/x-www-form-urlencoded";
        String encodedData = URLEncoder.encode( rawData, "UTF-8" );
        URL u = new URL("http://www.example.com/page.php");
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "Content-Type", type );
        conn.setRequestProperty( "Content-Length", String.valueOf(encodedData.length()));
        OutputStream os = conn.getOutputStream();
        os.write(encodedData.getBytes());
         */

/*      http.setHeader("Accept", "application/json");
        http.setHeader("Content-type", "application/json");

        //HttpURLConnection httpconnection = (HttpURLConnection)((new URL(http://172.30.81.4:8080/ams/Reminders?req=ChangeReminders).openConnection()));
        URLConnection httpcon = (HttpURLConnection) ((new URL("127.0.0.1:8080").openConnection()));
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");
        http.setRequestMethod("POST");
        http.connect();*/
    }

    void Check_registration(String macaddress) throws IOException {
        String charterapi = charterapi_b;
        System.out.println("[DBG] start Check_registration:"
                + "\n[DBG] used charterapi: " + charterapi);

        String postfix = "/amsIp/";
        String url = charterapi + postfix + macaddress;
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        //request.setHeader("Content-type", "text/plain");
        //request.setHeader("Content-type", "application/json");
        //request.setHeader("charset", "utf-8");
        request.setHeader("charset", "UTF-8");
        System.out.println("[DBG] Request string: " + request.toString());
        //+"\n[DBG] Request getRequestLine: "+request.getRequestLine());

        HttpResponse response = client.execute(request);
        //System.out.println("\n[DBG] Response string: " + response.toString());
        //+"\n[DBG] Response getStatusLine: "+response.getStatusLine());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder body = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
        }

        //JSONTokener tokener = new JSONTokener(builder.toString());
        //JSONArray finalResult = new JSONArray(tokener);

        /*if (response.getStatusLine().getStatusCode() != 200) {
            //assert(response.getStatusLine().getStatusCode(), equalTo(200));
            System.out.println("getStatusCode != 200");
        }*/

        //client.close();
    }

    void Change_registration(String macaddress, String ams_ip) throws IOException {
        String charterapi = charterapi_b;
        System.out.println("[DBG] start Change_registration:"
                + "\n[DBG ]used charterapi: " + charterapi
                + "\n[DBG] used ams: " + ams_ip + ":" + ams_port);

        String postfix = "?requestor=AMS";
        String url = charterapi + postfix;
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        String json_change = "{\"setting\":{\"groups\":[" +
                "{\"options\":[]," +
                "\"id\":\"STB" + macaddress + "\"," +
                "\"type\":\"device-stb\"," +
                "\"amsid\":\"" + ams_ip + "\"}" +
                "]}}";
        StringEntity entity = new StringEntity(json_change);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        System.out.println("[DBG] Request string: " + request
                + "\n[DBG] Request string: " + request.toString()
                + "\n[DBG] Request json string: " + json_change
                + "\n[DBG] Request entity: " + request.getEntity()
                + "\n[DBG] Request headers: " + Arrays.toString(request.getAllHeaders()));

        HttpResponse response = client.execute(request);
        //System.out.println("\n[DBG] Response string: " + response.toString());
        //+"\n[DBG] Response getStatusLine: "+response.getStatusLine());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder body = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null; ) {
            System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
        }
        //JSONTokener tokener = new JSONTokener(builder.toString());
        //JSONArray finalResult = new JSONArray(tokener);

        //client.close();
    }

    void All(String macaddress, int count_reminders, int reminderOffset, int reminderOffset_new) throws IOException, InterruptedException {
        System.out.println("[DBG] start All:");
        Add(macaddress, count_reminders, reminderOffset);
        Edit(macaddress, count_reminders, reminderOffset, reminderOffset_new);
        Delete(macaddress, count_reminders, reminderOffset);
        System.out.println(statuscode);
    }

    void Add(String macaddress, int count_reminders, int reminderOffset) throws IOException, InterruptedException {
        System.out.println("[DBG] [date] start Add 48rems with Offset=" + reminderOffset + ", iteration=?/" + count_iterations + ", macaddress=" + macaddress + ", data=?, channel=?");

        String json_add48 = "{\"deviceId\":\"" + macaddress + "\",\"reminders\":[" +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + " }" +
                "]}";

        String postfix_change = "/ams/Reminders?req=ChangeReminders";
        String url = "http://" + ams_ip + ":" + ams_port + postfix_change;
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        StringEntity entity = new StringEntity(json_add48);
        request.setEntity(entity);

        for (int i = 1; i <= count_iterations; i++) {
            for (String ayyyymmdd : yyyymmdd) {
                for (String aRACK_CHANNEL : RACK_CHANNEL) {
                    //request.setHeader("Accept", "application/json");
                    //request.setHeader("Content-type", "application/json");
                    //request.setHeader("Content-type", "text/plain");
                    request.setHeader("charset", "UTF-8");

                    System.out.println("[DBG] Request string: " + request.toString()
                            //+ "\n[DBG] Request json string: "+json_add48
                            + "\n[DBG] Request entity: " + request.getEntity()
                            + "\n[DBG] [date]: iteration=" + i + "/" + count_iterations + ", data=" + ayyyymmdd + ", channel=" + aRACK_CHANNEL);

                    HttpResponse response = client.execute(request);
                    //System.out.println("\n[DBG] Response string: " + response.toString());
                    //+ "\n[DBG] Response getStatusLine: " + response.getStatusLine());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder body = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
                    }
                    //client.close();
                    //Thread.sleep(1000);
                }
            }
        }
    }

    void Edit(String macaddress, int count_reminders, int reminderOffset, int reminderOffset_new) throws IOException {
        System.out.println("[DBG] start Edit:");

        String json_delete48_add48 = "{\"deviceId\":\"" + macaddress + "\",\"reminders\":[" +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}," +
                "{\"operation\":\"Add\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset_new + "}" +
                "]}";

        String url = "http://" + ams_ip + ":" + ams_port + postfix_change;
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        StringEntity entity = new StringEntity(json_delete48_add48);
        request.setEntity(entity);

        //System.out.println(startmessage);
        for (int i = 1; i <= count_iterations; i++) {
            for (String ayyyymmdd : yyyymmdd) {
                for (String aRACK_CHANNEL : RACK_CHANNEL) {
                    //request.setHeader("Accept", "application/json");
                    //request.setHeader("Content-type", "application/json");
                    //request.setHeader("Content-type", "text/plain");
                    request.setHeader("charset", "UTF-8");

                    System.out.println("[DBG] Request string: " + request.toString()
                            //+ "\n[DBG] Request json string: " + json_delete48_add48
                            + "\n[DBG] Request entity: " + request.getEntity()
                            + "\n[DBG] date: Edit(delete 48 + add 48) with Offset=" + reminderOffset + ", offset_new=" + reminderOffset_new + ", iteration=" + i + "/" + count_iterations + ", macaddress=" + macaddress + ", data=" + ayyyymmdd + ", channel=" + aRACK_CHANNEL);

                    HttpResponse response = client.execute(request);
                    //System.out.println("\n[DBG] Response string: " + response.toString());
                    //+ "\n[DBG] Response getStatusLine: " + response.getStatusLine());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder body = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
                    }
                }
            }
        }
    }

    void Delete(String macaddress, int count_reminders, int reminderOffset) throws IOException {
        System.out.println("[DBG] start Delete:");

        String json_delete48 = "{\"deviceId\":\"" + macaddress + "\",\"reminders\":[" +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 00:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 01:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 02:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 03:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 04:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 05:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 06:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 07:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 08:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 09:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 10:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 11:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 12:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 13:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 14:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 15:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 16:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 17:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 18:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 19:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 20:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 21:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 22:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:00\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + "}," +
                "{\"operation\":\"Delete\",\"reminderChannelNumber\":" + channel + ",\"reminderProgramStart\":\"" + yyyymmdd + " 23:30\",\"reminderProgramId\":0,\"reminderOffset\":" + reminderOffset + " }" +
                "]}";

        String url = "http://" + ams_ip + ":" + ams_port + postfix_change;
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        //StringEntity entity = new StringEntity(json_delete48);
        StringEntity entity = new StringEntity(new FileReader("src/main/resources/json_delete48.json").toString());
        request.setEntity(entity);

        //System.out.println(startmessage);
        for (int i = 1; i <= count_iterations; i++) {
            for (String ayyyymmdd : yyyymmdd) {
                for (String aRACK_CHANNEL : RACK_CHANNEL) {
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-type", "application/json");
                    //request.setHeader("Content-type", "text/plain");
                    request.setHeader("charset", "UTF-8");

                    System.out.println("[DBG] Request string: " + request.toString()
                            //+ "\n[DBG] Request json string: " + json_delete48
                            + "\n[DBG] Request entity: " + request.getEntity()
                            + "\n[DBG] date: Delete 48rems with Offset=" + reminderOffset + ", iteration=" + i + "/" + count_iterations + ", macaddress=" + macaddress + ", data=" + ayyyymmdd + ", channel=" + aRACK_CHANNEL);

                    HttpResponse response = client.execute(request);
                    //System.out.println("\n[DBG] Response string: " + response.toString());
                    //+ "\n[DBG] Response getStatusLine: " + response.getStatusLine());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder body = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
                    }
                }
            }
        }
    }

    public String Generate_json(String json, int count_reminders){
   //     String macaddress = "A0722CB1AF24";
   //     int reminderOffset = 0;
        String result = "";
        return result;
    }

    void Generate_json(int count_remindres){
        //int count_reminders = count_remindres;
        //String json, String macaddress, String[] channel, String[] data, int[] reminderOffset) {
        System.out.println("generate_json with count_reminders: " + count_remindres);

/*        String json_add5 = "{\"deviceId\":" + macaddress + ",\"reminders\":["
                + "{\"operation\":" + operation + ",\"reminderChannelNumber\":" + reminderChannelNumber + ",\"reminderProgramStart\":" + reminderProgramStart + ",\"reminderProgramId\":" + reminderProgramId + ",\"reminderOffset\":" + reminderOffset + "},"
                + "{\"operation\":" + operation + ",\"reminderChannelNumber\":" + reminderChannelNumber + ",\"reminderProgramStart\":" + reminderProgramStart + ",\"reminderProgramId\":" + reminderProgramId + ",\"reminderOffset\":" + reminderOffset + "},"
                + "{\"operation\":" + operation + ",\"reminderChannelNumber\":" + reminderChannelNumber + ",\"reminderProgramStart\":" + reminderProgramStart + ",\"reminderProgramId\":" + reminderProgramId + ",\"reminderOffset\":" + reminderOffset + "},"
                + "{\"operation\":" + operation + ",\"reminderChannelNumber\":" + reminderChannelNumber + ",\"reminderProgramStart\":" + reminderProgramStart + ",\"reminderProgramId\":" + reminderProgramId + ",\"reminderOffset\":" + reminderOffset + "},"
                + "{\"operation\":" + operation + ",\"reminderChannelNumber\":" + reminderChannelNumber + ",\"reminderProgramStart\":" + reminderProgramStart + ",\"reminderProgramId\":" + reminderProgramId + ",\"reminderOffset\":" + reminderOffset + "}]}";
*/


        String macaddress = "A0722CB1AF24";
        String operation = "Add";
        int reminderChannelNumber = 2;
        String[] hhmm = { "00:00", "00:30", "01:00", "01:30" };
        String reminderProgramStart = "\"2018-03-01 "+hhmm[0]+"\"";
        int reminderProgramId = 0;
        int reminderOffset = 0;

        String json = "{\"deviceId\":" + macaddress + ",\"reminders\":[{\"operation\":" + operation + ", \"reminderChannelNumber\":" + reminderChannelNumber + ", \"reminderProgramStart\":" + reminderProgramStart + ", \"reminderProgramId\":" + reminderProgramId + ", \"reminderOffset\":" + reminderOffset + "}]}";
        String json_add2 = "{\"deviceId\":" + macaddress + ",\"reminders\":[{\"operation\": \"Delete\", \"reminderChannelNumber\":" + reminderChannelNumber + ", \"reminderProgramStart\":" + reminderProgramStart + ", \"reminderProgramId\":" + reminderProgramId + ", \"reminderOffset\":" + reminderOffset + "},{\"operation\":" + operation + ", \"reminderChannelNumber\":" + reminderChannelNumber + ", \"reminderProgramStart\":" + reminderProgramStart + ", \"reminderProgramId\":" + reminderProgramId + ", \"reminderOffset\":" + reminderOffset + "}]}";

        ArrayList hhmm_list = new ArrayList();
        hhmm_list.addAll(Arrays.asList(hhmm));
        System.out.println("[DBG] hhmm_list: " + hhmm_list);


        //WORKING parsing from json_string to Class:
        Gson g = new Gson();
        Reminder reminder = g.fromJson(json_add2, Reminder.class);
        System.out.println("[DBG] parsing from json_string to Class: \nmacaddress: " + reminder.deviceId);
        System.out.println("[DBG] count of reminders in class: " + reminder.reminders.size());
            for(Reminders rems : reminder.reminders){System.out.println(
                    "operation: " + rems.operation + ", " +
                    "reminderChannelNumber: " + rems.reminderChannelNumber + ", " +
                    "reminderProgramStart: " + rems.reminderProgramStart + ", " +
                    "reminderProgramId: " + rems.reminderProgramId + ", " +
                    "reminderOffset: " + rems.reminderOffset);
            }

        //parsing from Class to json_string
        System.out.println("[DBG] parsing from Class to json_string: \n" + g.toJson(reminder));




        //System.out.println(reminder.reminders.add(2, g));
        //reminder.reminders.add(1,"hjg");

        //for (int i=1; i<=count_remindres; i++){
//            reminder.reminders.size()

  //      }



/*        //WORKING variant for one class Reminder + one class Reminders
        //==============================================================
        final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        //from class -> to string json
        //class with fields:
        Reminders rs = new Reminders(operation, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
        Reminder r = new Reminder(macaddress, rs);
        //create json structure:
        String json = GSON.toJson(r);
        System.out.println("[DBG] from class -> to string json:\n" + json);

        //from json string -> to class
        Reminder to_class = GSON.fromJson(json, Reminder.class);
        System.out.println("[DBG] from json string -> to class:\n" + to_class.getDeviceId()+ " " + to_class.getClass());
*/


/*
        //from class -> to string json
        Reminder from_class = new Reminder(macaddress, Arrays.asList("operation", "reminderChannelNumber", "reminderProgramStart", "reminderProgramId", "reminderOffset"));
        String json = GSON.toJson(from_class);
        System.out.println("[DBG] from class -> to string json:\n" + json);

        //from json string -> to class
        Reminder to_class = GSON.fromJson(json, Reminder.class);
        System.out.println("[DBG] from json string -> to class:\n" + to_class.getDeviceId()+ " " + to_class.getReminders_list());
*/


        //WORKING
        //System.out.println();
        //JsonObject jo = new JsonParser().parse(json_add2).getAsJsonObject();
        //System.out.println("1 show full jsonobject: " + jo);
        //show only jsonarray:
        //String ja = jo.get("reminders").getAsJsonArray().toString();
        //System.out.println("2 only jsonarray: " + ja);

        System.out.println("\n\n");
        JSONArray ar = new JSONArray();
        JSONObject obj = new JSONObject();
        JSONObject resultJson = new JSONObject();
        String reminderProgramStart2 = "2018-03-01 "+hhmm[0];

        resultJson.put("deviceId", macaddress);
        resultJson.put("reminders", ar);

        for (int i=0;i<count_remindres;i++){
            ar.add(obj);
        }
        obj.put("operation", operation);
        obj.put("reminderChannelNumber", reminderChannelNumber);
        obj.put("reminderProgramStart", reminderProgramStart2);
        obj.put("reminderProgramId", reminderProgramId);
        obj.put("reminderOffset", reminderOffset);

        System.out.println("[DBG] result json: " + resultJson);

/*
        ja.add("reminderChannelNumber", reminderChannelNumber);
        jo.put("reminderProgramStart", reminderProgramStart);
        jo.put("reminderProgramId", reminderProgramId);
        jo.put("reminderOffset", reminderOffset);
        System.out.println("resultJson reminders: " + jo);


        obj.put("one", 2);
        obj.put("three", 4);


        resultJson.put("paramsObj", obj);
        resultJson.put("paramsStr", "some string");
        System.out.println(obj.toString());
        System.out.println("resultJson: " + resultJson);
*/
    }
}