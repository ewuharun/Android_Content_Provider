package com.example.workmanagerimplementation.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workManager.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
    }

    public void deleteTable(SQLiteDatabase db) {
        AssetManager assetManager = this.context.getAssets();

        try {
            InputStream e = assetManager.open("create_table.xml");
            InputSource inStream = new InputSource(e);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("table");
            for (int temp = 0; temp < nList.getLength(); ++temp) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;
                    String table_name = eElement.getAttribute("tablename").toString();
                    String deleteTable = "DROP TABLE "+table_name;
                    db.execSQL(deleteTable);
                    Log.v("Delete Table : ", deleteTable);
                }
            }
        } catch (Exception var22) {
            var22.printStackTrace();
        }
    }

    public void createTable(SQLiteDatabase db) {

        AssetManager assetManager = this.context.getAssets();
        try {
            InputStream e = assetManager.open("create_table.xml");
            InputSource inStream = new InputSource(e);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("table");
            for (int temp = 0; temp < nList.getLength(); ++temp) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;
                    String table_name = eElement.getAttribute("tablename").toString();
                    String table_column_format = "";
                    int len = eElement.getElementsByTagName("col_name").getLength();

                    for (int i = 0; i < len; ++i) {
                        String coloum_name = eElement.getElementsByTagName("col_name").item(i).getTextContent().toString();
                        String column_name_attribute = "";
                        Node aNode = eElement.getElementsByTagName("col_name").item(i);
                        NamedNodeMap attributes = aNode.getAttributes();
                        for (int a = 0; a < attributes.getLength(); ++a) {
                            Node theAttribute = attributes.item(a);
                            column_name_attribute = column_name_attribute + theAttribute.getNodeValue() + " ";
                        }
                        table_column_format = table_column_format + coloum_name + " " + column_name_attribute + ", ";
                    }
                    Log.e("test_table",table_name);
                    String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + " (" + table_column_format.substring(0, table_column_format.lastIndexOf(44) - 1) + ")";
                    db.execSQL(CREATE_USER_TABLE);
                    Log.v("Create Table : ", CREATE_USER_TABLE);
                }
            }
        } catch (Exception var22) {
            var22.printStackTrace();
        }
    }
}
