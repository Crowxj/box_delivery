package com.wu.boxdelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.boxdelivery.entity.DishFlavor;
import com.wu.boxdelivery.mapper.DishFlavorMapper;
import com.wu.boxdelivery.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl <DishFlavorMapper, DishFlavor>implements DishFlavorService {
}
