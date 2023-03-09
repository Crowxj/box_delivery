package com.wu.boxdelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.boxdelivery.entity.Category;

public interface CategoryService extends IService<Category> {//K扩展自己方法
    public void remove(Long id);
}
