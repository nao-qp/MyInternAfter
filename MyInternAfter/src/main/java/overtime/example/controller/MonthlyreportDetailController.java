package overtime.example.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import overtime.example.application.service.TimeConverterService;
import overtime.example.domain.user.model.ReportsSumDedail;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class MonthlyreportDetailController {

	@Autowired
	private UserService userService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private TimeConverterService timeConverterService;


	//月次資料詳細画面表示
	@GetMapping("monthlyreport/detail/{id}")
	public String getMonthlyreportList(Model model, Locale locale, @PathVariable("id") Integer id) {
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        // ユーザー情報を取得
        Users user = userService.getUser(currentUserId);
        model.addAttribute("user", user);

        //月次資料詳細データ取得
        List<ReportsSumDedail> reportsSumDedailList = reportService.getMonthlyUser(id);
        if (reportsSumDedailList == null || reportsSumDedailList.isEmpty()) {
        	// nullだったら空のリストを作成
        	reportsSumDedailList = new ArrayList<>();
        }
        // 表示表に編集
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
        for (ReportsSumDedail reportsSumDedail: reportsSumDedailList) {
        	// 時間から分表示へ
        	reportsSumDedail.setAllSum(timeConverterService.toHoursFromMinutes(reportsSumDedail.getAllSum()));
        	reportsSumDedail.setWdayDtUnder60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getWdayDtUnder60()));
        	reportsSumDedail.setWdayDtOver60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getWdayDtOver60()));
        	reportsSumDedail.setWdayEmnUnder60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getWdayEmnUnder60()));
        	reportsSumDedail.setWdayEmnOver60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getWdayEmnOver60()));
        	reportsSumDedail.setHdayDtUnder60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getHdayDtUnder60()));
        	reportsSumDedail.setHdayDtOver60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getHdayDtOver60()));
        	reportsSumDedail.setHdayEmnUnder60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getHdayEmnUnder60()));
        	reportsSumDedail.setHdayEmnOver60(timeConverterService.toHoursFromMinutes(reportsSumDedail.getHdayEmnOver60()));
        	// 日付の表記をMM月ddにフォーマット
        	reportsSumDedail.setFormatOvertimeDate(reportsSumDedail.getOvertimeDate().format(formatter));
        }
        model.addAttribute("reportsSumDedailList", reportsSumDedailList);
        
        // タイトル表示用にユーザー名を設定
        // 一件目セット
        ReportsSumDedail firstReportsSumDedailList = reportsSumDedailList.isEmpty() ? null : reportsSumDedailList.get(0);
        model.addAttribute("userName", firstReportsSumDedailList.getUsersName());
        
        return "monthlyreport/detail";
	}
	
}
