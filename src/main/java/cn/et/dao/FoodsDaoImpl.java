package cn.et.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.et.mapper.FoodsMapper;

@Repository
public class FoodsDaoImpl implements FoodsDao {
    @Autowired
    private FoodsMapper foodsMapper;
    
    /* (non-Javadoc)
     * @see cn.et.dao.FoodsDao#queryFoods()
     */
    @Override
    public List<Map<String, String>> queryFoods(Integer index,Integer rows) {
        
        return foodsMapper.queryFoods(index,rows);
    }

    @Override
    public Integer getCounts() {
        
        return foodsMapper.getCounts();
    }
}



