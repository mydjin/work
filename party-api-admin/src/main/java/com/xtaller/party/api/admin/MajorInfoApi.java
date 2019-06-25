package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.MajorInfo;
import com.xtaller.party.core.service.impl.AcademyInfoService;
import com.xtaller.party.core.service.impl.MajorInfoService;
import com.xtaller.party.doc.MajorInfoCreate;
import com.xtaller.party.doc.MajorInfoUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/23
 */
@Api(value = "06_专业信息管理")
@RestController
@RequestMapping("/v1/base")
public class MajorInfoApi extends BaseApi {
    @Autowired
    private MajorInfoService majorInfoService;
    @Autowired
    private AcademyInfoService academyInfoService;

    @PostMapping("/majorInfo")
    @ApiOperation(value = "专业信息新增")
    public Object createMajorInfo(@RequestBody MajorInfoCreate object,
                                  @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        MajorInfo model = o2c(object, token, MajorInfo.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = majorInfoService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        Boolean nameexist = majorInfoService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");


        model = majorInfoService.createMajorInfo(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/majorInfo")
    @ApiOperation(value = "修改专业信息")
    public Object updateMajorInfo(@RequestBody MajorInfoUpdate object,
                                  @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        MajorInfo model = o2c(object, token, MajorInfo.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        MajorInfo data = majorInfoService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!model.getCode().equals(data.getCode())) {
            Boolean exist = majorInfoService.exist(W.f(
                    W.and("code", "eq", model.getCode()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("代码已经存在请更换一个代码");
        }
        if (!model.getName().equals(data.getName())) {
            Boolean nameexist = majorInfoService.exist(W.f(
                    W.and("name", "eq", model.getName()),
                    W.and("isDel", "eq", "0"))
            );
            if (nameexist)
                return R.error("名称已经存在请更换一个名称");
        }


        model.setReviser(userId);
        model = majorInfoService.updateMajorInfo(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/majorInfo/{id}")
    @ApiOperation(value = "专业信息删除")
    public Object deleteMajorInfo(@PathVariable("id") String id,
                                  @RequestHeader("token") String token) {

        if (!majorInfoService.existId(id))
            return R.error("Id数据异常");

        if (majorInfoService.deleteMajorInfo(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/majorInfo/{index}-{size}-{name}")
    @ApiOperation(value = "读取专业信息分页列表")
    public Object getMajorInfo(@PathVariable("index") int index,
                               @PathVariable("size") int size,
                               @PathVariable("name") String name,

                               @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(name))
            wKey = S.apppend(" and a.name like '%", name, "%' ");
//        if (!V.isEmpty(code))
//            wKey += S.apppend(" and (code = '", code , "')");

        return R.ok(majorInfoService.page(index, size, wKey));

    }

    @GetMapping("/majorInfo")
    @ApiOperation(value = "读取专业信息所有列表")
    public Object getAllMajorInfo(@RequestHeader("token") String token) {

        return R.ok(majorInfoService.queryAll(""));

    }

    @GetMapping("/majorInfoByAcademy/{academyCode}")
    @ApiOperation(value = "读取专业信息列表by学院code")
    public Object getAllMajorInfoByAcademyCode(@PathVariable("academyCode") String academyCode,
                                               @RequestHeader("token") String token) {

        if (!V.isEmpty(academyCode))
            academyCode = S.apppend(" and academyCode = '", academyCode, "' ");
        return R.ok(majorInfoService.queryAll(academyCode));

    }

    @PostMapping("/majorInfo/import/{batch}")
    @ApiOperation(value = "导入专业")
    public Object Import(
            HttpServletRequest req,
            @PathVariable("batch") String batch,
            @RequestHeader("token") String token) {
        if (V.isEmpty(batch)) {
            return R.error("导入批次有误！");
        }


        String filepath = uploadFile(req);
        if (V.isEmpty(filepath)) {
            return R.error("文件上传失败！");
        }


        JSONObject result = new JSONObject();

        JSONObject obj = new JSONObject();

        obj.put("token", token);
        obj.put("filepath", filepath);


        JSONObject res = SolvedFile(obj, batch);
        if (res == null) {
            return R.error("专业导入异常！");
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


    @PostMapping("/majorInfo/importStatus/{batch}")
    @ApiOperation(value = "导入专业状态")
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


    public JSONObject SolvedFile(JSONObject obj, String batch) {
        JSONObject res = new JSONObject();
        JSONArray errors = new JSONArray();
        if (obj.getString("filepath") == null || obj.getString("filepath") == "") {
            res.put("flag",false);
            res.put("message","上传文件有误！");
            return res;
        }
        if (obj.getString("token") == null || obj.getString("token") == "") {
            res.put("flag",false);
            res.put("message","用户授权失败！");
            return res;
        }
        String token = obj.getString("token");


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

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是标题所以不读
                int lastRowIndex = sheet.getLastRowNum();

                int dealCount = 0, trueCount = 0;

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    //  System.out.println("rIndex: " + rIndex);

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        String name = "", code = "", academyName = "";

                        if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                            JSONObject importObj = new JSONObject();
                            code = row.getCell(0).toString().trim();//专业代码
                            name = row.getCell(1).toString().trim();//专业名称
                            academyName = row.getCell(2).toString().trim();//学院名称
                            importObj.put("code", code);
                            importObj.put("name", name);
                            importObj.put("academyName", academyName);

                            boolean importFlag = false;
                            String userId = getUserIdByCache(token);
                            dealCount++;

                            Boolean nameexist = majorInfoService.exist(W.f(
                                    W.and("name", "eq", name),
                                    W.and("isDel", "eq", "0"))
                            );
                            Boolean codeexist = majorInfoService.exist(W.f(
                                    W.and("code", "eq", code),
                                    W.and("isDel", "eq", "0"))
                            );
                            if (nameexist || codeexist) {
                                importFlag = false;
                                if (nameexist) {
                                    importObj.put("info", "专业名称已存在");
                                }
                                if (codeexist) {
                                    importObj.put("info", "专业代码已存在");
                                }
                            } else {
                                JSONObject academyObj = academyInfoService.queryByName(academyName);

                                if (V.isEmpty(academyObj)) {
                                    importFlag = false;
                                    importObj.put("info", "学院名称不存在");
                                } else {

                                    MajorInfo info = new MajorInfo();
                                    info.setCode(code);
                                    info.setName(name);
                                    info.setCreator(userId);
                                    info.setAcademyCode(academyObj.getString("code"));
                                    info.setCampus(academyObj.getInteger("campus"));

                                    info = majorInfoService.createMajorInfo(info);
                                    if (info == null) {
                                        importFlag = false;
                                        importObj.put("info", "专业信息保存失败");
                                    } else {
                                        importFlag = true;

                                    }
                                }


                            }


                            if (!importFlag) {
                                if (V.isEmpty(importObj.getString("info"))) {
                                    importObj.put("info", "专业信息创建失败");
                                }
                                errors.add(importObj);
                                cacheKit.setVal(batch + "status", 2 + "", 0);
                            } else {
                                trueCount++;
                                cacheKit.setVal(batch + "status", 1 + "", 0);
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

                res.put("total", dealCount);

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
            res.put("message", "专业处理出错！");
            return  res;
        }
    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入专业代码");
        verify.put("academyCode", "请输入所属学院");
        verify.put("name", "请输入专业名称");
        verify.put("status", "请输入状态");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入专业代码");
        verify.put("academyCode", "请选择所属学院");
        verify.put("name", "请输入专业名称");
        verify.put("status", "请输入状态");
        return verify;
    }


    //通过学院代码找该学院所有专业
    @GetMapping("/majorInfo/majors/{academyCode}")
    @ApiOperation(value = "通过学院代码找该学院所有专业")
    public Object getAcademyInfo(@PathVariable("academyCode") String academyCode,
                                 @RequestHeader("token") String token) {
        String wKey = "";
        if (!V.isEmpty(academyCode))
            wKey = S.apppend(" and academyCode = '", academyCode, "'");
        return R.ok(majorInfoService.getMajorByAcademyCode(wKey));

    }


}
