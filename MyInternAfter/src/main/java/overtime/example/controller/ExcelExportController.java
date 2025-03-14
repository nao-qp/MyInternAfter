package overtime.example.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.Requests;

@RestController
@RequestMapping("/excel")
public class ExcelExportController {
	@GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(HttpSession session) throws IOException {
        // テンプレートExcelファイルのパス
    	ClassPathResource resource = new ClassPathResource("excel/template.xlsx");
    	InputStream in = resource.getInputStream();
    
        // 既存のテンプレートExcelファイルを読み込む
        try (XSSFWorkbook workbook = new XSSFWorkbook(in)) {
	        // シートを取得
	        XSSFSheet sheet = workbook.getSheetAt(0);  // 1番目のシートを取得（シート番号は0から始まる）
        
        // 表示データを取得
        Model model = (Model)session.getAttribute("model");
        session.removeAttribute("model");
        
       
        // Excelの表示項目をセット
        // RequestControllerでmodelの項目を表示用に編集済み
        // 編集対象ではない項目はmodelに格納しているrequestのデータを使用する
        // 申請項目
        Requests request = (Requests)model.getAttribute("request");
        String requestDate = "";
        String departmentsName = "";
        String usersName = "";
        String workPatternsDisplay = "";
        List<String> overtimeDisplayList = new ArrayList<>();
        String reasonWithBr = "";
        String approvalDate = "";
        String approvalUsersName = "";
        if (request != null ) {
        	requestDate = (String)model.getAttribute("requestDate");
        	if (requestDate == null) {
        		requestDate = "";
        	}
            departmentsName = request.getDepartmentsName();
            usersName = request.getUsersName();
            
            workPatternsDisplay = (String)model.getAttribute("workPatternsDisplay");
            // 残業予定時間 表示行可変表示表リスト作成
            String beforeOvertimeDisplay = (String)model.getAttribute("beforeOvertimeDisplay");
            String afterOvertimeDisplay = (String)model.getAttribute("afterOvertimeDisplay");
            if (beforeOvertimeDisplay != null) {
            	overtimeDisplayList.add(beforeOvertimeDisplay);
            }
            if (afterOvertimeDisplay != null) {
            	overtimeDisplayList.add(afterOvertimeDisplay);
            }
            reasonWithBr = (String)model.getAttribute("reasonWithBr");
            approvalDate = (String)model.getAttribute("approvalDate");
            approvalUsersName = (String)model.getAttribute("approvalUsersName");
        }
        
        // 報告項目
        Reports report = (Reports)model.getAttribute("report");
        List<String> overtimeDisplayReportList = new ArrayList<>();
        String restPeriod = "";
        String reportReasonWithBr = "";
        String reportDate = "";
        String reportUsersName = "";
        if (report != null) {
        	// 申請情報がなくても表示する共通項目を報告データから設定
        	if (departmentsName != null && departmentsName.trim().isEmpty()) {
        		departmentsName = report.getDepartmentsName();
        	}
        	if (usersName != null && usersName.trim().isEmpty()) {
        		usersName = report.getUsersName();
        	}
        	if (workPatternsDisplay != null && workPatternsDisplay.trim().isEmpty()) {
        		workPatternsDisplay = (String)model.getAttribute("workPatternsDisplay");
        	}

            // 実残業時間 可変表示リスト作成
        	String beforeOvertimeDisplayReport = (String)model.getAttribute("beforeOvertimeDisplayReport");
        	String afterOvertimeDisplayReport = (String)model.getAttribute("afterOvertimeDisplayReport");
            if (beforeOvertimeDisplayReport != null) {
            	overtimeDisplayReportList.add(beforeOvertimeDisplayReport);
            }
            if (afterOvertimeDisplayReport != null) {
            	overtimeDisplayReportList.add(afterOvertimeDisplayReport);
            }
     
            restPeriod = (String)model.getAttribute("restPeriod");
            reportReasonWithBr = (String)model.getAttribute("reportReasonWithBr");
            reportDate = (String)model.getAttribute("reportDate");
            reportUsersName = report.getUsersName();
        }

        // テンプレートのセルを処理し、プレースホルダーを変数の値で置き換える
        for (Row row : sheet) {
            // 行が空かどうかを判定するフラグ
            boolean isRowEmpty = true;
            
            // 行のすべてのセルをチェック
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    isRowEmpty = false; // 空でないセルが見つかればフラグをfalseに
                    break;  // 空でないセルが見つかればチェック終了
                }
            }

            // 空の行はスキップ
            if (isRowEmpty) {
                continue;
            }

            // 行にデータがあれば処理を続ける
            for (Cell cell : row) {
            	// 空のセルはスキップ
            	if (cell.getCellType() != CellType.BLANK) {
            		if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();

                        // プレースホルダーを変数の値に置き換え
                        // 申請部分
                        if (requestDate != null && cellValue.contains("{{requestDate}}")) {
                            cell.setCellValue(cellValue.replace("{{requestDate}}", requestDate));
                        }
                        if (departmentsName != null && cellValue.contains("{{departmentsName}}")) {
                            cell.setCellValue(cellValue.replace("{{departmentsName}}", departmentsName));
                        }
                        if (usersName != null && cellValue.contains("{{usersName}}")) {
                            cell.setCellValue(cellValue.replace("{{usersName}}", usersName));
                        }
                        if (workPatternsDisplay != null && cellValue.contains("{{workPatternsDisplay}}")) {
                            cell.setCellValue(cellValue.replace("{{workPatternsDisplay}}", workPatternsDisplay));
                        }
                        // 残業予定時間が2つある場合に対応
                        if (cellValue.contains("{{overtimeDisplay1}}") || cellValue.contains("{{overtimeDisplay2}}")) {
                        	List<String> filteredList = overtimeDisplayList.stream()
                        		    .filter(data -> data != null && !data.trim().isEmpty()) // 空白ではないデータ
                        		    .collect(Collectors.toList()); 
                        	String newCell = cellValue;
                        	if (!filteredList.isEmpty()) {
                        		int overtimeDisplayCount = 0;
                        		for (String overtimeDisplay: filteredList) {
                        			if (overtimeDisplayCount == 0) {
                        				newCell = newCell.replace("{{overtimeDisplay1}}", overtimeDisplay);
                        				overtimeDisplayCount++;
                            		} else {
                            			// 残業予定時間2つ目の表示位置に設定
                            			newCell = newCell.replace("{{overtimeDisplay2}}", overtimeDisplay);
                            			break;	// 最大2つなのでループ終了
                            		}
                        		}
                        	}
                        	// 残業予定時間の数によって空白を設定
                    		if (filteredList.size() == 0) {
                    			newCell = newCell.replace("{{overtimeDisplay1}}", "");
                    			newCell = newCell.replace("{{overtimeDisplay2}}", "");
                    		} else if (filteredList.size() == 1) {
                    			newCell = newCell.replace("{{overtimeDisplay2}}", "");
                    		}
                    		// セルに設定
                    		cell.setCellValue(newCell);
                        }
                                              
                        if (reasonWithBr != null && cellValue.contains("{{reasonWithBr}}")) {
                        	// 改行設定
                        	reasonWithBr = reasonWithBr.replace("<br>","\n");
                        	setCellStyleLineBreak(cell);
                            cell.setCellValue(cellValue.replace("{{reasonWithBr}}", reasonWithBr));
                        }                     
                        if (approvalDate != null && cellValue.contains("{{approvalDate}}")) {
                            cell.setCellValue(cellValue.replace("{{approvalDate}}", approvalDate));
                        }
                        if (approvalUsersName != null && cellValue.contains("{{approvalUsersName}}")) {
                            cell.setCellValue(cellValue.replace("{{approvalUsersName}}", approvalUsersName));
                        }
 
                        // 報告部分
                        // 実残業時間が2つある場合に対応
                        if (cellValue.contains("{{overtimeDisplayReport1}}") || cellValue.contains("{{overtimeDisplayReport2}}")) {
                        	List<String> filteredList = overtimeDisplayReportList.stream()
                        		    .filter(data -> data != null && !data.trim().isEmpty()) // 空白ではないデータ
                        		    .collect(Collectors.toList()); 
                        	String newCell = cellValue;
                        	if (!filteredList.isEmpty()) {
                        		int overtimeDisplayCount = 0;
                        		for (String overtimeDisplayReport: filteredList) {
                        			if (overtimeDisplayCount == 0) {
                        				newCell = newCell.replace("{{overtimeDisplayReport1}}", overtimeDisplayReport);
                        				overtimeDisplayCount++;
                            		} else {
                            			// 実残業時間2つ目の表示位置に設定
                            			newCell = newCell.replace("{{overtimeDisplayReport2}}", overtimeDisplayReport);
                            			break;	// 最大2つなのでループ終了
                            		}
                        		}
                        	}
                        	// 実残業時間の数によって空白を設定
                    		if (filteredList.size() == 0) {
                    			newCell = newCell.replace("{{overtimeDisplayReport1}}", "");
                    			newCell = newCell.replace("{{overtimeDisplayReport2}}", "");
                    		} else if (filteredList.size() == 1) {
                    			newCell = newCell.replace("{{overtimeDisplayReport2}}", "");
                    		}
                    		// セルに設定
                    		cell.setCellValue(newCell);
                        }
                        
                        if (restPeriod != null && cellValue.contains("{{restPeriod}}")) {
                            cell.setCellValue(cellValue.replace("{{restPeriod}}", restPeriod));
                        }
                        if (reportReasonWithBr != null && cellValue.contains("{{reportReasonWithBr}}")) {
                        	// 改行設定
                        	reportReasonWithBr = reportReasonWithBr.replace("<br>","\n");
                        	setCellStyleLineBreak(cell);
// NOTE: cell.setCellValue()によりセルの結合が外れてしまう場合に使用                         
                            //// セルの結合を再設定
//                            int rowIndex = cell.getRowIndex();
//                            int colIndex = cell.getColumnIndex();
//                            // 結合範囲を取得
//                            MergedRegionCell mergedRegionCell = getMergedRegionForCell(sheet, rowIndex, colIndex);
                            
                            // 値を設定
                            cell.setCellValue(cellValue.replace("{{reportReasonWithBr}}", reportReasonWithBr));
                            
// NOTE: cell.setCellValue()によりセルの結合が外れてしまう場合に使用
                            // 結合を再設定
//                            sheet.addMergedRegion(new CellRangeAddress(
//                            		mergedRegionCell.firstRow, mergedRegionCell.lastRow, 
//                            		mergedRegionCell.firstColumn, mergedRegionCell.lastColumn));

                        }
                        if (reportDate != null && cellValue.contains("{{reportDate}}")) {
                            cell.setCellValue(cellValue.replace("{{reportDate}}", reportDate));
                        }
                        if (reportUsersName != null && cellValue.contains("{{reportUsersName}}")) {
                            cell.setCellValue(cellValue.replace("{{reportUsersName}}", reportUsersName));
                        }
                    }
            	}
            }
        }

        // Excelをバイト配列として書き出し
        try (FileOutputStream fos = new FileOutputStream("output.xlsx")) {
        	 workbook.write(fos);
        }
        }
        // Excelをバイト配列として取得
        byte[] excelBytes;
        try (FileInputStream outputFis = new FileInputStream("output.xlsx")) {
            excelBytes = outputFis.readAllBytes();
        }

        // レスポンスのヘッダーを設定してExcelをレスポンスとして返す
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"report.xlsx\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
    
    /**
     * セルのスタイルの折り返しを有効にする
     * @param cell
     * @param workbook
     */
    public void setCellStyleLineBreak(Cell cell) {
    	// セルのスタイルを設定（折り返し設定）
    	CellStyle cellStyle = cell.getCellStyle();
    	cellStyle.setWrapText(true);  // 折り返しを有効にする
    	cell.setCellStyle(cellStyle);
    }
    
    
// NOTE: cell.setCellValue()によりセルの結合が外れてしまう場合に使用
//    // 結合セル範囲取得用オブジェクト
//    record MergedRegionCell(int firstRow, int lastRow, int firstColumn, int lastColumn){};
//
//	 // プレースホルダーが設定されたセルを基準に結合セル範囲を取得
//    public static MergedRegionCell getMergedRegionForCell(XSSFSheet sheet, int rowIndex, int colIndex) {
//            // 結合セルの範囲を全て取得
//            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
//                // 結合セルの範囲を取得
//                CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
//                
//                // プレースホルダーが設定されたセルがこの結合セル範囲内にあるかチェック
//                if (mergedRegion.isInRange(rowIndex, colIndex)) {
//                    // 結合セルの範囲が見つかった場合、範囲を取得
//                	MergedRegionCell mergedRegionCell = new MergedRegionCell(
//                			mergedRegion.getFirstRow(), mergedRegion.getLastRow(),
//                			mergedRegion.getFirstColumn(), mergedRegion.getLastColumn());
//                    return mergedRegionCell;
//                }
//            }
//            return null;
//    }

}
