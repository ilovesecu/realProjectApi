package com.playground.real_project_api.proc;

import com.playground.real_project_api.utils.annotation.RealProjectDb;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RealProjectDb
public interface RealProjectMapper {
    @Select("SELECT * FROM pgfu_member.user")
    Map<String,Object> test();
}
