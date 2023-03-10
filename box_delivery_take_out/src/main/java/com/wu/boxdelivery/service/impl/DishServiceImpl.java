package com.wu.boxdelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.boxdelivery.dto.DishDto;
import com.wu.boxdelivery.entity.Dish;
import com.wu.boxdelivery.entity.DishFlavor;
import com.wu.boxdelivery.mapper.DishMapper;
import com.wu.boxdelivery.service.DishFlavorService;
import com.wu.boxdelivery.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
   @Autowired
   private DishFlavorService dishFlavorService;
    /**
     *新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional//多张表添加事务控制,保证事务一致性
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        Long dishId=dishDto.getId();//菜品id
        //菜品口味
        List<DishFlavor>flavors=dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());//stream流

        //保存菜品口味数据道菜品口味的dish_flavor
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish=this.getById(id);

        DishDto dishDto=new DishDto();//构造dto对象
        BeanUtils.copyProperties(dish,dishDto);//dish源

        //查询当前菜品的对应口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }
}