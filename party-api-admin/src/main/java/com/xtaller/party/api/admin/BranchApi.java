package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Branch;
import com.xtaller.party.core.model.NationInfo;
import com.xtaller.party.core.service.impl.AcademyInfoService;
import com.xtaller.party.core.service.impl.BranchService;
import com.xtaller.party.doc.BranchCreate;
import com.xtaller.party.doc.BranchUpdate;
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
 * Created by party on 2018/11/30
 */
@Api(value = "03_党支部管理")
@RestController
@RequestMapping("/v1/base")
public class BranchApi extends BaseApi {
    @Autowired
    private BranchService branchService;

    @Autowired
    private AcademyInfoService academyInfoService;

    @PostMapping("/branch")
    @ApiOperation(value = "党支部新增")
    public Object createBranch(@RequestBody BranchCreate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Branch model = o2c(object, token, Branch.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        Boolean exist = branchService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("支部代码已经存在请更换一个代码");


        Boolean nameexist = branchService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");


        model = branchService.createBranch(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/branch")
    @ApiOperation(value = "修改党支部")
    public Object updateBranch(@RequestBody BranchUpdate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Branch model = o2c(object, token, Branch.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Branch data = branchService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!model.getCode().equals(data.getCode())) {
            Boolean exist = branchService.exist(W.f(
                    W.and("code", "eq", model.getCode()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("代码已经存在请更换一个代码");
        }
        if (!model.getName().equals(data.getName())) {
            Boolean nameexist = branchService.exist(W.f(
                    W.and("name", "eq", model.getName()),
                    W.and("isDel", "eq", "0"))
            );
            if (nameexist)
                return R.error("名称已经存在请更换一个名称");
        }

        Boolean exist = branchService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("id", "ne", model.getId()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        model.setReviser(userId);
        model = branchService.updateBranch(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/branch/{id}")
    @ApiOperation(value = "党支部删除")
    public Object deleteBranch(@PathVariable("id") String id,
                               @RequestHeader("token") String token) {

        if (!branchService.existId(id))
            return R.error("Id数据异常");

        if (branchService.deleteBranch(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/branch/{index}-{size}-{key}")
    @ApiOperation(value = "读取党支部分页列表")
    public Object getBranch(@PathVariable("index") int index,
                            @PathVariable("size") int size,
                            @PathVariable("key") String key,
                            @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and name like '%", key, "%' ");
        return R.ok(branchService.page(index, size, wKey));

    }

    @GetMapping("/branch")
    @ApiOperation(value = "读取党支部所有列表")
    public Object getAllBranch(@RequestHeader("token") String token) {

        return R.ok(branchService.queryAll(""));

    }

    //通过学院代码找该学院所有专业
    @GetMapping("/branch/academyCode/{academyCode}")
    @ApiOperation(value = "通过学院代码找该学院所有支部")
    public Object getBranchByCode(@PathVariable("academyCode") String academyCode,
                                  @RequestHeader("token") String token) {

        return R.ok(branchService.getBranchByAcademyCode(academyCode));

    }


    @PostMapping("/branch/import/{batch}")
    @ApiOperation(value = "导入党支部")
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
            return R.error("党支部导入异常！");
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


    @PostMapping("/branch/importStatus/{batch}")
    @ApiOperation(value = "导入党支部状态")
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
                            dealCount++;
                            JSONObject importObj = new JSONObject();
                            code = row.getCell(0).toString().trim();//党支部代码
                            name = row.getCell(1).toString().trim();//党支部名称
                            academyName = row.getCell(2).toString().trim();//学院名称
                            importObj.put("code", code);
                            importObj.put("name", name);
                            importObj.put("academyName", academyName);

                            boolean importFlag = false;
                            String userId = getUserIdByCache(token);

                            Boolean nameexist = branchService.exist(W.f(
                                    W.and("name", "eq", name),
                                    W.and("isDel", "eq", "0"))
                            );
                            Boolean codeexist = branchService.exist(W.f(
                                    W.and("code", "eq", code),
                                    W.and("isDel", "eq", "0"))
                            );
                            if (nameexist || codeexist) {
                                importFlag = false;
                                if (nameexist) {
                                    importObj.put("info", "党支部名称已存在");
                                }
                                if (codeexist) {
                                    importObj.put("info", "党支部代码已存在");
                                }
                            } else {
                                JSONObject academyObj = academyInfoService.queryByName(academyName);

                                if (V.isEmpty(academyObj)) {
                                    importFlag = false;
                                    importObj.put("info", "学院名称不存在");
                                } else {

                                    Branch info = new Branch();
                                    info.setCode(code);
                                    info.setName(name);
                                    info.setCreator(userId);
                                    info.setAcademyCode(academyObj.getString("code"));

                                    info = branchService.createBranch(info);
                                    if (info == null) {
                                        importFlag = false;
                                        importObj.put("info", "党支部信息保存失败");
                                    } else {
                                        importFlag = true;

                                    }
                                }


                            }


                            if (!importFlag) {
                                if (V.isEmpty(importObj.getString("info"))) {
                                    importObj.put("info", "党支部信息创建失败");
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
            res.put("message", "党支部处理出错！");
            return  res;
        }
    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("academyCode", "请选择所属学院");
        verify.put("code", "请输入支部代码");
        verify.put("name", "请输入支部名称");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("academyCode", "请选择所属学院");
        verify.put("code", "请输入支部代码");
        verify.put("name", "请输入支部名称");
        return verify;
    }

}
