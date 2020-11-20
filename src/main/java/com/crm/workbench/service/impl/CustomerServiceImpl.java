package com.crm.workbench.service.impl;

import com.crm.settings.dao.UserDao;
import com.crm.settings.domain.User;
import com.crm.utils.SqlSessionUtil;
import com.crm.vo.PaginationVO;
import com.crm.workbench.dao.CustomerDao;
import com.crm.workbench.dao.CustomerRemarkDao;
import com.crm.workbench.domain.Customer;
import com.crm.workbench.domain.CustomerRemark;
import com.crm.workbench.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> sList = customerDao.getCustomerName(name);
        return sList;
    }

    @Override
    public boolean save(Customer cus) {
        boolean flag = true;
        int count = customerDao.save(cus);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Customer> pageList(Map<String, Object> map) {
        //取得total
        int total = customerDao.getTotalByCondition(map);
        //取得dataList
        List<Customer> cList = customerDao.getListByCondition(map);
        //创建一个vo对象，将total和dataList存放在vo中
        PaginationVO<Customer> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(cList);
        return vo;
    }

    @Override
    public Map<String, Object> edit(String id) {
        List<User> uList = userDao.getUserList();
        Customer c = customerDao.edit(id);
        Map<String, Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("c",c);
        return map;
    }

    @Override
    public boolean update(Customer cus) {
        boolean flag = true;
        int count = customerDao.update(cus);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        int count = customerDao.delete(ids);
        return flag;
    }

    @Override
    public Customer detail(String id) {
        Customer c = customerDao.detail(id);
        return c;
    }

    @Override
    public List<CustomerRemark> getRemarkListByCid(String id) {
        List<CustomerRemark> cList = customerRemarkDao.getRemarkListByCid(id);
        return cList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = customerRemarkDao.deleteRemark(id);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(CustomerRemark cr) {
        boolean flag = true;
        int count = customerRemarkDao.saveRemark(cr);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(CustomerRemark cr) {
        boolean flag = true;
        int count = customerRemarkDao.updateRemark(cr);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
