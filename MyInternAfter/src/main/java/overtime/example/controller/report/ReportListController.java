package overtime.example.controller.report;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import overtime.example.aop.annotation.Authenticated;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class ReportListController {

	@Autowired
	private ReportService reportService;

	//残業申請一覧画面表示
	@GetMapping("report/list")
	@Authenticated
	public String getReportList(Model model, Locale locale, @AuthenticationPrincipal CustomUserDetails user) {

        model.addAttribute("user", user);

        //報告データ一覧取得
        List<Reports> reportList = reportService.getReportList(user.getId());
        model.addAttribute("reportList", reportList);


        return "report/list";
	}
}
