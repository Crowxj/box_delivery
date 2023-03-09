package com.wu.boxdelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.boxdelivery.common.CustomException;
import com.wu.boxdelivery.entity.Category;
import com.wu.boxdelivery.entity.Dish;
import com.wu.boxdelivery.entity.Setmeal;
import com.wu.boxdelivery.mapper.CategoryMapper;
import com.wu.boxdelivery.service.CategoryService;
import com.wu.boxdelivery.service.DishService;
import com.wu.boxdelivery.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired//注入
   private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);//等值查询
        int count1=dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联抛出一个业务异常
        if (count1>0){
            //已经关联抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能够够删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果已经关联抛出一个业务异常

        if (count2 > 0) {
            //已经关联抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能够够删除");
        }
        //正常删除分类
        super.removeById(id);

    }
}
