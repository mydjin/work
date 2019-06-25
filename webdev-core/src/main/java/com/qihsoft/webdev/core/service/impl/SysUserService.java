package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.model.*;
import com.qihsoft.webdev.core.service.ISysUserService;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.J;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.convert.W;
import com.qihsoft.webdev.utils.kit.IdKit;
import com.qihsoft.webdev.core.mapper.SysUserMapper;
/*import com.qihsoft.webdev.webdev.core.model.*;*/
import com.qihsoft.webdev.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserService extends TServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserRoleService userRoleService;


    @Transactional
    @Override
    public Object createUser(JSONObject object) {
        String userId = IdKit.getId(SysUser.class);
        object.put("id", userId);
        SysUser sysUser = J.o2m(object, SysUser.class);
        // 用户主表
        this.insert(sysUser);
        String roleId = object.getString("roleId");
        if (V.isEmpty(roleId)) {//如果没有选择角色权限，则选择默认
            //读取默认角色Id
            SysRole sysRole = roleService.getDefaultRole(object.getString("systemId"));
            roleId = sysRole.getId();
        }
        // 用户角色关系
        SysUserRole ur = new SysUserRole();
        ur.setUserId(userId);
        ur.setRoleId(roleId);
        ur.setSystemId("0");
        userRoleService.createUserRole(ur);
        return findByLoginName(sysUser.getLoginName());
    }

    @Transactional
    @Override
    public Object updateUser(JSONObject object) {
        SysUser sysUser = J.o2m(object, SysUser.class);
        this.update(sysUser);
        if (V.isEmpty(sysUser.getLoginName()))
            return this.selectByIds(sysUser.getId());
        return findByLoginName(sysUser.getLoginName());
    }

    // 修改
    @Override
    public SysUser updateSysUser(SysUser model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<SysUser> findByIds(Object ids) {
        return this.selectByIds(ids);
    }

    @Override
    public Object findByLoginName(String loginName) {
        where = W.f(
                W.and("loginName", "eq", loginName)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return null;
        SysUser sysUser = sysUsers.get(0);
        if (sysUser.getStatus() == 0)
            return sysUser;

        String userId = sysUser.getId();

        //权限信息
        Object auth = new JSONObject();

        //数据整合
        JSONObject u = new JSONObject();
        u.put("id", userId);
        u.put("loginName", sysUser.getLoginName());
        u.put("createTime", sysUser.getCreateTime());
        u.put("status", sysUser.getStatus());
        u.put("email", sysUser.getEmail());
        u.put("password", sysUser.getPassword());
        u.put("salt", sysUser.getSalt());
        u.put("number", sysUser.getNumber());

        List<JSONObject> info = sysUserInfoService.queryAll(" and id = "+userId);
        if (!V.isEmpty(info)){
            u.put("info", info.get(0));
        }

        // 加入角色权限
        SysUserRole userRole = userRoleService.findByUserId(userId);
        u.put("role", userRole);

        u.put("auth", auth);
        u.put("sysUser", sysUser);
        u.put("phone", sysUser.getPhone());
        u.put("errorCount", sysUser.getErrorCount());
        u.put("unlockTime", sysUser.getUnlockTime());
        u.put("verifyCode", sysUser.getVerifyCode());
        return u;
    }

    @Override
    public Boolean existLoginName(String loginName) {
        where = W.f(
                W.and("loginName", "eq", loginName)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return false;
        return true;
    }


    public Boolean existNumber(String number) {
        where = W.f(
                W.and("number", "eq", number)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return false;
        return true;
    }


    //htc
    public SysUser findByPhone(String phone) {
        where = W.f(
                W.and("phone", "eq", phone)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return null;
        SysUser sysUser = sysUsers.get(0);
        return sysUser;
    }

    //htc
    public SysUser findByUserName(String loginName) {
        where = W.f(
                W.and("loginName", "eq", loginName)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return null;
        SysUser sysUser = sysUsers.get(0);
        return sysUser;
    }


    //htc
    public SysUser findByNumber(String number) {
        where = W.f(
                W.and("number", "eq", number)
        );
        List<SysUser> sysUsers = this.query(where);
        if (sysUsers.size() == 0)
            return null;
        SysUser sysUser = sysUsers.get(0);
        return sysUser;
    }


    @Override
    public Page page(int index, int size, String whereStr) {
        if (whereStr.length() > 0)
            whereStr = " and loginName like '%" + whereStr + "%' ";
        int pageSize = size;
        // 总记录数
        JSONObject row = baseMapper.getPageCount(whereStr);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        List<JSONObject> users = F.f2l(baseMapper.getPage(whereStr, limit, pageSize),"id");
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;
        if (users.size() == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, totalCount, totalPage, currentPage);

        //整合info

        for (JSONObject user : users) {
            SysUserInfo infos = sysUserInfoService.selectById(user.getString("id"));
            if (!V.isEmpty(infos)){
                user.put("info", infos);

            }
        }

        return new Page(F.f2l(users, "id"), pageSize, totalCount, totalPage, currentPage);
    }

    @Override
    public Boolean existId(Object id) {
        where = W.f(
                W.and("id", "eq", id),
                W.field("1")
        );
        return this.query(where).size() > 0;
    }

    @Override
    public Boolean deleteUser(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }


    @Transactional
    @Override
    public Boolean initPwd(JSONObject object) {
        SysUser sysUser = J.o2m(object, SysUser.class);
        this.update(sysUser);
        return true;
    }

    @Override
    public List<JSONObject> getUserAuth(String userId) {


        List<SysMenu> menus = baseMapper.getRoleMenuByUserId(userId);
        // 找父级parentId=0
        if (menus.size() == 0)
            return new ArrayList<>();
        List<SysMenuAuth> auths = baseMapper.getRoleAuthByUserId(userId);

        List<JSONObject> list = new ArrayList<JSONObject>();
        List<SysMenu> modules = new ArrayList<>();

        List<SysMenu> parents = new ArrayList<>();
        List<JSONObject> subs = new ArrayList<>();
        // 取模块
        for (SysMenu m1 : menus) {
            if (V.isEqual(m1.getParentId(), "0")) {
                modules.add(m1);
            }
        }
        // 取父级
        for (SysMenu module : modules) {
            for (SysMenu menu : menus) {
                if (V.isEqual(menu.getParentId(), module.getId())) {
                    parents.add(menu);
                }
            }
        }
        // 取子级
        for (SysMenu parent : parents) {
            for (SysMenu menu : menus) {
                if (V.isEqual(menu.getParentId(), parent.getId())) {
                    JSONObject sub = new JSONObject();
                    sub.put("id", menu.getId());
                    sub.put("name", menu.getName());
                    sub.put("url", menu.getUrl());
                    sub.put("icon", menu.getIcon());
                    sub.put("code", menu.getCode());
                    sub.put("sort", menu.getSort());
                    sub.put("parentId", menu.getParentId());
                    sub.put("hide",menu.getHide());
                    List<JSONObject> btns = new ArrayList<>();
                    if (auths.size() > 0) {
                        for (SysMenuAuth auth : auths) {
                            if (V.isEqual(auth.getMenuId(), menu.getId())) {
                                JSONObject btn = new JSONObject();
                                btn.put("id", auth.getId());
                                btn.put("name", auth.getName());
                                btn.put("fun", auth.getFun());
                                btn.put("menuId", auth.getMenuId());
                                btn.put("code", auth.getCode());
                                btns.add(btn);
                            }
                        }
                    }
                    sub.put("btns", btns);
                    //权限按钮
                    subs.add(sub);
                }
            }
        }

        for (SysMenu module : modules) {
            //模块
            JSONObject mModule = new JSONObject();
            mModule.put("id", module.getId());
            mModule.put("url", "/");
            mModule.put("name", module.getName());
            mModule.put("icon", module.getIcon());
            mModule.put("sort", module.getSort());
            mModule.put("parentId", module.getParentId());
            List<JSONObject> ps = new ArrayList<JSONObject>();
            if (parents.size() > 0) {
                for (SysMenu p : parents) {
                    if (V.isEqual(p.getParentId(), module.getId())) {
                        //父级
                        JSONObject parent = new JSONObject();
                        parent.put("id", p.getId());
                        parent.put("url", "/");
                        parent.put("name", p.getName());
                        parent.put("icon", p.getIcon());
                        parent.put("sort", p.getSort());
                        parent.put("parentId", p.getParentId());

                        List<JSONObject> mSub = new ArrayList<JSONObject>();
                        if (subs.size() > 0) {
                            for (JSONObject sub : subs) {
                                if (V.isEqual(sub.getString("parentId"), p.getId())) {
                                    mSub.add(sub);
                                }
                            }
                        }
                        parent.put("sub", mSub);
                        ps.add(parent);
                    }
                }
            }
            mModule.put("module", ps);
            list.add(mModule);
        }
        return list;
    }







}