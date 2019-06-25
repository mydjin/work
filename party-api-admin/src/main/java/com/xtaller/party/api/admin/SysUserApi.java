package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.*;
import com.xtaller.party.utils.tool.RSAEncrypt;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.*;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.utils.kit.IdKit;
import com.xtaller.party.utils.kit.MD5Kit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Taller on 2017/9/9.
 */
@Api(value = "SYS5_用户管理")
@RestController
@RequestMapping("/v1/sys")
public class SysUserApi extends BaseApi {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private UserBaseInfoService userBaseInfoService;
    @Autowired
    private UserDetailInfoService userDetailInfoService;

    @GetMapping("/user/{index}-{size}-{loginName}-{roleId}-{academyCode}")
    @ApiOperation(value = "读取用户列表")
    public Object getUserList(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("loginName") String loginName,
                              @PathVariable("roleId") String roleId,
                              @PathVariable("academyCode") String academySelectCode,
                              @RequestHeader("token") String token) {
        String userkey = "";
        String rolekey = "";
        String infokey = "";

        if (!V.isEmpty(loginName))
            userkey += S.apppend(" and loginName like '%", loginName.replace("*",""), "%' ");
        if (!V.isEmpty(roleId))
            rolekey  += S.apppend(" and roleId like '%", roleId.replace("*",""), "%' ");

        if (!V.isEmpty(academySelectCode))
            infokey += S.apppend(" and academyCode like '%", academySelectCode.replace("*",""), "%' ");



        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        if (visible == 1) {//1-全部（组织部）

            return R.ok(userService.page(index, size, userkey,rolekey,infokey));


        } else if (visible == 2) {//2-院级

            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }

            String numbers = customService.queryUserNumbersByAcademyCode(academyCode);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }
            //获取该学院代码下所有的用户信息
            userkey += S.apppend(" and number in (", numbers, ") ");


            return R.ok(userService.page(index, size, userkey,rolekey,infokey));

        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }

            String numbers = customService.queryUserNumbersByLinkNumber(loginNumber);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }

            userkey += S.apppend(" and number in (", numbers, ") ");

            return R.ok(userService.page(index, size, userkey,rolekey,infokey));
        } else {
            return R.error("您没有权限查看");
        }


    }

    @GetMapping("/userInfo/{index}-{size}-{number}-{academyCode}")
    @ApiOperation(value = "读取用户基础信息分页列表")
    public Object getUserInfo(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("number") String number,
                              @PathVariable("academyCode") String academySelectCode,
                              @RequestHeader("token") String token) {

        String wKey = "";

        if (!V.isEmpty(number) )
            wKey += S.apppend(" and number = '", number, "' ");

        if (!V.isEmpty(academySelectCode) )
            wKey += S.apppend(" and academyCode = '",academySelectCode, "' ");
        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }
        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        if (visible == 1) {//1-全部（组织部）

            return R.ok(userBaseInfoService.page(index, size, wKey));


        } else if (visible == 2) {//2-院级

            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }

            String numbers = customService.queryUserNumbersByAcademyCode(academyCode);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }
            //获取该学院代码下所有的用户信息
            wKey += S.apppend(" and number in (", numbers, ") ");


            return R.ok(userBaseInfoService.page(index, size, wKey));

        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }

            String numbers = customService.queryUserNumbersByLinkNumber(loginNumber);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }

            wKey += S.apppend(" and number in (", numbers, ") ");

            return R.ok(userBaseInfoService.page(index, size, wKey));
        } else {
            return R.error("您没有权限查看");
        }


    }


    @PostMapping("/user")
    @ApiOperation(value = "登记用户")
    public Object createUser(@RequestBody SysUserReg object, @RequestHeader("token") String token) {

        JSONObject user = cacheKit.user(token);

        if (V.isEmpty(object.getLoginName()))
            return R.error("请输入登录名");
        if (V.isEmpty(object.getPassword()))
            return R.error("请输入登录密码");
        if (V.isLength(object.getPassword(), 6))
            return R.error("必须配置6位数以上密码");

        if (userService.existLoginName(object.getLoginName()))
            return R.error(F.s("当前登录名[%s]已经被注册", object.getLoginName()));

        if (object.getNumber() == null || object.getNumber().length() == 0) {
            return R.error("请输入学号/工号");
        }

        if (userService.existNumber(object.getNumber())) {
            return R.error("该学号/工号已存在");
        }


        if (userService.existPhone(object.getAttr().getMobile())) {
            return R.error("该手机号已存在");
        }

        boolean flag = false;
        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (visible == 1) {//1-全部（组织部）
            //继续操作
            flag = true;
        } else if (visible == 2) {//2-院级
            String academyCode = getAcademyCodeByCache(token);
            if (object.getAttr().getAcademyCode().equals(academyCode)) {
                return true;
            }
        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String academyCode = getAcademyCodeByCache(token);
            String departCode = getDepartCodeByCache(token);
            if (object.getAttr().getAcademyCode().equals(academyCode) && object.getAttr().getDepartCode().equals(departCode)) {
                return true;
            }
        }

        if (!flag) {
            return R.error("您没有当前学院或党支部的管理权限，无法添加成员。");
        }


        String salt = S.randomNum();
        String pwd = MD5Kit.encode(object.getPassword() + salt);

        JSONObject model = new JSONObject();
        model.put("loginName", object.getLoginName());
        model.put("password", pwd);
        model.put("salt", salt);
        model.put("number", object.getNumber());
        model.put("phone", object.getAttr().getMobile());
        model.put("type", object.getType());
        model.put("creator", user.getString("id"));
        model.put("roleId", object.getRoleId());

        model = (JSONObject) userService.createUser(model);
        if (model == null) {
            return R.error("用户创建失败");
        } else {
            String id = model.getString("id");
            //新增到个人信息表
            UserBaseInfo userBaseInfo = new UserBaseInfo();
            userBaseInfo.setId(id);
            userBaseInfo.setName(object.getAttr().getTrueName());
            userBaseInfo.setNumber(object.getNumber());
            userBaseInfo.setType(object.getType());
            userBaseInfo.setPhone(object.getAttr().getMobile());
            userBaseInfoService.createUserBaseInfo(userBaseInfo);

            //新增到个人详细信息表
            UserDetailInfo userDetailInfo = new UserDetailInfo();
            userDetailInfo.setId(id);
            userDetailInfo.setNumber(object.getNumber());
            userDetailInfo.setAcademyCode(object.getAttr().getAcademyCode());
            userDetailInfo.setDepartCode(object.getAttr().getDepartCode());

            userDetailInfoService.createUserDetailInfo(userDetailInfo);
            return R.ok(model);

        }

    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "密码修改")
    public Object updatePassword(@RequestBody UpdatePassword object, @RequestHeader("token") String token) {

        String uid = cacheKit.getUserId(token);
        int salt = S.randomNums();
        List<SysUser> userList = userService.findByIds(uid);
        if (userList == null || userList.size() <= 0) {
            return R.error("查不到用户信息");
        }
        SysUser user = userList.get(0);


        String passwordStr1 = object.getOldPassword().replace(" ", "+");
        String passwordStr2 = object.getPassword().replace(" ", "+");
        String passwordStr3 = object.getPasswordCheck().replace(" ", "+");

        String privateKey = cacheKit.getVal(token + "_privateKey");


        String password1 = RSAEncrypt.decrypt(passwordStr1, privateKey);
        String password2 = RSAEncrypt.decrypt(passwordStr2, privateKey);
        String password3 = RSAEncrypt.decrypt(passwordStr3, privateKey);

        cacheKit.deleteVal(token + "_privateKey");

        object.setOldPassword(password1);

        object.setPassword(password2);

        object.setPasswordCheck(password3);


        String oldPassword = MD5Kit.encode(object.getOldPassword() + user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            return R.error("旧密码不正确");
        }

        if (!object.getPassword().equals(object.getPasswordCheck())) {
            return R.error("两次密码不相同");
        }

        String password = MD5Kit.encode(object.getPassword() + salt);
        user.setReviser(uid);
        user.setSalt(salt);
        user.setPassword(password);

        Object o = userService.updateSysUser(user);
        if (o == null)
            return R.error("修改失败");
        return R.ok("修改成功", o);


    }

    @PutMapping("/user-pwd/{id}")
    @ApiOperation(value = "密码重置")
    public Object initPwd(@PathVariable("id") String id, @RequestHeader("token") String token) {

        if (!userService.existId(id))
            return R.error("Id信息异常");

        String uid = cacheKit.getUserId(token);
        String salt = S.randomNum();
        String pwd = S.random(8);
        String password = MD5Kit.encode(pwd + salt);
        JSONObject model = new JSONObject();
        model.put("id", id);
        model.put("password", password);
        model.put("salt", salt);
        model.put("reviser", uid);

        if (!userService.initPwd(model))
            return R.error("密码重置失败");
        model.remove("password");
        model.put("password", pwd);
        return R.ok(model);

    }

    @PutMapping("/user-status/{userId}-{status}")
    @ApiOperation(value = "冻结/启用")
    public Object initStatus(@PathVariable("userId") String userId,
                             @PathVariable("status") String status,
                             @RequestHeader("token") String token) {

        if (!userService.existId(userId))
            return R.error("Id信息异常");
        JSONObject model = new JSONObject();
        model.put("id", userId);
        model.put("status", status);

        Object o = userService.updateUser(model);
        if (o == null)
            return R.error("状态配置失败");
        return R.ok("状态配置成功");

    }

    @PutMapping("/change-role/{id}-{roleId}")
    @ApiOperation(value = "修改角色")
    public Object changeRole(@PathVariable("id") String id,
                             @PathVariable("roleId") String roleId,
                             @RequestHeader("token") String token) {

        String userId = cacheKit.getUserId(token);
        if (!userService.existId(id))
            return R.error("Id信息异常");

        SysUser sysUser = userService.selectById(id);

        String number = sysUser.getNumber();

        if (!checkUserDataAuth(token, number)) {
            return R.error("您没有权限修改" + number + "的角色");
        }


        //读取 被修改用户的用户角色关系
        SysUserRole userRole = userRoleService.findByUserId(id);
        if (userRole == null) {
            SysUserRole newUserRole = new SysUserRole();
            newUserRole.setStatus(1);
            newUserRole.setRoleId(roleId);
            newUserRole.setUserId(id);
            newUserRole.setSystemId("0");
            newUserRole.setCreator(userId);
            newUserRole = userRoleService.createUserRole(newUserRole);
            if (newUserRole == null)
                return R.error("修改失败");

            return R.ok("修改成功", fm2(newUserRole));
        }


        userRole.setRoleId(roleId);
        userRole.setReviser(userId);
        userRole = userRoleService.updateUserRole(userRole);
        if (userRole == null)
            return R.error("修改失败");

        return R.ok("修改成功", fm2(userRole));

    }

    @GetMapping("/user-system/{userId}")
    @ApiOperation(value = "根据用户Id读取开通子系统信息")
    public Object getSubSystemByUserId(@PathVariable("userId") String userId) {

        List<JSONObject> srs = userService.getSubSystemByUserId(userId);
        List<JSONObject> roles = roleService.getRoleInfo();
        roles = F.f2l(roles, "roleId");
        roles = F.f2l(roles, "systemId");
        if (srs.size() > 0) {
            for (JSONObject sr : srs) {
                sr.put("role", getRoleBySystemId(roles, sr.getString("systemId")));
            }
        }
        return R.ok(srs);
    }

    @PutMapping("/user-system/{userId}")
    @ApiOperation(value = "配置子系统信息")
    public Object setUserSystemRole(@RequestBody List<SysUserSystemRole> usr,
                                    @PathVariable("userId") String userId) {
        List<SysUserRole> urs = J.o2l(usr, SysUserRole.class);
        if (userRoleService.setUserRole(userId, urs))
            return R.ok("配置成功");
        return R.error("配置失败");
    }

    private List<JSONObject> getRoleBySystemId(List<JSONObject> roles, String systemId) {
        List<JSONObject> r = new ArrayList<>();
        if (roles.size() > 0) {
            for (JSONObject role : roles) {
                if (V.isEqual(role.getString("systemId"), systemId)) {
                    r.add(role);
                }
            }
        }
        return r;
    }

    @DeleteMapping("/user/{userId}")
    @ApiOperation(value = "删除用户信息")
    public Object deleteUser(@PathVariable("userId") String userId,
                             @RequestHeader("token") String token) {

        String uid = cacheKit.getUserId(token);
        if (!userService.existId(userId))
            return R.error("Id信息异常");

        SysUser sysUser = userService.selectById(userId);

        String number = sysUser.getNumber();

        if (!checkUserDataAuth(token, number)) {
            return R.error("您没有权限删除" + number + "");
        }


        if (userService.deleteUser(userId, uid)) {
            userBaseInfoService.deleteUserBaseInfo(userId, uid);
            userDetailInfoService.deleteUserDetailInfo(userId, uid);
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @GetMapping("/user/exist/{number}")
    @ApiOperation(value = "学号查询用户是否存在")
    public Object exsistNumber(@PathVariable("number") String number,
                               @RequestHeader("token") String token) {

        if (!userService.existNumber(number))
            return R.error("用户不存在");
        return R.ok("用户存在");

    }

    @PutMapping("/updatePersonalBaseInfo")
    @ApiOperation(value = "更改个人基本信息")
    public Object updatePersonalBaseInfo(@RequestBody PersonalBaseInfoUpdate object,
                                         @RequestHeader("token") String token) {

        String userId = "";
        if (!V.isEmpty(object.getId())) {
            userId = object.getId();
        } else {
            userId = getUserIdByCache(token);
        }
        List<SysUser> sysUserList = userService.findByIds(userId);
        String number = sysUserList.get(0).getNumber();
        if (number.isEmpty())
            return R.error("用户信息缺少学号/工号");

        if (!checkUserDataAuth(token, number)) {
            return R.error("您没有权限修改" + number + "的记录");
        }

        //映射对象
        UserBaseInfo model = o2c(object, token, UserBaseInfo.class);

        //        //数据校验
        JSONObject check = V.checkEmpty(updateBaseInfoVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

//        List<SysUser> sysUserList = sysUserService.findByIds(userId);
        if (sysUserList == null || sysUserList.size() == 0) {
            return R.error("登录超时，请重新登录");
        }


        if (number != null && (number.length() != 0 && !number.equals(model.getNumber().trim()))) {
            return R.error("请勿修改学号/工号");
        }

        if (number == null) {
            if (userBaseInfoService.existNumber(object.getNumber())) {
                return R.error("该学号/工号已存在");
            }
        }

        if (number != null) {
            List<UserBaseInfo> userBaseInfoList = userBaseInfoService.findByNumber(number);
            if (userBaseInfoList != null && userBaseInfoList.size() > 0) {
                String idCard = userBaseInfoList.get(0).getIdCard();
                String phone = userBaseInfoList.get(0).getPhone();

                if (idCard != null && idCard.trim().length() != 0 && !idCard.equals(model.getIdCard())) {
                    return R.error("请勿修改身份证");
                }

                if (idCard == null || "".equals(idCard)) {
                    if (userBaseInfoService.existIdcard(object.getIdCard())) {
                        return R.error("该身份证号已存在");
                    }
                }

                if (phone != null && phone.trim().length() != 0 && !phone.equals(model.getPhone())) {
                    if (userBaseInfoService.existPhone(object.getPhone())) {
                        return R.error("该手机号已存在");
                    }
                }

                if (phone == null) {
                    if (userBaseInfoService.existPhone(object.getPhone())) {
                        return R.error("该手机号已存在");
                    }
                }


            }
        }


        List<UserBaseInfo> userBaseInfoList = userBaseInfoService.findByNumber(number);
        if (userBaseInfoList == null || userBaseInfoList.size() <= 0) {
            model.setCreator(userId);
            model.setNumber(number);
            model = userBaseInfoService.createUserBaseInfo(model);
            if (model == null)
                return R.error("修改失败.");
        } else {
            model.setReviser(userId);
            model.setId(userBaseInfoList.get(0).getId());
            model = userBaseInfoService.updateUserBaseInfo(model);
            if (model == null)
                return R.error("修改失败");
        }
        SysUser updateUser = new SysUser();
        updateUser.setId(model.getId());
        updateUser.setNumber(model.getNumber());
        updateUser.setPhone(model.getPhone());
        userService.updateSysUser(updateUser);

        return R.ok("修改成功", fm2(model));
    }

    @PutMapping("/updatePersonalDetailInfo")
    @ApiOperation(value = "修改个人详细信息")
    public Object updatePersonalDetailInfo(@RequestBody PersonalDetailInfoUpdate object,
                                           @RequestHeader("token") String token) {

        String userId = "";
        if (!V.isEmpty(object.getId())) {
            userId = object.getId();
        } else {
            userId = getUserIdByCache(token);
        }
        List<SysUser> sysUserList = userService.findByIds(userId);
        String number = sysUserList.get(0).getNumber();
        if (number.isEmpty())
            return R.error("用户信息缺少学号/工号");
        if (!checkUserDataAuth(token, number)) {
            return R.error("您没有权限修改" + number + "的记录");
        }

        //映射对象
        UserDetailInfo model = o2c(object, token, UserDetailInfo.class);

        List<UserDetailInfo> userDetailInfoList = userDetailInfoService.findByNumber(number);
        if (userDetailInfoList == null || userDetailInfoList.size() <= 0) {
            model.setCreator(userId);
            model.setNumber(number);
            model = userDetailInfoService.createUserDetailInfo(model);
            if (model == null)
                return R.error("修改失败.");
        } else {
            model.setReviser(userId);
            //model.setPoliticalOutlook(userDetailInfoList.get(0).getPoliticalOutlook());
            //  model.setDepartCode(userDetailInfoList.get(0).getDepartCode());
            model.setId(userDetailInfoList.get(0).getId());
            model = userDetailInfoService.updateUserDetailInfo(model);
            if (model == null)
                return R.error("修改失败");
        }

        return R.ok("修改成功", fm2(model));
    }

    @GetMapping("/getPersonBaseAndDetail")
    @ApiOperation(value = "获取用户基本和详细信息")
    public Object getPersonBaseAndDetail(@RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        JSONObject back = new JSONObject();
        List<SysUser> sysUserList = userService.findByIds(userId);
        //学号/公号
        String number = sysUserList.get(0).getNumber();
        List<JSONObject> userBaseInfoList = userBaseInfoService.queryAll("and number ='" + number + "'");
        if (userBaseInfoList == null || userBaseInfoList.size() == 0)
            userBaseInfoList = null;
        back.put("userBaseInfoList", userBaseInfoList);
        back.put("userDetailInfoList", userBaseInfoList);
        return R.ok(back);

    }


    @GetMapping("/user/getUserInfoById/{id}")
    @ApiOperation(value = "根据id获取用户信息")
    public Object getUserInfoById(@PathVariable("id") String id, @RequestHeader("token") String token) {


        if (V.isEmpty(id)) {
            return R.error("该用户不存在！");
        }

        List<SysUser> sysUserList = userService.findByIds(id);

        if (V.isEmpty(sysUserList)) {
            return R.error("该用户不存在！");
        }
        //学号/公号
        List<JSONObject> userBaseInfoList = userBaseInfoService.queryAll("and id ='" + id + "'");
        if (userBaseInfoList == null || userBaseInfoList.size() == 0)
            userBaseInfoList = null;

        return R.ok(userBaseInfoList.get(0));

    }

    @PostMapping("/user/importSetting")
    @ApiOperation(value = "导入用户配置")

    public Object importSetting(@RequestBody UserImport object,
                                @RequestHeader("token") String token) {

        if (object.getRoleId() == null || object.getRoleId() == "") {
            return R.error("未选择角色!");
        }
        if (object.getAcademyCode() == null || object.getAcademyCode() == "") {
            return R.error("未选择学院!");
        }

        if (object.getType() == null || object.getType() == 0) {
            return R.error("未选择用户类型!");
        }


        String batch = S.getToken();

        cacheKit.setVal(batch + "roleId", object.getRoleId(), 0);
        cacheKit.setVal(batch + "academyCode", object.getAcademyCode(), 0);
        cacheKit.setVal(batch + "type", object.getType() + "", 0);


        return R.ok("配置成功", batch);

    }


    @PostMapping("/user/importStatus/{batch}")
    @ApiOperation(value = "导入用户状态")
    public Object getImportStatus(@PathVariable("batch") String batch) {
        if (V.isEmpty(batch)) {
            return R.error("导入批次有误！");
        }

        JSONObject obj = getImportStatusObj(batch);

        if (V.isEmpty(batch)) {
            return R.error("导入状态异常！");
        }

        return R.ok(obj);

    }


    @PostMapping("/user/import/{batch}")
    @ApiOperation(value = "导入用户")
    public Object Import(
            HttpServletRequest req,
            @PathVariable("batch") String batch,
            @RequestHeader("token") String token) {
        if (V.isEmpty(batch)) {
            return R.error("导入批次有误！");
        }

        if (cacheKit.getVal(batch + "roleId") == null) {
            return R.error("未选择角色!");
        }
        if (cacheKit.getVal(batch + "academyCode") == null) {
            return R.error("未选择学院!");
        }
        if (cacheKit.getVal(batch + "type") == null) {
            return R.error("未选择用户类型!");
        }

        String academyCode = cacheKit.getVal(batch + "academyCode");
        String examId = cacheKit.getVal(batch + "examId");
        String roleId = cacheKit.getVal(batch + "roleId");


        int type = Integer.parseInt(cacheKit.getVal(batch + "type")); //学生用户类型

        String userId = getUserIdByCache(token);

        String filepath = uploadFile(req);
        if (V.isEmpty(filepath)) {
            return R.error("文件上传失败！");
        }

        JSONObject result = new JSONObject();


        JSONObject obj = new JSONObject();
        obj.put("academyCode", academyCode);
        obj.put("examId", examId);
        obj.put("roleId", roleId);
        obj.put("type", type);
        obj.put("userid", userId);
        obj.put("token", token);
        obj.put("filepath", filepath);


        JSONObject res = SolvedFile(obj, batch);
        if (res == null) {
            return R.error("用户导入异常！");
        }
        Boolean flag =res.getBoolean("flag");
        if(!flag){
            return  R.error(res.getString("message"));
        }

        result.put("total", res.getInteger("total"));
        result.put("truecount", res.getInteger("truecount"));
        result.put("errcount", res.getInteger("errcount"));

        if (res.getInteger("truecount") == 0 && res.getInteger("errcount") == 0) {
            return R.error("导入数据为空！");
        }


        if (res.getInteger("errcount") > 0) {
            result.put("errlist", res.getJSONArray("errlist"));
            result.put("success", 0);
        } else {
            result.put("success", 1);

        }


        return R.ok("导入成功", result);
    }


    public JSONObject SolvedFile(JSONObject obj, String batch) {
        JSONObject res = new JSONObject();
        JSONArray errors = new JSONArray();
        if (obj.getString("filepath") == null || obj.getString("filepath") == "") {
            res.put("flag",false);
            res.put("message","上传文件有误！");
            return res;
        }
        try {
            File excel = new File(obj.getString("filepath"));
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    res.put("flag",false);
                    res.put("message","上传文件有误！");
                    return res;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();

                int userCount = 0, trueCount = 0;

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    //  System.out.println("rIndex: " + rIndex);

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        //String examId,String academyCode,String myclass,String name,String number,String phone,String token
                        String name, number, phone;
                        if (row.getCell(0) != null && row.getCell(1) != null) {
                            JSONObject userobj = new JSONObject();
                            number = row.getCell(0).toString().trim();
                            name = row.getCell(1).toString().trim();
                            if (row.getCell(2) != null) {
                                phone = row.getCell(2).toString().trim();
                                userobj.put("phone", phone);
                            }
                            userobj.put("name", name);
                            userobj.put("number", number);
                            if (number == null || number == "") {
                                continue;
                            }

                            userobj.put("academyCode", obj.getString("academyCode"));
                            userobj.put("roleId", obj.getString("roleId"));
                            userobj.put("type", obj.getInteger("type"));
                            userobj.put("userid", obj.getString("userid"));
                            userobj.put("token", obj.getString("token"));

                            JSONObject importObj = userService.importUser(userobj);

                            userobj.remove("academyCode");
                            userobj.remove("roleId");
                            userobj.remove("type");
                            userobj.remove("userid");
                            userobj.remove("token");

                            userCount++;
                            if (importObj != null) {
                                boolean userFlag = importObj.getBoolean("flag");
                                if (!userFlag) {
                                    userobj.put("info", importObj.getString("info"));
                                    errors.add(userobj);
                                    cacheKit.setVal(batch + "status", 2 + "", 0);
                                } else {
                                    if (obj.getString("phone") != null || obj.getString("phone") != "") {
                                        //发送消息提醒
                                        StringBuffer content = new StringBuffer();
                                        content.append("#{name}你好，" + "您的登陆账号为：" + number + ",初始密码为："
                                                + "gxun" + number.substring(number.length() - 6) + ",请尽快登陆系统修改密码！");
                                        //发送短信提醒
                                        customService.sendNote(number, "账号创建通知", content.toString(), 0, obj.getString("token"));
                                    }
                                    trueCount++;
                                    cacheKit.setVal(batch + "status", 1 + "", 0);
                                }

                            } else {
                                cacheKit.setVal(batch + "status", 2 + "", 0);
                                userobj.put("info", "其他原因");
                                errors.add(userobj);
                            }
                            cacheKit.setVal(batch + "now", rIndex + "", 0);
                        }

                        cacheKit.setVal(batch + "total", lastRowIndex + "", 0);

                        if (rIndex != lastRowIndex) {
                            cacheKit.setVal(batch + "res", 2 + "", 0);
                        }

                    }


                }

                cacheKit.setVal(batch + "res", 1 + "", 0);

                res.put("truecount", trueCount);

                res.put("errcount", errors.size());

                res.put("total", userCount);

                res.put("errlist", errors);

                res.put("flag",true);

                return res;

            } else {
                res.put("flag", false);
                res.put("message", "上传文件有误！");
                System.out.println("找不到指定的文件");
                return  res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("flag", false);
            res.put("message", "考生处理出错！");
            return  res;
        }
    }


    private Map<String, String> updateBaseInfoVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "姓名");
//        verify.put("oldName", "曾用名");
        verify.put("number", "学号/工号");
//        verify.put("sex", "性别");
//        verify.put("nationCode", "民族代码");
//        verify.put("birthday", "出生日期");
//        verify.put("graduate", "学历");
//        verify.put("degree", "学位或职称");
//        verify.put("nationality", "国籍");
//        verify.put("originPlace", "籍贯");
//        verify.put("birthPlace", "出生地");
//        verify.put("presentPesidence", "现居住地");
        verify.put("phone", "手机号");
//        verify.put("email", "邮箱");
//        verify.put("specialty", "特长");
        verify.put("idCard", "身份证");
        return verify;
    }
}
