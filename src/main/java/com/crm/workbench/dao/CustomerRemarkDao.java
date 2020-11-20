package com.crm.workbench.dao;

import com.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    List<CustomerRemark> getRemarkListByCid(String id);

    int deleteRemark(String id);

    int saveRemark(CustomerRemark cr);

    int updateRemark(CustomerRemark cr);
}
