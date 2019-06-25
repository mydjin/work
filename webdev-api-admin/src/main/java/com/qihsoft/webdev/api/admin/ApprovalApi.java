package com.qihsoft.webdev.api.admin;

import com.qihsoft.webdev.api.BaseApi;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qihsoft on 2018/08/14
 */
@Api(value = "212_待办事项/审核管理")
@RestController
@RequestMapping("/v1/base")
public class ApprovalApi extends BaseApi {
   /* @Autowired
    private ApprovalService approvalService;

    @GetMapping("/approval/{index}-{size}-{key}")
    @ApiOperation(value = "读取审核信息分页列表")
    public Object getApproval(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("key") String key,
                              @RequestHeader("token") String token) {
             String wKey = "";
            if (!V.isEmpty(key))
                //FIXME 修改id为需要查询的字段
                wKey = S.apppend(" and id like '%", key, "%' ");
            return R.ok(approvalService.page(index, size, wKey));

    }

    @GetMapping("/approval/approvalType/{index}-{size}-{approvalType}")
    @ApiOperation(value = "通过类型读取审核信息分页列表")
    public Object getApprovalByType(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("approvalType") int approvalType,
                              @RequestHeader("token") String token) {
                String wKey = "";
            if (!V.isEmpty(approvalType))

                wKey = S.apppend(" and approvalType =", approvalType, " ");
            return R.ok(approvalService.page(index, size, wKey));

    }


    @GetMapping("/approval/todo/{index}-{size}-{approvalType}/")
    @ApiOperation(value = "通过类型读取待办事项分页列表")
    public Object getToDoApproval(@PathVariable("index") int index,
                                    @PathVariable("size") int size,
                                    @PathVariable("approvalType") String approvalType,
                                    @RequestHeader("token") String token) {
            String wKey = "";
            if (!V.isEmpty(approvalType)) {
                wKey = S.apppend(" approvalType =", approvalType, " ");
            } else {
                wKey = S.apppend("  ");
            }
            return R.ok(approvalService.todoPage(index, size, wKey));

    }*/

}
