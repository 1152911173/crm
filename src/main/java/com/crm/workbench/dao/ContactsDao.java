package com.crm.workbench.dao;

import com.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactListByCid(String customerId);

    int deleteContactById(String id);
}
