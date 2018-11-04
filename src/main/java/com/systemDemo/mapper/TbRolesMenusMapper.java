package com.systemDemo.mapper;

import com.systemDemo.pojo.TbRolesMenus;
import com.systemDemo.pojo.TbRolesMenusExample;
import com.systemDemo.pojo.TbRolesMenusKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbRolesMenusMapper {
    int countByExample(TbRolesMenusExample example);

    int deleteByExample(TbRolesMenusExample example);

    int deleteByPrimaryKey(TbRolesMenusKey key);

    int insert(TbRolesMenus record);

    int insertSelective(TbRolesMenusKey record);

    List<TbRolesMenusKey> selectByExample(TbRolesMenusExample example);

    int updateByExampleSelective(@Param("record") TbRolesMenusKey record, @Param("example") TbRolesMenusExample example);

    int updateByExample(@Param("record") TbRolesMenusKey record, @Param("example") TbRolesMenusExample example);
    
}