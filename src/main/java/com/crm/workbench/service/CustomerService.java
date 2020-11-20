package com.crm.workbench.service;

import com.crm.vo.PaginationVO;
import com.crm.workbench.domain.Customer;
import com.crm.workbench.domain.CustomerRemark;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<String> getCustomerName(String name);

    boolean save(Customer cus);

    PaginationVO<Customer> pageList(Map<String, Object> map);


    Map<String, Object> edit(String id);

    boolean update(Customer cus);

    boolean delete(String[] ids);

    Customer detail(String id);

    List<CustomerRemark> getRemarkListByCid(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(CustomerRemark cr);

    boolean updateRemark(CustomerRemark cr);
}
