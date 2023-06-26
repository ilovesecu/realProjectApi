package com.playground.real_project_api.proc;

import com.playground.real_project_api.auth.member.RpmMember;
import com.playground.real_project_api.utils.annotation.RealProjectDb;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@RealProjectDb
public interface RealProjectMemberMapper {
    RpmMember getFullUserWithUserId(@Param(value = "userId")String userId);
}
