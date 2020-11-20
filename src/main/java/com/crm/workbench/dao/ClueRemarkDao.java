package com.crm.workbench.dao;

import com.crm.workbench.domain.ClueRemark;
import com.crm.workbench.service.ClueService;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> geListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int getTotalByCid(String id);

    int deleteByCid(String id);

    int saveRemark(ClueRemark cr);

    int updateRemark(ClueRemark cr);

    int deleteRemark(String id);
}
