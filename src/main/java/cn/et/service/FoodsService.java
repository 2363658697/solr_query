package cn.et.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

public interface FoodsService {

    public void write() throws SolrServerException, IOException;

}