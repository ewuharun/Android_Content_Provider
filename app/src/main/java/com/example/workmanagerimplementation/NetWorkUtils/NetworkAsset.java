package com.example.workmanagerimplementation.NetWorkUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Md.harun or rashid on 12,April,2021
 * BABL, Bangladesh,
 */
public class NetworkAsset {

    public NetworkAsset() {
    }

    public String getStream(String url, int method, List<NameValuePair> params){
        String response="";
        try{
            DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
            HttpEntity entity=null;
            HttpResponse httpResponse=null;

            if(method==2){
                HttpPost httpPost=new HttpPost(url);
                if(params!=null){
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = defaultHttpClient.execute(httpPost);

            }

            entity=httpResponse.getEntity();
            response=EntityUtils.toString(entity);
        }catch (UnsupportedEncodingException unsupportedEncodingException){
            unsupportedEncodingException.printStackTrace();
        }
        catch (ClientProtocolException clientProtocolException){
            clientProtocolException.printStackTrace();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return response;
    }
}
