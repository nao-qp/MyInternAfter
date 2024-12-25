package overtime.example.controller;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import overtime.example.application.service.EditForDisplayService;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.RequestService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class ReportDetailController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private EditForDisplayService editForDisplayService;

	//残業報告詳細画面表示
	@GetMapping("report/detail/{id}")
	public String getReportDetail(Model model, Locale locale, @PathVariable("id") Integer id) {

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

        // 残業報告データ取得
        Reports report = reportService.getReport(id);	//reportテーブルのid
        model.addAttribute("report", report);

        ////申請書欄設定
        //承認欄用に申請書情報を取得する
        Requests request = requestService.getRequest(report.getRequestsId());
        if (request != null) {
        	//申請欄を設定
            editForDisplayService.editRequestForm(model, request);
        } else {
        	//申請なしの場合、承認日の枠だけ表示
        	String approvalDate = "　　年　　月　　日";
        	model.addAttribute("approvalDate", approvalDate);
        }
        
        //残業理由の改行を表示する
        String requestReasonWithBr = editForDisplayService.convertNewlinesToBr(report.getRequestsReason());
        model.addAttribute("requestReasonWithBr", requestReasonWithBr);
        

        //報告日を編集
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String reportDate = (report.getReportDate()).format(formatter);
        model.addAttribute("reportDate", reportDate);

        //実残業時間を編集
        //前残業、後残業表示データ作成
        Map<String, String> overtimeBefAftDisplayMap = editForDisplayService.getOvertimeBefAftDisplay(
		        		report.getStartTime(), report.getEndTime(),
		        		report.getWorkPatternsStartTime(), report.getWorkPatternsEndTime());
        String beforeOvertimeDisplayReport = null;
        String afterOvertimeDisplayReport = null;
        if (overtimeBefAftDisplayMap.get("before") != null) {
        	beforeOvertimeDisplayReport = overtimeBefAftDisplayMap.get("before");
        }
        if (overtimeBefAftDisplayMap.get("after") != null) {
        	afterOvertimeDisplayReport = overtimeBefAftDisplayMap.get("after");
        }
        model.addAttribute("beforeOvertimeDisplayReport", beforeOvertimeDisplayReport);
        model.addAttribute("afterOvertimeDisplayReport", afterOvertimeDisplayReport);
        
        String restPeriod = "";
        if (report.getRestPeriod() > 0) {
        	restPeriod = report.getRestPeriod() + "　分";
        }
        
        model.addAttribute("restPeriod", restPeriod);

        //残業報告の改行を表示する
        String reportReasonWithBr = editForDisplayService.convertNewlinesToBr(report.getReason());
        model.addAttribute("reportReasonWithBr", reportReasonWithBr);
   
		return "report/detail";
	}

}
