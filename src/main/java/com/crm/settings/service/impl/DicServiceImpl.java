package com.crm.settings.service.impl;

import com.crm.settings.dao.DicTypeDao;
import com.crm.settings.dao.DicValueDao;
import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;
import com.crm.settings.service.DicService;
import com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        //将字典类型列表取出，
        List<DicType> dtList = dicTypeDao.getTypeList();

        Map<String,List<DicValue>> map = new HashMap<>();
        //将字典类型遍历
        for(DicType dt : dtList){
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code,dvList);
        }

        return map;
    }
}
