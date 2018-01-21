package cn.et.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.et.service.FoodsService;
import cn.et.util.SolrUtils;

@RestController
public class FoodsController {
    @Autowired
    private FoodsService foodsService;

    @RequestMapping("/write")
    public String writeData() throws IOException, SolrServerException {

        foodsService.write();

        return "写入数据成功";
    }

    @RequestMapping("/searchFood")
    public List<Map<String, String>> search(String keyWord) throws Exception {
        String field = "foodName";

        List<Map<String, String>> list = SolrUtils.search(field, keyWord);
        return list;
    }
    
    @RequestMapping("/searchByCondition")
    public List<Map<String, String>> searchByCondition(String foodName,String typeName) throws Exception {
        List<Map<String, String>> list = SolrUtils.searchByCondition(new String[]{"foodName","typeName"}, new String[]{foodName,typeName});
        return list;
    }
    
    @RequestMapping("/facetFood")
    public List<Map<String, String>> facet(String keyWord) throws Exception {

        List<Map<String, String>> list = SolrUtils.facet("foodName", keyWord, "typeName");
        return list;
    }
}
