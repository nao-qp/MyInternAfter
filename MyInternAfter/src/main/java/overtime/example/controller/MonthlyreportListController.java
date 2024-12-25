package overtime.example.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import overtime.example.application.service.TimeConverterService;
import overtime.example.domain.user.model.ReportsSum;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.MonthlySubmitDataService;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.impl.CustomUserDetails;
import overtime.example.form.MonthlyreportListForm;

@Controller
public class MonthlyreportListController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private MonthlySubmitDataService monthlySubmitDataService;
	
	@Autowired
	private TimeConverterService timeConverterService;
	
	//月次資料画面表示
	@GetMapping("monthlyreport/list")
	public String getMonthlyreportList(Model model, Locale locale, @ModelAttribute MonthlyreportListForm form) {

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

        //残業時間データ取得
        List<ReportsSum> reportsSumList = reportService.getMonthlySum();
        for (ReportsSum reportsSum: reportsSumList) {
        	timeConverterService.toHoursFromMinutes(reportsSum.getAllSum());
        	timeConverterService.toHoursFromMinutes(reportsSum.getWdayDtUnder60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getWdayDtOver60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getWdayEmnUnder60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getWdayEmnOver60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getHdayDtUnder60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getHdayDtOver60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getHdayEmnUnder60());
        	timeConverterService.toHoursFromMinutes(reportsSum.getHdayEmnOver60());
        }

        model.addAttribute("reportsSumList", reportsSumList);

        //提出済み判定
        String yearmonth = "202412";	//プロトタイプでは年月を固定
        Integer submitted = monthlySubmitDataService.getMonthlySubmitted(yearmonth);
        model.addAttribute("submitted", submitted);
        
		return "monthlyreport/list";
	}
	
	@PostMapping("monthlyreport/submitdata")
	public String PostMonthlyreportSubmit(Locale locale) {
		
		//提出ボタンが押されたら
		//すでに提出済みであるか確認
        String yearmonth = "202412";	//プロトタイプでは年月を固定
        Integer submitted = monthlySubmitDataService.getMonthlySubmitted(yearmonth);
		
		if (submitted == null || submitted.equals(0)) {
			//提出済みデータを作成
			monthlySubmitDataService.monthlySubmit(yearmonth);
		}
		
		return "redirect:/monthlyreport/list";
		
	}

}
