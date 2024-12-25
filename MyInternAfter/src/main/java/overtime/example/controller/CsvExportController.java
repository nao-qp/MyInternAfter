package overtime.example.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletResponse;
import overtime.example.application.service.CsvExportService;

@Controller
public class CsvExportController {

	@Autowired
	private CsvExportService csvExportService;

	//残業申請一覧画面表示
	@GetMapping("export/csv/{data}")
	public void exportMonthlyreportAllToCsv (HttpServletResponse response, @PathVariable("data") String data) throws IOException {

		// 出力先のバイトストリームを作成
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // CSV データをエクスポート
        String fileNmae = "";
        switch (data) {
        	case "all":
        		csvExportService.exportMonthlyAllToCsv(byteArrayOutputStream);
        		fileNmae = "MonthlyreportAll.csv";
        		break;
        	case "one" :
        		csvExportService.exportMonthlyOneToCsv(byteArrayOutputStream);
        		fileNmae = "MonthlyreportOne.csv";
        }



        // レスポンスのヘッダー設定
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileNmae + "\"");
        response.setStatus(HttpStatus.OK.value());

        // 出力バイトストリームをレスポンスに書き込む
        response.getOutputStream().write(byteArrayOutputStream.toByteArray());
        response.getOutputStream().flush();
	}
}
