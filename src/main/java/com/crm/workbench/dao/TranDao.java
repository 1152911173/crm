package com.crm.workbench.dao;

import com.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();

    List<Tran> getTransactionListByCid(String id);

    int deleteById(String id);
}
