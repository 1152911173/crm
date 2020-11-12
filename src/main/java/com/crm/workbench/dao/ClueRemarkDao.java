package com.crm.workbench.dao;

import com.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> geListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
