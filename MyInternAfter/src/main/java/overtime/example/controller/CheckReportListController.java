package overtime.example.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class CheckReportListController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	//残業報告確認一覧画面表示
	@GetMapping("check/report/list")
	public String getCheckReportList(Model model, Locale locale) {

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
