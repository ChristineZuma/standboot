package cn.itcast.controller.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.*;

/**
 * @description: poi导出Excel的demo
 * @author: lisong
 * @since: 2023-02-03 13:23
 **/
@RestController
@RequestMapping
public class exportController{

    public void exportData(HttpServletResponse resp, List<Map<String,Object>> list){
        XSSFWorkbook wookbook = new XSSFWorkbook();
        XSSFSheet sheet = wookbook.createSheet("补贴管理");
        //表头
        XSSFRow headRow = sheet.createRow(0);
        //取list的第一个元素进行遍历,找出所有的key做表头
        Iterator<Map.Entry<String, Object>> iterator = list.get(0).entrySet().iterator();
        int i=0;
        //用于数据行查找
        List<Object> headRowData = new ArrayList<>();
        //单元格样式-用于数据变红
        CellStyle cellStyle = wookbook.createCellStyle();
        XSSFFont redFont = wookbook.createFont();
        redFont.setColor(Font.COLOR_RED);
        cellStyle.setFont(redFont);
        while(iterator.hasNext()){
            String next = iterator.next().getKey();
            //设置表头
            headRow.createCell(i).setCellValue(new XSSFRichTextString(next));
            headRowData.add(next);
            i++;
        }
        //列表
        int rowNum=1;
        for (Map<String, Object> dataMap : list) {
            //创建数据行
            XSSFRow dataRow = sheet.createRow(rowNum);
            for(int j=0,size = headRowData.size();j<size;j++){
                //设置数据
                XSSFCell cell = dataRow.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(dataMap.get(headRowData.get(j)).toString());
            }
            rowNum++;
        }
        //数据导出
        //resp.setContentType("application/octet-stream;charset=UTF-8");
        resp.setContentType("application/msexcel");
        try {
            //resp.setHeader("Content-Disposition","attachment="+ URLEncoder.encode("管理信息.xlsx","UTF-8"));
            resp.setHeader("Content-disposition", "attachment; filename="+toUtf8String("demoTest.xlsx"));
            wookbook.write(resp.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/export")
    public void exportData(HttpServletResponse response){
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("aaa",UUID.randomUUID().toString().replace("-","").substring(0,5));
            map.put("bbb",UUID.randomUUID().toString().replace("-","").substring(0,5));
            map.put("ccc",UUID.randomUUID().toString().replace("-","").substring(0,5));
            map.put("ddd",UUID.randomUUID().toString().replace("-","").substring(0,5));
            list.add(map);
        }
        exportData(response,list);
    }

    public static String toUtf8String(String s){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c >= 0 && c <= 255){sb.append(c);}
            else{
                byte[] b;
                try { b = Character.toString(c).getBytes(StandardCharsets.UTF_8);}
                catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
