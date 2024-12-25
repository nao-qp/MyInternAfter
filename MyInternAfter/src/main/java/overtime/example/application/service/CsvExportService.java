package overtime.example.application.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.service.ReportService;

@Service
public class CsvExportService {

	@Autowired
    private ReportService reportService;

	@Autowired
    private TimeConverterService timeConverterService;

	//月次資料全件出力
    public void exportMonthlyAllToCsv(OutputStream outputStream) throws IOException {

    	//月次資料データ（全件）取得
        List<Reports> reportList = reportService.getMonthlyListAll();

        // CSV 書き込み準備
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            // ヘッダー行を書き込む
            writer.writeNext(new String[] {"部署",
            								"社員番号",
            								"氏名",
            								"残業実施日",
            								"前残業開始時間",
            								"通常勤務時間〜",
            								"〜通常勤務時間",
            								"後残業終了時間",
            								"残業時間（実働）",
            								"休憩時間",
            								"残業時間+休憩時間",
            								"残業内容",
            								"平日日中60時間未満",
            								"平日日中60時間超",
            								"平日深夜・早朝60時間未満",
            								"平日深夜・早朝60時間超",
            								"休日日中60時間未満",
            								"休日日中60時間超",
            								"休日深夜・早朝60時間未満",
            								"休日深夜・早朝60時間超"
            								});

            // データ行を書き込む
            //残業時間、休憩時間は時間表示
            for (Reports report : reportList) {
                writer.writeNext(new String[] {
                		report.getDepartmentsName(),
                		String.valueOf(report.getAccount()),
                		report.getUsersName(),
                		String.valueOf(report.getOvertimeDate()),
                		String.valueOf(report.getStartTime()),
                		String.valueOf(report.getWorkPatternsStartTime()),
                		String.valueOf(report.getWorkPatternsEndTime()),
                		String.valueOf(report.getEndTime()),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getActualOvertime())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getRestPeriod())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getActualOvertime() + report.getRestPeriod())),
                		report.getReason(),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getWdayDtUnder60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getWdayDtOver60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getWdayEmnUnder60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getWdayEmnOver60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getHdayDtUnder60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getHdayDtOver60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getHdayEmnUnder60())),
                		String.valueOf(timeConverterService.toHoursFromMinutes(report.getHdayEmnOver60()))
                });
            }
        }
    }


    //月次資料データ社員個別出力
    public void exportMonthlyOneToCsv(OutputStream outputStream) throws IOException {

    	//月次資料データ（全件）取得
        List<Reports> repotList = reportService.getMonthlyListAll();

        // CSV 書き込み準備
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            // ヘッダー行を書き込む（必要に応じて）
            writer.writeNext(new String[] {"id", "usersId", "usersName", "departmentsName"});

            // データ行を書き込む
            for (Reports report : repotList) {
                writer.writeNext(new String[] {
                		String.valueOf(report.getId()),
                		String.valueOf(report.getUsersId()),
                		report.getUsersName(),
                		report.getDepartmentsName()
                });
            }
        }
    }
}
