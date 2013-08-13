package com.baidu.soushang.utils;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.baidu.soushang.Config;
import com.baidu.soushang.cloudapis.FeatureEvent;


public class JsonTool {

  private static ArrayList<FeatureEvent> list;

  public static ArrayList<FeatureEvent> getFeatureData(String addr, Context context) {
    try {

      list = new ArrayList<FeatureEvent>();

      String url = String.format(addr, Config.getAccessToken(context));
      HttpPost request = new HttpPost(url);


      HttpClient httpClient = new DefaultHttpClient();
      HttpResponse httpResponse = httpClient.execute(request);

      if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

        String result = EntityUtils.toString(httpResponse.getEntity());
        list = JsonTool.parseJsonMulti(result);

      } else {
        System.out.println("at getFeatureData of JsonTool Á´½Ó»ñÈ¡json×Ö·û´®Ê§°Ü!");
      }

    } catch (Exception e) {
      System.out.println(e + "at getRelultDone!!");
    }
    return list;
  }

  public static ArrayList<FeatureEvent> parseJsonMulti(String strResult) {
    ArrayList<FeatureEvent> list = new ArrayList<FeatureEvent>();
    FeatureEvent sBean;
    try {

      JSONObject jObject = new JSONObject(strResult);
      JSONArray array = jObject.getJSONArray("rooms");
      for (int i = 0; i < array.length(); i++) {
        JSONObject jsonObj = array.getJSONObject(i);
        sBean = new FeatureEvent();
        sBean.setmStartTime(jsonObj.getInt("starttime"));
        sBean.setmEndTime(jsonObj.getInt("endtime"));
        sBean.setRunning(jsonObj.getBoolean("running"));
        sBean.setFinished(jsonObj.getBoolean("finished"));
        sBean.setId(jsonObj.getInt("id"));
        sBean.setPnum(jsonObj.getInt("pnum"));
        sBean.setTitle(jsonObj.getString("title"));
        sBean.setmIntroduce(jsonObj.getString("introduce"));
        list.add(sBean);
      }
    } catch (JSONException e) {
      System.out.println(e + "Jsons parse error !");
      e.printStackTrace();
    }
    return list;
  }

}
