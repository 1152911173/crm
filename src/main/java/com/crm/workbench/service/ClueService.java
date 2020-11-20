package com.crm.workbench.service;

import com.crm.vo.PaginationVO;
import com.crm.workbench.domain.Clue;
import com.crm.workbench.domain.ClueRemark;
import com.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

    Clue getClueById(String id);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean update(Clue clue);

    boolean delete(String[] ids);

    List<ClueRemark> getRemarkListByClueId(String clueId);

    boolean saveRemark(ClueRemark cr);

    boolean updateRemark(ClueRemark cr);

    boolean deleteRemark(String id);
}
