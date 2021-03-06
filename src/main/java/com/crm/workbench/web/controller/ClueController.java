package com.crm.workbench.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.vo.PaginationVO;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.Clue;
import com.crm.workbench.domain.ClueRemark;
import com.crm.workbench.domain.Tran;
import com.crm.workbench.service.ActivityService;
import com.crm.workbench.service.ClueService;
import com.crm.workbench.service.impl.ActivityServiceImpl;
import com.crm.workbench.service.impl.ClueServiceImpl;
import com.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        } else if ("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        } else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        } else if ("/workbench/clue/getClueById.do".equals(path)) {
            getClueById(request,response);
        } else if ("/workbench/clue/pageList.do".equals(path)) {
            pageList(request,response);
        } else if ("/workbench/clue/update.do".equals(path)) {
            update(request,response);
        } else if ("/workbench/clue/delete.do".equals(path)) {
            delete(request,response);
        } else if ("/workbench/clue/getRemarkListByClueId.do".equals(path)) {
            getRemarkListByClueId(request,response);
        } else if ("/workbench/clue/saveRemark.do".equals(path)) {
            saveRemark(request,response);
        } else if ("/workbench/clue/updateRemark.do".equals(path)) {
            updateRemark(request,response);
        } else if ("/workbench/clue/deleteRemark.do".equals(path)) {
            deleteRemark(request,response);
        }
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索评论的删除操作");
        String id= request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索评论的更新操作");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);
        cr.setEditFlag(editFlag);

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.updateRemark(cr);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("cr",cr);
        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索评论的添加操作");
        String id = UUIDUtil.getUUID();
        String clueId = request.getParameter("clueId");
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setCreateBy(createBy);
        cr.setCreateTime(createTime);
        cr.setEditFlag(editFlag);
        cr.setClueId(clueId);

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.saveRemark(cr);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("cr",cr);
        PrintJson.printJsonObj(response,map);

    }

    private void getRemarkListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id获取评论列表");
        String clueId = request.getParameter("clueId");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> cList = cs.getRemarkListByClueId(clueId);
        PrintJson.printJsonObj(response,cList);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id删除线索");
        String[] ids = request.getParameterValues("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id更新线索信息");
        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        clue.setContactSummary(contactSummary);
        clue.setCompany(company);
        clue.setAppellation(appellation);
        clue.setAddress(address);

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.update(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("展示线索列表");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> vo = cs.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void getClueById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id获取线索");
        String id = request.getParameter("id");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.getClueById(id);
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("c",c);
        PrintJson.printJsonObj(response,map);

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行线索转换的操作");

        String clueId = request.getParameter("clueId");
        //接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        Tran t = null;
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        if("a".equals(flag)){
            //接收交易表单中的数据
            t = new Tran();
            String id = UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
        }
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        /*
        为业务层传递的参数
            1：clueId,
            2: T对象 为null或交易
            3: createBy:
         */
        boolean flag1 = cs.convert(clueId,t,createBy);
        if(flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场互动的操作");
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动列表（根据名称模糊查+排除掉已经关联指定线索的列表");
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行解除关联操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索的详细信息页");
        String id = request.getParameter("id");
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索的添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setContactSummary(contactSummary);
        clue.setCompany(company);
        clue.setAppellation(appellation);
        clue.setAddress(address);

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }
}