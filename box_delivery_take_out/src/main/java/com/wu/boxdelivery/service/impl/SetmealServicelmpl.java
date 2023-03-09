package com.wu.boxdelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.boxdelivery.entity.Setmeal;
import com.wu.boxdelivery.mapper.SetmealMapper;
import com.wu.boxdelivery.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServicelmpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
