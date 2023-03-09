package com.wu.boxdelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.boxdelivery.entity.Category;
import com.wu.boxdelivery.mapper.CategoryMapper;
import com.wu.boxdelivery.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
