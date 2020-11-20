package com.crm.workbench.service.impl;

import com.crm.utils.DateTimeUtil;
import com.crm.utils.SqlSessionUtil;
import com.crm.utils.UUIDUtil;
import com.crm.vo.PaginationVO;
import com.crm.workbench.dao.*;
import com.crm.workbench.domain.*;
import com.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);

        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for(String aid: aids){
            //取得每一个aid 和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            //执行添加操作
            int count = clueActivityRelationDao.bund(car);
            if(count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        // 1)根据id，查找线索对象
        Clue c = clueDao.getById(clueId);
        // 2)通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配,判断客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        //cus为null，新建这个客户
        if(cus==null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(c.getCreateTime());
            cus.setCreateBy(c.getCreateBy());
            cus.setContactSummary(c.getContactSummary());
            //添加客户
            int count = customerDao.save(cus);
            if(count != 1){
                flag = false;
            }
        }

        // 3)通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(c.getCreateTime());
        con.setCreateBy(c.getCreateBy());
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());
        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1) {
            flag = false;
        }

        //4)将线索的备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.geListByClueId(clueId);
        for(ClueRemark clueRemark: clueRemarkList){
            //取出备注信息（主要转换到客户备注和联系人备注）
            String noteContent = clueRemark.getNoteContent();
            //创建客户备注对象，添加信息
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3!=1){
                flag = false;
            }
            //创建联系人备注对象，添加信息
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4!=1){
                flag = false;
            }
        }
        //5)”线索和市场活动“的关系转换到”联系人和市场活动“的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getListByClueId(clueId);
        //遍历出每一条与市场活动关联的关联关系记录
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations){
            String activityId = clueActivityRelation.getActivityId();
            //创建 联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation =new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            //添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag=false;
            }
        }

        //6)如果有创建交易需求，创建交易
        if(t!=null){
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());
            //添加交易
            int count6 = tranDao.save(t);
            if(count6!=1){
                flag = false;
            }

            //7)创建交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if(count7!=1){
                flag = false;
            }
        }
        //8)删除线索和备注
        for(ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag = false;
            }
        }
        //9)删除线索和市场活动之间的关系
        for(ClueActivityRelation clueActivityRelation: clueActivityRelations){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1){
                flag = false;
            }
        }
        //10)删除线索
        int count10 = clueDao.delete(clueId);
        if(count10!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue getClueById(String id) {
        Clue c = clueDao.getById(id);
        return c;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        //取得total
        int total = clueDao.getTotalByCondition(map);
        //取得dataList
        List<Clue> dataList = clueDao.getClueListByCondition(map);
        //封装进vo中
        PaginationVO<Clue> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean update(Clue clue) {
        boolean flag = true;
        int count = clueDao.update(clue);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        for(String id : ids){
            int count1 = clueDao.delete(id);
            if(count1!=1){
                flag = false;
            }
            int count2 = clueRemarkDao.getTotalByCid(id);
            int count3 = clueRemarkDao.deleteByCid(id);
            if(count2!=count3){
                flag = false;
            }
            int count4 = clueActivityRelationDao.getTotalByCid(id);
            int count5 = clueActivityRelationDao.deleteByCid(id);
            if(count4!=count5){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getRemarkListByClueId(String clueId) {
        List<ClueRemark> cList = clueRemarkDao.geListByClueId(clueId);
        return cList;
    }

    @Override
    public boolean saveRemark(ClueRemark cr) {
        boolean flag = true;
        int count = clueRemarkDao.saveRemark(cr);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ClueRemark cr) {
        boolean flag = true;
        int count = clueRemarkDao.updateRemark(cr);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = clueRemarkDao.deleteRemark(id);
        if(count!=1){
            flag = false;
        }
        return flag;
    }
}
