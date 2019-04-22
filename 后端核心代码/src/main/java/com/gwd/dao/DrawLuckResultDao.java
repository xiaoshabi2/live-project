package com.gwd.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gwd.entity.DrawLuckResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrawLuckResultDao extends BaseMapper<DrawLuckResult> {
    List<DrawLuckResult> getAll();
    DrawLuckResult getById(@Param("id")Integer id);
}

