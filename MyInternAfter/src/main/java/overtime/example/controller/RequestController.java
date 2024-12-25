package overtime.example.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import overtime.example.application.service.EditForDisplayService;
import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.RequestService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class RequestController {

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;
	
	@Autowired
	private EditForDisplayService editForDisplayService;


	//残業申請一覧画面表示
	@GetMapping("request/list")
	public String getRequestList(Model model, Locale locale,
			@RequestParam(value = "fromMenu", required = false) String fromMenu ) {
	      
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

        //権限によって初期ページにリダイレクトする
        switch (user.getRole().intValue()) {
        	case 1:
        		if (fromMenu == null) {
        			//ログイン後、初期表示の場合
        			//次長（申請確認画面へ）
            		return "redirect:/check/request/list";
        		}
        		break;
        	case 2:
        		//課長（申請承認画面へ）
        		return "redirect:/approve/request/list";
        	case 3:
        		//人事部
        		return "redirect:/monthlyreport/list";
        }

        // 残業申請データ一覧を取得
        List<Requests> requestList = requestService.getRequestList(currentUserId);
        model.addAttribute("requestList", requestList);

        return "request/list";
	}

	//残業申請詳細画面表示
	@GetMapping("request/detail/{id}")
	public String getRequestDetail(Model model, Locale locale, @PathVariable("id") Integer id) {

	      
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        // 権限を取得
        String role = authentication.getAuthorities().stream()
        				.map(GrantedAuthority::getAuthority)
        				.findFirst()
        				.orElse(null);
        model.addAttribute("role", role);
        // ユーザー情報を取得
        Users user = userService.getUser(currentUserId);
        model.addAttribute("user", user);

        // 残業申請データ取得
        Requests request = requestService.getRequest(id);	//requestsテーブルのid
        model.addAttribute("request", request);
        //表示用に編集、モデルに設定
        if (request != null) {
        	editForDisplayService.editRequestForm(model, request);
        }
        
        //残業理由の改行を表示する
        String reasonWithBr = editForDisplayService.convertNewlinesToBr(request.getReason());
        model.addAttribute("reasonWithBr", reasonWithBr);

        
//        //勤務パターン欄編集
//        String workPatternsDisplay = "";
//        //休日（「休日」を表示）
//        if (calcOvertimeService.isWeekend(request.getOvertimeDate())) {
//        	workPatternsDisplay = "休日";
//        } else {
//        //平日（勤務パターンを表示）
//        	workPatternsDisplay = request.getWorkPatternsName() + "　" 
//        						+ request.getWorkPatternsStartTime() + " 〜 " + request.getWorkPatternsEndTime();
//        }
//        model.addAttribute("workPatternsDisplay", workPatternsDisplay);
//        
//        //残業予定時間欄編集
//        //前残業、後残業表示データ作成
//        Map<String, String> overtimeBefAftDisplayMap = editForDisplayService.getOvertimeBefAftDisplay(
//        								request.getStartTime(), request.getEndTime(),
//        								request.getWorkPatternsStartTime(), request.getWorkPatternsEndTime());
//        String beforeOvertimeDisplay = null;
//        String afterOvertimeDisplay = null;
//        if (overtimeBefAftDisplayMap.get("before") != null) {
//        	beforeOvertimeDisplay = overtimeBefAftDisplayMap.get("before");
//        }
//        if (overtimeBefAftDisplayMap.get("after") != null) {
//        	afterOvertimeDisplay = overtimeBefAftDisplayMap.get("after");
//        }
//        model.addAttribute("beforeOvertimeDisplay", beforeOvertimeDisplay);
//        model.addAttribute("afterOvertimeDisplay", afterOvertimeDisplay);
//        
//        //承認日、承認者編集
//        String approvalDate = "";
//        String approvalUsersName = "";
//        //承認済の場合、承認日、承認者を表示
//        //念の為nullチェックをして設定
//        if (request.getApprovalStatus().equals("承認済")) {
//        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年 M月 d日");
//        	approvalDate = (request.getApprovalDate() != null)
//        					? request.getApprovalDate().format(formatter)
//        					: "　　年　　月　　日";
//        	approvalUsersName = (request.getApprovalUsersName() != null)
//        							? request.getApprovalUsersName()
//        							: "";
//        }
//        model.addAttribute("approvalDate", approvalDate);
//        model.addAttribute("approvalUsersName", approvalUsersName);
// 
//        //報告欄フォーマット
//        String reportDate = "　　年　　月　　日";
//        model.addAttribute("reportDate", reportDate);
        
        
		return "request/detail";
	}
}
