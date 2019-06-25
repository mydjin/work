package com.xtaller.party.api.admin;


import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.SysUserRole;
import com.xtaller.party.core.service.impl.SysUserRoleService;
import com.xtaller.party.doc.SysRoleAdd;
import com.xtaller.party.doc.SysRoleAuthConfig;
import com.xtaller.party.doc.SysRoleUpdate;
import com.xtaller.party.core.model.SysRole;
import com.xtaller.party.core.service.impl.SysRoleService;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Taller on 2017/9/26.
 */
@Api(value = "SYS4_角色统一管理")
@RestController
@RequestMapping("/v1/sys")
public class SysRoleApi extends BaseApi {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserRoleService userRoleService;

    /********************* 角色 **************************/
    @GetMapping("/role")
    @ApiOperation(value = "读取所有角色")
    public Object getRole() {
        return R.ok(roleService.query(W.f(W.and("1", "eq", "1"))));
    }

    @PostMapping("/role")
    @ApiOperation(value = "创建角色")
    public Object createRole(@RequestBody SysRoleAdd object, @RequestHeader("token") String token) {

        SysRole model = o2c(object, token, SysRole.class);
        if (V.isEmpty(model.getName()))
            return R.error("请输入角色名称");

        Boolean exist = roleService.exist(W.f(
                W.and("name", "eq", model.getName())
        ));
        if (exist)
            return R.error("角色名称已经存在请更换一个名称");

        model = roleService.createRole(model);
        if (model == null) {
            return R.error("创建失败");
        }
        return R.ok("创建成功", fm2(model));
    }

    @PutMapping("/role")
    @ApiOperation(value = "修改角色")
    public Object updateRole(@RequestBody SysRoleUpdate object, @RequestHeader("token") String token) {

        SysRole model = o2u(object, token, SysRole.class);
        if (!roleService.existId(model.getId()))
            return R.error("Id数据异常");
        if (V.isEmpty(model.getName()))
            return R.error("请输入角色名称");

        Boolean exist = roleService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("id", "ne", model.getId())
        ));
        if (exist)
            return R.error("权限模块名称已经存在请更换一个名称");

        model = roleService.updateRole(model);
        if (model == null) {
            return R.error("修改失败");
        }
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "删除角色")
    public Object deleteRole(@PathVariable("id") String id, @RequestHeader("token") String token) {

        if (!roleService.existId(id))
            return R.error("Id数据异常");
        if (roleService.delete(id, cacheKit.getUserId(token))) {
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

    @GetMapping("/role/{id}")
    @ApiOperation(value = "读取角色权限")
    public Object getRoleAuthById(@PathVariable("id") String id,
                                  @RequestHeader("token") String token) {

        if (!roleService.existId(id))
            return R.error("角色信息异常");
        return R.ok(roleService.getAuthTreeByRoleId(id));

    }


    @GetMapping("/role/user/{userId}")
    @ApiOperation(value = "通过userId读取角色权限Id")
    public Object getRoleByUserId(@PathVariable("userId") String userId,
                                  @RequestHeader("token") String token) {

        SysUserRole userRole = userRoleService.findByUserId(userId);
        if (userRole == null)
            return R.error("角色信息异常");
        return R.ok(userRole);

    }

    @PostMapping("/role-auth")
    @ApiOperation(value = "角色权限配置")
    public Object configAuth(@RequestBody SysRoleAuthConfig object) {
        String roleId = object.getRoleId();
        List<JSONObject> menu = object.getMenu();
        List<JSONObject> btns = object.getBtns();
        if (roleService.configRole(roleId, menu, btns))
            return R.ok("权限分配成功");
        return R.error("角色信息异常");
    }

    @GetMapping("/role/getAll/{name}")
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
}
