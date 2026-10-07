package com.aquariux.technical.assessment.trade.mapper;

import com.aquariux.technical.assessment.trade.dto.internal.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("""
            SELECT u.id, u.id
            FROM user u 
            WHERE u.id = #{userId} 
            """)
    UserDto findById(Long userId);
}