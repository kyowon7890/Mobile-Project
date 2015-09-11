package com.example.kyo.gmaptest;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 데이터를 파싱하는 Class입니다. 작업중
 * Created by kyo on 2015-09-09.
 */
//private inner class extending AsyncTask
public class GetXMLTask extends AsyncTask<String, Void, Document> {

    private Document doc = null;
    private String getListTag;
    private ArrayList<String> pasingTagData;
    private ArrayList<String> pasingResultData;

    /**
     * URL을 파라미터로 받아 해당 URL로 설정합니다. (생성자)
     * @param URL
     */
    public GetXMLTask(String URL, String getListTag, ArrayList<String> pasingTagData){
       execute(URL);
        this.getListTag = getListTag;
        this.pasingTagData = pasingTagData;
    }

    /////Getter
    /**
     * Document를 반환 합니다.
     * @return
     */
    public Document getDoc() {
        return doc;
    }

    ////Setter
    /**
     * Document를 설정합니다.
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     *
     * ListTag를 반환합니다.
     */
    public String getGetListTag() {
        return getListTag;
    }
    /**
     *
     * ListTag를 설정합니다.
     */
    public void setGetListTag(String getListTag) {
        this.getListTag = getListTag;
    }

    @Override
    protected Document doInBackground(String... urls) {
        URL url;
        try {
            url = new URL(urls[0]);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
            doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
            doc.getDocumentElement().normalize();
        } catch (Exception e) {

        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {
        String data = "";

        //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
        NodeList nodeList = doc.getElementsByTagName(getListTag);
        //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

        for(int i = 0; i< nodeList.getLength(); i++){
            for(int j = 0; i < pasingTagData.size(); j++) {
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                NodeList Nodedata = fstElmnt.getElementsByTagName(pasingTagData.get(j)); //
                Element nameElement = (Element) Nodedata.item(0);
                Nodedata = nameElement.getChildNodes();

                data = ((Node) Nodedata.item(0)).getNodeValue();
                pasingResultData.add(data);
            }
        }
        super.onPostExecute(doc);
    }

    public ArrayList<String> getPasingData(){
        return pasingResultData;
    }
}//end inner class - GetXMLTask
