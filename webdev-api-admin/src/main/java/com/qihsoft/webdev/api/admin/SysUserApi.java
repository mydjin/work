package com.qihsoft.webdev.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.doc.SysUserInfoUpdate;
import com.qihsoft.webdev.doc.UpdatePassword;
import com.qihsoft.webdev.core.model.SysUser;
import com.qihsoft.webdev.core.model.SysUserInfo;
import com.qihsoft.webdev.core.model.SysUserRole;
import com.qihsoft.webdev.core.service.impl.SysUserInfoService;
import com.qihsoft.webdev.core.service.impl.SysUserRoleService;
import com.qihsoft.webdev.core.service.impl.SysUserService;
import com.qihsoft.webdev.doc.SysUserReg;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.kit.MD5Kit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Qihsoft on 2017/9/9.
 */
@Api(value = "106_用户管理")
@RestController
@RequestMapping("/v1/sys")
public class SysUserApi extends BaseApi {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysUserInfoService userInfoService;


    @GetMapping("/user/{index}-{size}-{loginName}")
    @ApiOperation(value = "读取用户列表")
    public Object getUserList(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("loginName") String loginName,
                              @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            return R.ok(userService.page(index, size, loginName));
        }
    }

    @PostMapping("/user")
    @ApiOperation(value = "登记用户")
    public Object createUser(@RequestBody SysUserReg object, @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            JSONObject user = cacheKit.user(token);

            if (V.isEmpty(object.getLoginName()))
                return R.error("请输入登录名");
            if (V.isEmpty(object.getPassword()))
                return R.error("请输入登录密码");
            if (V.isEmpty(object.getType()))
                return R.error("请输入用户类型");
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
            model.put("attr", formUserInfo(object.getAttr()));
            model.put("roleId", object.getRoleId());


            JSONObject o = (JSONObject) userService.createUser(model);
            if (o == null) {
                return R.error("用户创建失败");
            } else {
                SysUserInfo userBaseInfo = o2c(new SysUserInfo(), token, SysUserInfo.class);
                userBaseInfo.setId(o.getString("id"));
                userBaseInfo.setType(object.getType());
                userBaseInfo.setName(object.getAttr().getTrueName());
                userBaseInfo.setNumber(object.getNumber());
                userBaseInfo.setPhone(object.getAttr().getMobile());
                userInfoService.createSysUserInfo(userBaseInfo);
                return R.ok(o);
            }
        }
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "密码修改")
    public Object updatePassword(@RequestBody UpdatePassword object, @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String uid = cacheKit.getUserId(token);
            int salt = S.randomNums();
            List<SysUser> userList = userService.findByIds(uid);
            if (userList == null || userList.size() <= 0) {
                return R.error("查不到用户信息");
            }
            SysUser user = userList.get(0);

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

    }

    @PutMapping("/user-pwd/{id}")
    @ApiOperation(value = "密码重置")
    public Object initPwd(@PathVariable("id") String id, @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
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
    }

    @PutMapping("/user-status/{userId}-{status}")
    @ApiOperation(value = "冻结/启用")
    public Object initStatus(@PathVariable("userId") String userId,
                             @PathVariable("status") String status,
                             @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
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
    }

    @PutMapping("/change-role/{id}-{roleId}")
    @ApiOperation(value = "修改角色")
    public Object changeRole(@PathVariable("id") String id,
                             @PathVariable("roleId") String roleId,
                             @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String userId = cacheKit.getUserId(token);
            if (!userService.existId(id))
                return R.error("Id信息异常");

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
    }


    @PutMapping("/user/{userId}")
    @ApiOperation(value = "修改用户信息")
    public Object updateUserInfo(@PathVariable("userId") String userId,
                                 @RequestBody SysUserInfoUpdate object,
                                 @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String uid = cacheKit.getUserId(token);

            //映射对象
            SysUserInfo model = o2c(object, token, SysUserInfo.class);
            //数据校验
            JSONObject check = V.checkEmpty(updateBaseInfoVerify(), object);
            if (check.getBoolean("check"))
                return R.error(check.getString("message"));
            model.setId(userId);
            model.setReviser(uid);
            model = userInfoService.updateSysUserInfo(model);
            if (model == null) {
                return R.error("用户信息修改失败");
            } else {
                SysUser user = o2c(new SysUser(), token, SysUser.class);
                user.setId(model.getId());
                user.setReviser(uid);
                user.setPhone(model.getPhone());
                user.setNumber(model.getNumber());
                user = userService.updateSysUser(user);
                return R.ok("用户信息修改成功");
            }


        }
    }

    @DeleteMapping("/user/{userId}")
    @ApiOperation(value = "删除用户")
    public Object deleteUser(@PathVariable("userId") String userId,
                             @RequestHeader("token") String token) {

        String uid = cacheKit.getUserId(token);
        if (!userService.existId(userId))
            return R.error("Id信息异常");

        if (userService.deleteUser(userId, uid)) {
            userInfoService.deleteSysUserInfo(userId, uid);
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

    @PutMapping("/updatePersonalInfo")
    @ApiOperation(value = "更改个人信息")
    public Object updatePersonalBaseInfo(@RequestBody SysUserInfoUpdate object,
                                         @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        List<SysUser> sysUserList = userService.findByIds(userId);
        String number = sysUserList.get(0).getNumber();
        if (number.isEmpty())
            return R.error("用户信息缺少学号/工号");

        //映射对象
        SysUserInfo model = o2c(object, token, SysUserInfo.class);

        //数据校验
        if (V.isEmpty(object.getType())) {
            R.error("请选择用户身份！");
        }

        //数据校验

        JSONObject check = V.checkEmpty(updateBaseInfoVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        if (sysUserList == null || sysUserList.size() == 0) {
            return R.error("登录超时，请重新登录");
        }


         /*   if (number != null && number.length() != 0 && !number.equals(model.getNumber().trim())) {
                return R.error("请勿修改学号");
            }

            if (number != null) {
                List<SysUserInfo> userInfoList = userInfoService.findByNumber(number);
                if (userInfoList != null && userInfoList.size() > 0) {
                    String idCard = userInfoList.get(0).getIdCard();

                    if (idCard != null && idCard.trim().length() != 0 && !idCard.equals(model.getIdCard())) {
                        return R.error("请勿修改身份证");
                    }
                }
            }*/

        List<SysUserInfo> userInfoList = userInfoService.findByNumber(number);
        if (userInfoList == null || userInfoList.size() <= 0) {
            model.setCreator(userId);
            model.setNumber(number);
            model = userInfoService.createSysUserInfo(model);
            if (model == null)
                return R.error("修改失败.");
        } else {
            model.setReviser(userId);
            model.setId(userInfoList.get(0).getId());
            model = userInfoService.updateSysUserInfo(model);
            if (model == null)
                return R.error("修改失败");
        }

        return R.ok("修改成功", fm2(model));

    }

    @GetMapping("/getPersonInfo")
    @ApiOperation(value = "获取用户信息")
    public Object getPersonBaseAndDetail(@RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        JSONObject back = new JSONObject();
        List<SysUser> sysUserList = userService.findByIds(userId);
        //学号/公号
        String number = sysUserList.get(0).getNumber();

        List<SysUserInfo> userInfoList = userInfoService.findByNumber(number);
        if (userInfoList == null || userInfoList.size() == 0)
            userInfoList = null;

        back.put("userInfoList", userInfoList);
        return R.ok(back);

    }


    private Map<String, String> updateBaseInfoVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "姓名");
        verify.put("number", "学号/工号");
        verify.put("phone", "手机号");
        verify.put("idCard", "身份证");
        return verify;
    }


}
