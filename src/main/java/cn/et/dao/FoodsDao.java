package cn.et.dao;

import java.util.List;
import java.util.Map;


public interface FoodsDao {

    public List<Map<String, String>> queryFoods(Integer  index,Integer rows);
    
    public Integer getCounts();
}