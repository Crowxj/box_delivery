package com.wu.boxdelivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.boxdelivery.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
