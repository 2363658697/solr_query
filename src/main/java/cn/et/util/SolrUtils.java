package cn.et.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrUtils {

    static Integer index = 1;
    static String urlString = "http://localhost:8080/solr/mycore";

    static SolrClient solr;
    static {
        solr = new HttpSolrClient(urlString);
    }

    public static List<Map<String, String>> facet(String field, String value,String facetField) throws SolrServerException, IOException {
       
        SolrQuery solrQuery = new SolrQuery(field + ":" + value);
        
        solrQuery.setFacet(true); // 开启分类
        
        solrQuery.setRows(0);
        solrQuery.addFacetField(facetField); // 按字段分类 相同的归于一类

        solrQuery.setFacetLimit(10);// 限制facet返回的数量

        solrQuery.setFacetMissing(false);// 不统计null的值

        solrQuery.setFacetMinCount(1);// 设置返回的数据中每个分组的数据最小值，比如设置为1，则统计数量最小为1，不然不显示

        QueryResponse query = solr.query(solrQuery); // 对查询的条件进行绑定

        List<FacetField> facets = query.getFacetFields();// 返回的facet列表

        List<Map<String, String>> list=new ArrayList<>();
        
        for (FacetField facet : facets) {

            List<Count> counts = facet.getValues();
            for (Count count : counts) {
                Map<String, String> map=new HashMap<>();
                map.put(count.getName().toString(), String.valueOf(count.getCount()));
                list.add(map);
            }
        }
        return list;
    }
    
    
    public static List<Map<String, String>> search(String field, String value) throws SolrServerException, IOException  {
        SolrQuery solrQuery = new SolrQuery(field + ":" + value);

        // 高亮
        // 是否高亮
        solrQuery.setHighlight(true);
        // 指定高亮的条件
        solrQuery.addHighlightField("foodName");
        solrQuery.setHighlightSimplePre("<font color=red>");
        solrQuery.setHighlightSimplePost("</font>");

        // 对查询的条件进行绑定
        QueryResponse query = solr.query(solrQuery);

        SolrDocumentList results = query.getResults();

        // 返回高亮数据
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        
        List<Map<String, String>> foodList = new ArrayList<Map<String, String>>();

        // 遍历数据
        for (SolrDocument solrDocument : results) {
            String id = solrDocument.getFieldValue("id").toString();

            // 根据id获取高亮的数据集合
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get("foodName");
            String hlString = list.get(0);

            Map<String, String> foodMap = new HashMap<>();

            foodMap.put("foodId", solrDocument.get("foodId").toString());
            foodMap.put("foodName", hlString);
            foodMap.put("price", solrDocument.get("price").toString());
            foodMap.put("typeName", solrDocument.get("typeName").toString());
            foodList.add(foodMap);
        }
        return foodList;
    }

    public static List<Map<String, String>> searchByCondition(String[] field, String[] value) throws SolrServerException, IOException  {
        SolrQuery solrQuery = new SolrQuery();
        
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<field.length;i++){
            if(i==field.length-1){
                sb.append(field[i]+":"+value[i]);
            }else{
                sb.append(field[i]+":"+value[i] +" && ");
            }
        }
        solrQuery.setQuery(sb.toString());
        
        // 高亮
        // 是否高亮
        solrQuery.setHighlight(true);
        // 指定高亮的条件
        solrQuery.addHighlightField("foodName");
        solrQuery.setHighlightSimplePre("<font color=red>");
        solrQuery.setHighlightSimplePost("</font>");

        // 对查询的条件进行绑定
        QueryResponse query = solr.query(solrQuery);

        SolrDocumentList results = query.getResults();

        // 返回高亮数据
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        
        List<Map<String, String>> foodList = new ArrayList<Map<String, String>>();

        // 遍历数据
        for (SolrDocument doc : results) {
            String id = doc.getFieldValue("id").toString();
            Map<String, List<String>> msl = highlighting.get(id);
            List<String> list = msl.get("foodName");
            String highStr = list.get(0);
            Map<String, String> map=new HashMap<>();
            
            map.put("foodid", doc.get("foodId").toString());
            map.put("foodName", highStr);
            map.put("price", doc.get("price").toString());
            map.put("typeName", doc.get("typeName").toString());
            foodList.add(map);
        }
        return foodList;
    }
    
    
    public static void write(SolrInputDocument document) throws SolrServerException, IOException {

        synchronized (index) {
            document.addField("id", index);
            index++;
        }

        solr.add(document);
        solr.commit();
    }
}
