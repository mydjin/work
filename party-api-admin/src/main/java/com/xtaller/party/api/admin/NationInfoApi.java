package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.NationInfo;
import com.xtaller.party.core.service.impl.NationInfoService;
import com.xtaller.party.doc.NationInfoCreate;
import com.xtaller.party.doc.NationInfoUpdate;
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
@Api(value = "04_民族信息管理")
@RestController
@RequestMapping("/v1/base")
public class NationInfoApi extends BaseApi {
    @Autowired
    private NationInfoService nationInfoService;

    @PostMapping("/nationInfo")
    @ApiOperation(value = "民族信息新增")
    public Object createNationInfo(@RequestBody NationInfoCreate object,
                                   @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        NationInfo model = o2c(object, token, NationInfo.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = nationInfoService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("民族代码已经存在请更换一个代码");


        Boolean nameexist = nationInfoService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");


        model = nationInfoService.createNationInfo(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/nationInfo")
    @ApiOperation(value = "修改民族信息")
    public Object updateNationInfo(@RequestBody NationInfoUpdate object,
                                   @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        NationInfo model = o2c(object, token, NationInfo.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        NationInfo data = nationInfoService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!model.getCode().equals(data.getCode())) {
            Boolean exist = nationInfoService.exist(W.f(
                    W.and("code", "eq", model.getCode()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("代码已经存在请更换一个代码");
        }
        if (!model.getName().equals(data.getName())) {
            Boolean nameexist = nationInfoService.exist(W.f(
                    W.and("name", "eq", model.getName()),
                    W.and("isDel", "eq", "0"))
            );
            if (nameexist)
                return R.error("名称已经存在请更换一个名称");
        }
        model.setReviser(userId);
        model = nationInfoService.updateNationInfo(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/nationInfo/{id}")
    @ApiOperation(value = "民族信息删除")
    public Object deleteNationInfo(@PathVariable("id") String id,
                                   @RequestHeader("token") String token) {

        if (!nationInfoService.existId(id))
            return R.error("Id数据异常");

        if (nationInfoService.deleteNationInfo(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/nationInfo/{index}-{size}-{key}")
    @ApiOperation(value = "读取民族信息分页列表")
    public Object getNationInfo(@PathVariable("index") int index,
                                @PathVariable("size") int size,
                                @PathVariable("key") String key,
                                @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and name like '%", key, "%' ");
        return R.ok(nationInfoService.page(index, size, wKey));

    }

    @PostMapping("/nationInfo/import/{batch}")
    @ApiOperation(value = "导入民族")
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
            return R.error("民族导入异常！");
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


    @PostMapping("/nationInfo/importStatus/{batch}")
    @ApiOperation(value = "导入民族状态")
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
                        String name = "", code = "";

                        if (row.getCell(0) != null && row.getCell(1) != null) {
                            JSONObject importObj = new JSONObject();
                            code = row.getCell(0).toString().trim();//民族代码
                            name = row.getCell(1).toString().trim();//民族名称
                            importObj.put("code", code);
                            importObj.put("name", name);
                            boolean importFlag = false;
                            String userId = getUserIdByCache(token);
                            dealCount++;

                            Boolean nameexist = nationInfoService.exist(W.f(
                                    W.and("name", "eq", name),
                                    W.and("isDel", "eq", "0"))
                            );
                            Boolean codeexist = nationInfoService.exist(W.f(
                                    W.and("code", "eq", code),
                                    W.and("isDel", "eq", "0"))
                            );
                            if (nameexist || codeexist) {
                                importFlag = false;
                                if (nameexist) {
                                    importObj.put("info", "民族名称已存在");
                                }
                                if (codeexist) {
                                    importObj.put("info", "民族代码已存在");
                                }
                            } else {
                                NationInfo nationInfo = new NationInfo();
                                nationInfo.setCode(code);
                                nationInfo.setName(name);
                                nationInfo.setCreator(userId);

                                nationInfo = nationInfoService.createNationInfo(nationInfo);
                                if (nationInfo == null) {
                                    importFlag = false;
                                    importObj.put("info", "民族信息保存失败");
                                } else {
                                    importFlag = true;

                                }


                            }


                            if (!importFlag) {
                                if (V.isEmpty(importObj.getString("info"))) {
                                    importObj.put("info", "民族信息创建失败");
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
            res.put("message", "民族处理出错！");
            return  res;

        }
    }


    @GetMapping("/nationInfo")
    @ApiOperation(value = "读取民族信息所有列表")
    public Object getAllNationInfo(@RequestHeader("token") String token) {

        return R.ok(nationInfoService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入代码");
        verify.put("name", "请输入民族名称");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入代码");
        verify.put("name", "请输入民族名称");
        return verify;
    }


}
