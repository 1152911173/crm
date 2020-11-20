package com.crm.workbench.dao;

import com.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);


    List<Customer> getListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    Customer edit(String id);

    int update(Customer cus);

    int delete(String[] ids);

    Customer detail(String id);
}
