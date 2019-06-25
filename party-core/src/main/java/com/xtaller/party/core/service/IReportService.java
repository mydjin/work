package com.xtaller.party.core.service;

import java.util.List;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.core.model.Report;

/**
* Created by Taller on 2018/08/10
*/
public interface IReportService {
   /******************* CURD ********************/ 
   // 创建 
   Report createReport(Report model); 
   // 删除 
   Boolean deleteReport(Object ids,String reviser);
   // 修改 
   Report updateReport(Report model); 
   // 查询 
   List<Report> findByIds(Object ids);
   // 属于 
   Boolean exist(List<Where> w); 
   // 查询一个id是否存在 
   Boolean existId(Object id); 
   /******************* CURD ********************/ 
 
}