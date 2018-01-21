package cn.et.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.et.dao.FoodsDao;
import cn.et.util.SolrUtils;

/** managed-schema文件定义的自段：其中foodName使用ik分词
<field name="foodId" type="int" indexed="false" stored="true"/>
<field name="foodName" type="text_ik" indexed="true" stored="true"/>
<field name="price" type="int" indexed="false" stored="true"/>
<field name="typeName" type="string" indexed="true" stored="true"/>*/

@Service
public class FoodsServiceImpl implements FoodsService {

    @Autowired
    private FoodsDao foodsDao;
 
    /**
     * 分页读取数据写入索引库
     * @throws IOException 
     * @throws SolrServerException 
     */
    @Override
    public void write() throws SolrServerException, IOException {
        int index = 0;
        int rows = 5;

        int counts = foodsDao.getCounts();

        while (index <= counts) {
            List<Map<String, String>> queryFoods = foodsDao.queryFoods(index, rows);
            for (Map<String, String> foods : queryFoods) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("foodId", foods.get("foodid"));
                document.addField("foodName", foods.get("foodname"));
                document.addField("price", foods.get("price"));
                document.addField("typeName", foods.get("typename"));
                SolrUtils.write(document);
            }
            index += rows;
        }

    }

}
