package overtime.example.controller.request;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import overtime.example.aop.annotation.Authenticated;
import overtime.example.application.service.EditForDisplayService;
import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.service.RequestService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class RequestController {

	@Autowired
	private RequestService requestService;
	
	@Autowired
	private EditForDisplayService editForDisplayService;

	
	//残業申請一覧画面表示
	
	@GetMapping("request/list")
	@Authenticated
	public String getRequestList(Model model, Locale locale,
			@RequestParam(value = "fromMenu", required = false) String fromMenu,
			@AuthenticationPrincipal CustomUserDetails user) {
		
		// 認証情報から取得したユーザー情報(CustomUserDetails user)を使用する
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
//        List<Requests> requestList = requestService.getRequestList(currentUserId);
        List<Requests> requestList = requestService.getRequestList(user.getId());
        model.addAttribute("requestList", requestList);

        return "request/list";
	}

	//残業申請詳細画面表示
	@GetMapping("request/detail/{id}")
	@Authenticated
	public String getRequestDetail(Model model, Locale locale, @PathVariable("id") Integer id,
			@AuthenticationPrincipal CustomUserDetails user,
			HttpSession session) {
			// Excel出力用にsessionを用意

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
        
        // Excel出力用にsessionに保存
        session.setAttribute("model", model);
        
		return "request/detail";
	}
}
