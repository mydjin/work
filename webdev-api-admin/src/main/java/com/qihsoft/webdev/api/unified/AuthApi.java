package com.qihsoft.webdev.api.unified;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.core.model.*;
import com.qihsoft.webdev.core.service.impl.*;
import com.qihsoft.webdev.doc.ForgetPassword;
import com.qihsoft.webdev.doc.SysLogin;
import com.qihsoft.webdev.utils.convert.*;
import com.qihsoft.webdev.utils.tool.SMS_func;
import com.qihsoft.webdev.core.async.TokenAsync;
import com.qihsoft.webdev.core.tps.CacheKit;
import com.qihsoft.webdev.utils.kit.MD5Kit;
import com.qihsoft.webdev.utils.kit.OSKit;
import com.qihsoft.webdev.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qihsoft on 2017/10/13
 */
@Api(value = "系统授权管理")
@RestController
@RequestMapping("/v1/common")
@CrossOrigin   //跨域服务注解
public class AuthApi extends BaseApi {
    @Autowired
    private TokenAsync tokenAsync;
    @Autowired
    public SysUserService sysUserService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysGlobalConfigService configService;
    @Autowired
    private CacheKit cacheKit;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private SysUserInfoService userInfoService;
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOperationRecordService sysOperationRecordService;

    @GetMapping("/verify-code")
    @ApiOperation(value = "获取登录验证码")
    public Object getSysVerifyCode(HttpServletRequest request) {
        JSONObject verify = new JSONObject();
        String ip = getIpAddr(request);
        String token = "";
        // 检查缓存
        String oldToken = cacheKit.getVal(ip);
        verify = J.s2j(cacheKit.getVal(oldToken));
        if (oldToken.length() == 0 || V.isEmpty(verify)) {
            verify = new JSONObject();
            String code = S.random(4);
            token = S.getToken();
            verify.put("ip", ip);
            verify.put("code", code);
            verify.put("token", token);
            cacheKit.setVal(ip, token, 120);
            cacheKit.setVal(token, J.o2s(verify), 120);
        } else {

            System.out.println(cacheKit.getVal(oldToken));
            verify.put("code", S.random(4));

            cacheKit.setVal(ip, oldToken, 120);
            cacheKit.setVal(oldToken, J.o2s(verify), 120);
        }
        return R.ok("获取验证码成功", verify);
    }



    @Autowired
    private Producer captchaProducer;

    @GetMapping("/verify-code/jpg")
    @ApiOperation(value = "获取登录验证码")
    public Object getSysVerifyCodePic(HttpServletRequest request) {

        String capText = captchaProducer.createText();

        JSONObject verify = new JSONObject();
        String ip = getIpAddr(request);
        String token = "";
        token = S.getToken();
        verify.put("ip", ip);
        verify.put("code", capText);
        verify.put("token", token);
        //  cacheKit.setVal(ip, token, 120);
        cacheKit.setVal(token, J.o2s(verify), 120);
        JSONObject res = new JSONObject();
        res.put("token", token);

        try {
            BufferedImage bi = captchaProducer.createImage(capText);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
            ImageIO.write(bi, "jpg", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
            res.put("jpg", "data:image/*;base64," + png_base64);

            return R.ok("获取验证码成功", res);


        } catch (IOException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户统一登录")
    public Object login(@RequestBody SysLogin object,
                        @RequestHeader("token") String token) {
        // 操作日记初始化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysOperationRecord sysOperationRecord = new SysOperationRecord();
        String ip = getIpAddr(request);
        sysOperationRecord.setIpAddr(ip);
        // 数据验证
        String str = cacheKit.getVal(token);
        if (V.isEmpty(str))
            return R.error("验证码已经过期请重新获取验证码");

        JSONObject json = J.s2j(str);
        String code = json.getString("code");
        System.out.println("code=" + code);
        System.out.println("object.getCode()=" + object.getCode());
        if (V.isEmpty(code))
            return R.error("验证码已经过期请重新获取验证码");
        if (V.isEmpty(object.getCode()))
            return R.error("请输入验证码");
        if (!V.isEqual(code, object.getCode()))
            return R.error("您输入的验证码不正确");
        if (V.isEmpty(token))
            return R.error("请先获取验证码");
        if (V.isEmpty(object.getLoginName()))
            return R.error("请输入登录名");
        if (V.isEmpty(object.getPassword()))
            return R.error("请输入登录密码");

        // 业务处理
        JSONObject user = J.o2j(sysUserService.findByLoginName(object.getLoginName()));
        if (user == null) {
            return R.error("登录名或密码错误");
        }


        //存在该用户
//        SysUser sysUser = user.getObject("sysUser",SysUser.class);
        SysUser sysUser = sysUserService.findByUserName(object.getLoginName());

        sysOperationRecord.setCreator(user.getString("id"));
        sysOperationRecord.setCreateTime((int) TimeKit.getTimestamp());
        sysOperationRecord.setControl("系统授权管理");
        sysOperationRecord.setFunction("用户统一登录");
        sysOperationRecord.setServer(adminServer);
        //sysOperationRecord.setParameter(JSON.toJSONString(object));登录时不存储密码

        //当前时间
        Long nowTime = TimeKit.getTimestamps();
        Long unlockTime = sysUser.getUnlockTime().longValue() * 1000;
        Integer errorCount = sysUser.getErrorCount();


        //判断是否禁用
        if (sysUser.getStatus() == 0) {
            return R.error("您的账号当前已被禁用，请联系管理员");
        }

//        System.out.println(TimeKit.stampToDate(nowTime));
//        System.out.println(TimeKit.stampToDate(unlockTime));
        String unlockTimeStr = TimeKit.stampToDate(unlockTime, "yyyy-MM-dd HH:mm");
        //解锁时间大于当前时间
        if (unlockTime > nowTime) {
            return R.error("您的账号暂时被冻结，请" + unlockTimeStr + "后再试");
        }

        //解锁时间已过，再次登录时重置解锁时间和登录次数
        if (unlockTime != 0) {
            //当前时间已经超过解锁时间的就重置解锁时间为0
            sysUser.setUnlockTime(0);
            sysUser.setErrorCount(0);
            sysUserService.update(sysUser);
            errorCount = sysUser.getErrorCount();
        }

        int canError = 5;
        List<Dictionary> dictionary = dictionaryService.findByFieldName("errorCount");
        Integer status = dictionary.get(0).getStatus();

        String userId = user.getString("id");

        if (status == 1) {//1-启用

            try {
                canError = Integer.parseInt(dictionary.get(0).getValue());
            } catch (Exception e) {

            }

            //次数大于设定值则禁用，默认5次
            if (errorCount == canError) {
                sysOperationRecord.setStatus(1);
                sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
                return R.error("尝试登录次数已超过" + canError + "次，请" + unlockTimeStr + "后再试");
            }


            // 密码处理
            String salt = user.getString("salt");
            String pwd = MD5Kit.encode(object.getPassword() + salt);

            //密码错误
            if (!V.isEqual(pwd, user.getString("password"))) {
                sysUser.setId(userId);
                sysUser.setErrorCount(errorCount + 1);
                System.out.println(canError == sysUser.getErrorCount());
                if (canError == sysUser.getErrorCount()) {
                    List<Dictionary> lockTimeData = dictionaryService.findByFieldName("lockTime");
                    //锁定时间
                    int lockTime = 10;//默认锁定10分钟
                    if (lockTimeData.get(0).getStatus() == 1) {//若启用字典设定值，则使用字典设定值
                        try {
                            lockTime = Integer.parseInt(lockTimeData.get(0).getValue());
                        } catch (Exception e) {

                        }
                    }

                    //解锁时间=现在时间的秒数数+lockTime分钟转为秒
                    sysUser.setUnlockTime(Integer.parseInt(nowTime / 1000 + "") + lockTime * 60);
                    sysUserService.update(sysUser);
                    sysOperationRecord.setStatus(1);
                    sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
                    return R.error("密码错误，账号已被锁定" + lockTime + "分钟");
                }
                sysUserService.update(sysUser);
                sysOperationRecord.setStatus(1);
                sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
                return R.error("密码错误，您还有" + (canError - sysUser.getErrorCount()) + "次尝试机会");
            }
        } else if (status == 2) {//2-禁用状态则不限制登录次数，只校验对错
            String salt = user.getString("salt");
            String pwd = MD5Kit.encode(object.getPassword() + salt);

            //密码错误
            if (!V.isEqual(pwd, user.getString("password"))) {
                return R.error("密码错误");
            }
        }
        // 数据处理
        user.remove("password");
        user.remove("salt");
        String userToken = S.getToken();
        user.put("token", userToken);
        user.put("role", "");
        user.put("lock", 0);
        user.put("auth", new ArrayList<>());
        // 权限
        SysUserRole ur = userRoleService.findByUserId(userId);
        if (ur != null) {
//            user.put("auth", sysUserService.getUserAuth(user.getString("id")));
            user.put("auth", roleService.getAuthInfoByRoleId(ur.getRoleId()));
            user.put("role", ur.getRoleId());
        }


        SysUserToken ut = new SysUserToken();
        ut.setServer(adminServer);
        ut.setType("redis");
        ut.setUserId(userId);
        ut.setToken(userToken);
        tokenAsync.updateToken(ut);

        List<JSONObject> sysUserRoles = sysRoleService.getUserRoleByUserId(userId);
        if (!V.isEmpty(sysUserRoles)) {
//            return R.error("没有权限");

            JSONObject sysUserRole = sysUserRoles.get(0);
            Integer visible = sysUserRole.getInteger("value");
            if (!V.isEmpty(visible)) {
                user.put("visible", visible);
            } else {
                user.put("visible", null);
            }

            //获取登录者的学号/工号
            String loginNumber = sysUserRole.getString("number");
            if (!V.isEmpty(loginNumber)) {
                user.put("loginNumber", loginNumber);
                // 查询部门代码
                List<SysUserInfo> userInfoList = userInfoService.query(W.f(
                        W.and("number", "eq", loginNumber)
                ));
                if (!V.isEmpty(userInfoList)) {
                    SysUserInfo userInfo = userInfoList.get(0);
                    String academyCode = userInfo.getAcademyCode();
                    if (!V.isEmpty(academyCode)) {
                        user.put("academyCode", academyCode);
                    } else {
                        user.put("academyCode", null);
                    }
                }

            }

        }

        // 缓存处理
        if (initCacheUser(user, userToken, 0)) {
            sysUser.setErrorCount(0);
            sysUser.setUnlockTime(0);
            sysUser.setVerifyCode("0");
            sysUserService.update(sysUser);
            sysOperationRecord.setCreator(user.getString("id"));
            sysOperationRecord.setStatus(0);
            sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
            return R.ok(user);
        }
        return R.ok("缓存处理失败", user);
    }


    @GetMapping("/resources/{time}/{fileName}.{suffix}")
    @ApiOperation(value = "读取文件")
    public void getFile(@PathVariable("time") String time,
                        @PathVariable("fileName") String fileName,
                        @PathVariable("suffix") String suffix,
                        HttpServletResponse response) throws IOException {
        try {
            JSONObject setting = configService.getSetting();
            Boolean linux = OSKit.isLinux();
            String filePath = "";
            if (linux) {
                filePath = S.apppend(setting.getString("linuxPath"), "/", time, "/", fileName, ".", suffix);
            } else {
                filePath = S.apppend(setting.getString("basePath"), "\\", time, "\\", fileName, ".", suffix);
            }
            FileInputStream inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            //byte数组用于存放图片字节数据
            byte[] buff = new byte[i];
            inputStream.read(buff);
            //记得关闭输入流
            inputStream.close();
            //设置发送到客户端的响应内容类型
            response.setContentType("image/*");
            OutputStream out = response.getOutputStream();
            out.write(buff);
            //关闭响应输出流
            out.close();
        } catch (Exception ex) {

        }
    }


    @GetMapping("/verify_code/{phone}")
    @ApiOperation(value = "获取找回密码验证码")
    public Object getVerifyCode(@PathVariable String phone) {
        // 验证该手机号是否已绑定
        SysUser sysUser = sysUserService.findByPhone(phone);
        if (sysUser == null) {
            return R.error("该手机号未被绑定");
        }

        //当前时间
        Long nowTime = TimeKit.getTimestamps();
        Long unlockTime = sysUser.getUnlockTime().longValue() * 1000;
        Integer errorCount = sysUser.getErrorCount();

        //判断是否禁用
        if (sysUser.getStatus() == 0) {
            return R.error("您的账号当前已被禁用，请联系管理员");
        }

//        System.out.println(TimeKit.stampToDate(nowTime));
//        System.out.println(TimeKit.stampToDate(unlockTime));
        int waitTime = Integer.parseInt(TimeKit.stampToDate(unlockTime - nowTime, "mm"));
        //解锁时间大于当前时间
        if (unlockTime > nowTime) {
            return R.error("你的账号暂时被冻结，请" + waitTime + "分钟后再试");
        }
        //当前时间已经超过解锁时间的就重置解锁时间为0
        sysUser.setUnlockTime(0);

        //次数大于大于设定值则禁用，默认5次
        int canError = 5;
        List<Dictionary> dictionary = dictionaryService.findByFieldName("errorCount");

        Integer status = dictionary.get(0).getStatus();
        if (status == 1) {//1-启用
            try {
                canError = Integer.parseInt(dictionary.get(0).getValue());
            } catch (Exception e) {

            }

            if (errorCount == canError) {
                return R.error("您尝试登录的次数已超过" + canError + "次,请" + waitTime + "分钟后再试");
            }
        }


        String verifyCode = S.randomNum();

        sysUser.setVerifyCode(verifyCode);
        sysUserService.update(sysUser);

        String content = "验证码：" + verifyCode + "您正在使用重置密码功能，该验证码仅用于身份验证，请勿泄露给他人使用。（由系统快速开发平台应用发送，请勿回复）";


        //发送1-验证码
        if (sysUserService.update(sysUser) == true) {
            Note note = new Note();
            note.setPhone(phone);
            note.setType(1);
            note.setDetail(content);
            note.setSendTime((int) TimeKit.getTimestamp());
            note.setTheme("找回密码");

            if (SMS_func.DirectSendSMS(phone, content)) {
                note.setSendStatus(2);//2-已发送-成功
                noteService.createNote(note);
                return R.ok("发送成功", verifyCode);

            } else {
                note.setSendStatus(1);//1-未发送-失败
                noteService.createNote(note);
                return R.error("发送失败");

            }

        } else {
            return R.error("发送失败");
        }
    }


    @PostMapping("/forget")
    @ApiOperation(value = "忘记密码")
    public Object forgetPassword(@RequestBody ForgetPassword object) {

        // 操作日记初始化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysOperationRecord sysOperationRecord = new SysOperationRecord();
        String ip = getIpAddr(request);
        sysOperationRecord.setIpAddr(ip);

        if (V.isEmpty(object.getPhone()))
            return R.error("请输入手机号码");
        if (V.isEmpty(object.getVerifyCode()))
            return R.error("请输入验证码");
        if (!object.getNewPassword1().equals(object.getNewPassword2())) {
            return R.error("新密码不一致");
        }
        wheres = W.f(
                W.and("phone", "eq", object.getPhone()),
                W.and("verifyCode", "eq", object.getVerifyCode()),
                W.and("isDel", "eq", "0")
        );
        List<SysUser> users = sysUserService.query(wheres);
        if (users == null || users.size() == 0) {
            return R.error("请检查手机号或验证码");
        }
        SysUser userInfo = users.get(0);


        sysOperationRecord.setCreator(userInfo.getId());
        sysOperationRecord.setCreateTime((int) TimeKit.getTimestamp());
        sysOperationRecord.setControl("系统授权管理");
        sysOperationRecord.setFunction("重置密码");
        sysOperationRecord.setServer(adminServer);
        // sysOperationRecord.setParameter(JSON.toJSONString(object));登录时不存储密码

        JSONObject user = new JSONObject();
        String salt = S.randomNum();
        String password = MD5Kit.encode(S.apppend(object.getNewPassword1(), salt));
        user.put("id", userInfo.getId());
        user.put("salt", Integer.parseInt(salt));
        user.put("password", password);
        user.put("verifyCode", "");
        user.put("errorCount", 0);
        user.put("unlockTime", 0);
        Object o = sysUserService.updateUser(user);
        if (o == null) {
            sysOperationRecord.setStatus(1);
            sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
            return R.error("密码修改失败");

        } else {
            sysOperationRecord.setStatus(0);
            sysOperationRecordService.createSysOperationRecord(sysOperationRecord);
            return R.ok("密码找回成功");
        }


    }


}
