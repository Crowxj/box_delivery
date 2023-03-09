package com.wu.boxdelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.boxdelivery.common.R;
import com.wu.boxdelivery.entity.Employee;
import com.wu.boxdelivery.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
/**
 * 员工登录
 * @param request
 * @param employee
 * @return
 * */
    @PostMapping("/login")
    public R<Employee>login(HttpServletRequest request ,@RequestBody Employee employee){
        //1.将页面提交的密码password进行md5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);//
        //3.如果没有查询到则返回登录失败
        if(emp==null){
            return R.error("登录失败");
        }
        //4.密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //5.查看员工状态，如果已经基诺那个，则返回禁用值
        if(emp.getStatus()==0){
            return R.error("账号已经禁用");
        }

        //6.登录成功，员工id存入到Session并返回登陆成功的结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * @description:
     * @author: wxj
     * 员工退出
     * @date: 2023/3/4 上午9:53
     * @param:
     * @return:
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }



    /**
     * @description:
     * @author: wxj
     * @date: 2023/3/4 下午3:34
     *新增员工
     **/
    @PostMapping
    public R<String>save(HttpServletRequest request,@RequestBody  Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //id使用雪花算法
        //设置初始密码为123456，需要进行md5加密处理

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       /* employee.setCreateTime(LocalDateTime.now());//获取当前时间
        employee.setUpdateTime(LocalDateTime.now());//设置当前更新时间
        //获取当前登录用户的id
        Long empId=(Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);//更新人*/
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     * @param page
     * @param name
     * @param pageSize
     * @return
     * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo =new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper =new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return  R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     * */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){//传过来的是jeso数据使用RequestBody
        log.info(employee.toString());
        long id=Thread.currentThread().getId();
        log.info("线程id为:{}",id);
/*        Long empId=(Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);*/
        employeeService.updateById(employee);
        return  R.success("员工信息修改成功");
    }
    /**
     * 根据id查询员工信息
     * @param id
     * @return
     * */
    @GetMapping("/{id}")//url地址栏使用{}
    public R<Employee> getById(@PathVariable  Long id){//路径变量，在请求路径中
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }else {
            return R.error("没有查到对应的员工信息");
        }
    }
}
