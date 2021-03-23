package com.example.workmanagerimplementation.SyncUtils.HelperUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataDownWorker;
import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataServices {
    private Context mContext;

    private DataDownWorker dataDownWorker;

    public DataServices(Context context){
        this.mContext=context;
    }

    public ArrayList<DataSync> dataUpServices(){
        ArrayList<DataSync> dataService = new ArrayList();
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStream e = assetManager.open("sync_data_up.xml");
            InputSource inStream = new InputSource(e);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("services");
            for (int temp = 0; temp < nList.getLength(); ++temp) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;

                    String table_name = eElement.getAttribute("table_name").toString();
                    Log.e("table_name",table_name);
                    String update_column = eElement.getAttribute("update_column").toString();
                    String url = eElement.getAttribute("url").toString();
                    String unique_column = eElement.getAttribute("unique_column").toString();
                    String http_method = eElement.getAttribute("http_method").toString();
                    String service = eElement.getAttribute("service").toString();

                    DataSync dataSync = new DataSync(url, http_method, table_name, unique_column, update_column);
                    dataSync.setServiceName(service);
                    dataService.add(dataSync);
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        return dataService;
    }

    //this method runs the sync_data_down.xml file and extract the api url, service name
    //http method etc. goto sync_data_down xml file for better understanding
    public ArrayList<DataSync> dataDownServices(){
        ArrayList<DataSync> dataServices=new ArrayList<>();
        AssetManager assetManager= getContext().getAssets();
        try{
            InputStream inputStream=assetManager.open("sync_data_down.xml");
            InputSource inputSource=new InputSource(inputStream);
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(inputSource);
            document.getDocumentElement().normalize();
            NodeList nodeList=document.getElementsByTagName("services");
            for (int temp = 0; temp < nodeList.getLength(); ++temp) {
                Node nNode = nodeList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;
                    String url = eElement.getAttribute("url").toString();
                    String httpMethod = eElement.getAttribute("http_method").toString();
                    String uniqueColumn = eElement.getAttribute("unique_column").toString();
                    Log.e("dataUni",uniqueColumn);
                    String whereCondition = eElement.getAttribute("where_condition").toString();
                    String service = eElement.getAttribute("service").toString();
                    DataSync dataSync = new DataSync(url, httpMethod, uniqueColumn, whereCondition);
                    dataSync.setServiceName(service);
                    dataServices.add(dataSync);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("dataservice",new Gson().toJson(dataServices));
        return dataServices;
    }




    public Context getContext() {
        return mContext;
    }
}
