package overtime.example.controller.check;

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
public class CheckReportListController {

	@Autowired
	private ReportService reportService;

	//残業報告確認一覧画面表示
	@GetMapping("check/report/list")
	@Authenticated
	public String getCheckReportList(Model model, Locale locale,
			@AuthenticationPrincipal CustomUserDetails user) {
		
        model.addAttribute("user", user);

        //次長権限ではない場合、ログインページにリダイレクトする
        if (!user.getRole().equals(1)) {
        	return "redirect:/user/login";
        }

        // 残業報告データ一覧を取得
        List<Reports> reportList = reportService.getCheckDataList();
        model.addAttribute("reportList", reportList);


		return "check/report/list";
	}
}
