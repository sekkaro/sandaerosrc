package com.sangdaero.walab.activity;

import com.sangdaero.walab.activity.dto.ActivityDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Component("activityExcel")
public class ActivityXlsView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"sangdaero\"" + LocalDate.now() + "\".xls\"");

        CellStyle numberCellStyle = workbook.createCellStyle();
        DataFormat numberDataFormat = workbook.createDataFormat();
        numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));

        Sheet sheet = workbook.createSheet("activity");

        @SuppressWarnings("unchecked")
        List<ActivityDto> list = (List<ActivityDto>) map.get("activities");

        Row head = sheet.createRow(0);
        Cell no = head.createCell(0);
        no.setCellValue("No.");
        Cell category = head.createCell(1);
        category.setCellValue("카테고리");
        Cell title = head.createCell(2);
        title.setCellValue("제목");
        Cell status = head.createCell(3);
        status.setCellValue("상태");
        Cell memo = head.createCell(4);
        memo.setCellValue("메모");
        Cell registerDate = head.createCell(5);
        registerDate.setCellValue("등록 날짜");
        Cell modDate = head.createCell(6);
        modDate.setCellValue("수정 날짜");

        for(int i=1;i<=list.size();i++) {
            Row row = sheet.createRow(i);

            Cell c_no = row.createCell(0);
            c_no.setCellValue(i);

            Cell c_category = row.createCell(1);
            c_category.setCellValue(list.get(i-1).getInterestCategory().getName());

            Cell c_title = row.createCell(2);
            c_title.setCellValue(list.get(i-1).getTitle());

            Cell c_status = row.createCell(3);
            String sts="";
            Byte stsNum = list.get(i-1).getStatus();

            if(stsNum==1) sts="매칭 전";
            else if(stsNum==2) sts="매칭 중";
            else if(stsNum==3) sts="매칭 완료";
            else if(stsNum==4) sts="활동 진행 중";
            else if(stsNum==5) sts="활동 완료";
            else if(stsNum==6) sts="취소된 활동";

            c_status.setCellValue(sts);

            Cell c_memo = row.createCell(4);
            c_memo.setCellValue(list.get(i-1).getContent());

            Cell c_registerDate = row.createCell(5);
            c_registerDate.setCellValue(list.get(i-1).getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            Cell c_modDate = row.createCell(6);
            c_modDate.setCellValue(list.get(i-1).getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}
