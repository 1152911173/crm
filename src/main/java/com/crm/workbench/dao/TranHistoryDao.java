package com.crm.workbench.dao;

import com.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);

    int totalByTranId(String id);

    int deleteByTranId(String id);
}
