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
public class ReportListController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	//残業申請一覧画面表示
	@GetMapping("report/list")
	public String getReportList(Model model, Locale locale) {

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

        //報告データ一覧取得
        List<Reports> reportList = reportService.getReportList(user.getId());
        model.addAttribute("reportList", reportList);


        return "report/list";
	}
}
