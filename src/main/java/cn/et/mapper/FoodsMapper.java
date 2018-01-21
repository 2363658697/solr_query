package cn.et.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface FoodsMapper {

    @Select("select f.*,t.typename from foods f inner join foodtype t on f.typeid=t.typeid limit  #{0},#{1}")
    public List<Map<String, String>> queryFoods(Integer index, Integer rows);
    
    @Select("select count(*) from foods")
    public Integer getCounts();
}
