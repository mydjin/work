package com.qihsoft.webdev.api.admin;

import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.doc.AcademyInfoCreate;
import com.qihsoft.webdev.core.model.AcademyInfo;
import com.qihsoft.webdev.core.service.impl.AcademyInfoService;
import com.qihsoft.webdev.doc.AcademyInfoUpdate;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "203_部门信息管理")
@RestController
@RequestMapping("/v1/base")
public class AcademyInfoApi extends BaseApi {
    @Autowired
    private AcademyInfoService academyInfoService;


    @PostMapping("/academyInfo")
    @ApiOperation(value = "添加部门信息")
    public Object createAcademyInfo(@RequestBody AcademyInfoCreate object
            , @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        AcademyInfo model = o2c(object, token, AcademyInfo.class);

        Boolean exist = academyInfoService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        model = academyInfoService.createAcademyInfo(model);
        if (model == null)
            return R.error("添加失败");

        return R.ok("添加成功", fm2(model));
    }

    @PutMapping("/academyInfo")
    @ApiOperation(value = " 修改部门信息")
    public Object updateAcademyInfo(@RequestBody AcademyInfoUpdate object, @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        AcademyInfo model = o2c(object, token, AcademyInfo.class);

        Boolean exist = academyInfoService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("id", "ne", model.getId()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        model.setReviser(userId);
        model = academyInfoService.updateAcademyInfo(model);
        if (model == null)
            return R.error("修改失败");

        return R.ok("修改成功", fm2(model));


    }

    @DeleteMapping("/academyInfo/{id}")
    @ApiOperation(value = "删除部门信息")
    public Object deleteAcademyInfo(@PathVariable("id") String id, @RequestHeader("token") String token) {
        if (!academyInfoService.existId(id))
            return R.error("Id数据异常");

        if (academyInfoService.delete(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");

    }

    @GetMapping("/academyInfo/{index}-{size}-{name}-{code}-{campus}")
    @ApiOperation(value = "读取部门信息分页列表")
    public Object getAcademyInfo(@PathVariable("index") int index,
                                 @PathVariable("size") int size,
                                 @PathVariable("name") String name,
                                 @PathVariable("code") String code,
                                 @PathVariable("campus") String campus,
                                 @RequestHeader("token") String token) {

//前端的搜索条件（部门代码，名称，校区）
        String wKey = "";
        if (!V.isEmpty(name))
            wKey = S.apppend(" and (name like '%", name, "%')");
        if (!V.isEmpty(code))
            wKey += S.apppend(" and (code = '", code, "')");
        if (!V.isEmpty(campus))
            wKey += S.apppend(" and (campus = '", campus, "')");
        return R.ok(academyInfoService.page(index, size, wKey));
    }


}
