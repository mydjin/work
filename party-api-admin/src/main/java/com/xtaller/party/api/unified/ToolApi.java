package com.xtaller.party.api.unified;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.exception.AbstractCosException;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import com.xtaller.party.api.BaseApi;

import com.xtaller.party.core.model.Note;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by party on 2018/08/23
 */
@Api(value = "系统工具")
@RestController
@RequestMapping("/v1/tool")
public class ToolApi extends BaseApi {
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private AcademyInfoService academyInfoService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private MajorInfoService majorInfoService;
    @Autowired
    private NationInfoService nationInfoService;

    @Autowired
    private SysTpsConfigService tpsConfigService;

    @GetMapping("/getDicFieldName/{fieldName}")
    @ApiOperation(value = "通过字段名读取值")
    public Object getValueByfieldName(@PathVariable("fieldName") String fieldName,
                                      @RequestHeader("token") String token) {

        String wkey = "";
        if (!V.isEmpty(fieldName))
            wkey = S.apppend(" and fieldName = '" + fieldName + "' and status = 1 ");
        return R.ok(dictionaryService.queryAll(wkey));

    }


    @GetMapping("/getRoleList/{name}")
    @ApiOperation(value = "读取角色列表")
    public Object getRoleList(//@PathVariable("index") int index,
                              //  @PathVariable("size") int size,
                              @PathVariable("name") String name,
                              @RequestHeader("token") String token) {

        List<Where> wheres = W.f(
                W.and("name", "like", name)
        );
//        String wKey = "";
//        if (!V.isEmpty(name))
//            wKey = S.apppend(" and (name like '%", name, "%')");
        return R.ok(roleService.query(wheres));
    }


    @GetMapping("/getAcademyList")
    @ApiOperation(value = "读取所有学院信息")
    public Object getAllAcademyInfo(@RequestHeader("token") String token) {
        String wKey = "";

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        if (visible == 1) {//1-全部（组织部）

            return R.ok(academyInfoService.queryAll(wKey));


        } else if (visible == 2 || visible == 3) {//2-院级

            String userAcademyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(userAcademyCode)) {
                return R.error("请先完善您的学院信息");
            }

            wKey += S.apppend(" and code = '", userAcademyCode, "' ");

            return R.ok(academyInfoService.queryAll(wKey));

        } else {
            return R.error("您没有权限查看");
        }


    }

    //通过学院代码找该学院所有专业
    @GetMapping("/getMajorList/{academyCode}")
    @ApiOperation(value = "通过学院代码找该学院所有专业信息")
    public Object getAcademyInfo(@PathVariable("academyCode") String academyCode,
                                 @RequestHeader("token") String token) {

        String wKey = "";

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        if (visible == 1) {//1-全部（组织部）
            if (!V.isEmpty(academyCode))
                wKey = S.apppend(" and academyCode = '", academyCode, "'");


            return R.ok(majorInfoService.getMajorByAcademyCode(wKey));


        } else if (visible == 2 || visible == 3) {//2-院级


            String userAcademyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(userAcademyCode)) {
                return R.error("请先完善您的学院信息");
            }
            wKey = S.apppend(" and academyCode = '", userAcademyCode, "'");


            return R.ok(majorInfoService.getMajorByAcademyCode(wKey));

        } else {
            return R.error("您没有权限查看");
        }


    }

    //通过学院代码找该学院所有专业
    @GetMapping("/getBranchList/{academyCode}")
    @ApiOperation(value = "通过学院代码找该学院所有支部信息")
    public Object getBranchByCode(@PathVariable("academyCode") String academyCode,
                                  @RequestHeader("token") String token) {


        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }
        String wKey = "";

        if (visible == 1) {//1-全部（组织部）

            return R.ok(branchService.getBranchByAcademyCode(academyCode));


        } else if (visible == 2) {//2-院级


            String userAcademyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(userAcademyCode)) {
                return R.error("请先完善您的学院信息");
            }

            return R.ok(branchService.getBranchByAcademyCode(userAcademyCode));

        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }
            String userDepartCode = getDepartCodeByCache(token);

            if (V.isEmpty(userDepartCode)) {
                return R.error("请先完善您的支部信息");
            }


            wKey += S.apppend(" and code = '", userDepartCode, "' ");

            return R.ok(branchService.queryAll(wKey));
        } else {
            return R.error("您没有权限查看");
        }


    }

    //获取所有民族信息
    @GetMapping("/getNationList")
    @ApiOperation(value = "读取所有民族信息")
    public Object getAllNationInfo(@RequestHeader("token") String token) {

        return R.ok(nationInfoService.queryAll(""));

    }

    //通过获取学校组织机构架构
    @GetMapping("/getBranchCatalogue")
    @ApiOperation(value = "读取学院和支部架构")
    public Object getAcademyCatalogue(@RequestHeader("token") String token) {

        JSONArray Catalogue = new JSONArray();

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }


        if (visible == 1) {//1-全部（组织部）
            List<JSONObject> academyList = academyInfoService.queryAll("");
            JSONObject defaultobj = new JSONObject();
            defaultobj.put("type", "all");
            defaultobj.put("id", 0);
            defaultobj.put("name", "广西民族大学");
            Catalogue.add(defaultobj);

            for (JSONObject academy : academyList) {
                JSONObject academyobj = new JSONObject();
                academyobj.put("type", "academy");
                academyobj.put("id", academy.getString("id"));
                academyobj.put("name", academy.getString("name"));
                academyobj.put("code", academy.getString("code"));
                academyobj.put("parentId", 0);

                Catalogue.add(academyobj);
                List<JSONObject> branchList = branchService.getBranchByAcademyCode(academy.getString("code"));
                for (JSONObject branch : branchList) {
                    JSONObject branchobj = new JSONObject();

                    branchobj.put("type", "branch");
                    branchobj.put("id", branch.getString("id"));
                    branchobj.put("name", branch.getString("name"));
                    branchobj.put("code", branch.getString("code"));
                    branchobj.put("parentId", academy.getString("id"));
                    Catalogue.add(branchobj);
                }
            }

            return R.ok(Catalogue);


        } else if (visible == 2) {//2-院级

            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }
            String wKey = S.apppend(" and code = '", academyCode, "' ");
            List<JSONObject> academyList = academyInfoService.queryAll(wKey);
            if (V.isEmpty(academyList)) {
                return R.error("您的学院信息异常");
            }
            JSONObject academyobj = new JSONObject();
            academyobj.put("type", "academy");
            academyobj.put("id", academyList.get(0).getString("id"));
            academyobj.put("name", academyList.get(0).getString("name"));
            academyobj.put("code", academyList.get(0).getString("code"));
            Catalogue.add(academyobj);
            List<JSONObject> branchList = branchService.getBranchByAcademyCode(academyCode);
            for (JSONObject branch : branchList) {
                JSONObject branchobj = new JSONObject();
                branchobj.put("type", "branch");
                branchobj.put("id", branch.getString("id"));
                branchobj.put("name", branch.getString("name"));
                branchobj.put("code", branch.getString("code"));
                branchobj.put("parentId", academyList.get(0).getString("id"));
                Catalogue.add(branchobj);
            }
            return R.ok(Catalogue);


        } else if (visible == 3) {
            //fixme: 暂不支持对个人发送信息
            //3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }
            String departCode = getDepartCodeByCache(token);

            if (V.isEmpty(departCode)) {
                return R.error("请先完善您的支部信息");
            }
            String wKey = S.apppend(" and code = '", departCode, "' ");
            List<JSONObject> branchList = branchService.queryAll(wKey);
            if (V.isEmpty(branchList)) {
                return R.error("您的支部信息异常");
            }
            JSONObject branchobj = new JSONObject();
            branchobj.put("type", "branch");
            branchobj.put("id", branchList.get(0).getString("id"));
            branchobj.put("name", branchList.get(0).getString("name"));
            branchobj.put("code", branchList.get(0).getString("code"));
            Catalogue.add(branchobj);

            return R.ok(Catalogue);

        } else {
            return R.error("您没有权限获取支部信息");
        }


    }

    //校验和存储外网图片
    @GetMapping("/savePicUrl/{url}/")
    @ApiOperation(value = "存储外网图片")
    public Object saveUrl(@PathVariable("url") String url) throws AbstractCosException {
        JSONObject cos = tpsConfigService.getByCode("cos");
        Credentials cred = new Credentials(cos.getLong("appId"),
                cos.getString("secretId"),
                cos.getString("secretKey"));
        ClientConfig clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
        clientConfig.setRegion(cos.getString("region"));
        COSClient cosClient = new COSClient(clientConfig, cred);
        Date date = new Date();
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(date);

        String path = "/partySystem/picture/" + "pic_" + now + "_" + generateString(8) + "." + getSuffix(url);

        String decodeUrl = decode(url);

        try {
            URL downurl = new URL(decodeUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) downurl.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);

            UploadFileRequest uploadFileRequest = new UploadFileRequest(cos.getString("bucket"), path, data);
            String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
            JSONObject json = JSONObject.parseObject(uploadFileRet);
            return R.ok("上传数据", json);


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return R.error("上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败");

        }

    }

    String getSuffix(String _fileName) {
        String suffix = "jpg";
        String[] item = _fileName.split(".");
        if (item.length > 0) {
            suffix = item[(item.length - 1)];
        }
        return suffix;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static String decode(String url) {
        //   try {
        String prevURL = "";
        String decodeURL = url;
        while (!prevURL.equals(decodeURL)) {
            prevURL = decodeURL;
            decodeURL = decodeURL.replace("*2", ":");
            decodeURL = decodeURL.replace("*1", "/");
            decodeURL = decodeURL.replace("*3", "?");
            decodeURL = decodeURL.replace("*4", "=");
            decodeURL = decodeURL.replace("*5", "&");
            decodeURL = decodeURL.replace("*6", ".");

        }
        return decodeURL;

    }

    @Autowired
    private SysUserService userService;

    @GetMapping("/user/exist/{number}")
    @ApiOperation(value = "学号查询用户是否存在")
    public Object exsistNumber(@PathVariable("number") String number,
                               @RequestHeader("token") String token) {

        if (!userService.existNumber(number))
            return R.error("用户不存在");
        return R.ok("用户存在");

    }


    public String generateString(int length) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

}
