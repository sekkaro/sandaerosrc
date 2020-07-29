package com.sangdaero.walab.common.push;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Push {

    public void sendPush(String jsonValue) throws IllegalStateException {
        try {
            URL url = new URL("https://exp.host/--/api/v2/push/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            OutputStreamWriter osw = new OutputStreamWriter(
                    conn.getOutputStream()
            );

            try {
                osw.write(jsonValue);
                osw.flush();

                BufferedReader br = null;

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line = null;

                System.out.println("================");
                while ((line = br.readLine()) != null) {

                    System.out.println(line);

                }
                osw.close();
                br.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
