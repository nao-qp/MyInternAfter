package overtime.example.controller.check;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import overtime.example.aop.annotation.Authenticated;
import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.service.RequestService;
import overtime.example.domain.user.service.impl.CustomUserDetails;

@Controller
public class CheckRequestListController {
	@Autowired
	private RequestService requestService;

	//残業申請確認一覧画面表示
	@GetMapping("check/request/list")
	@Authenticated
	public String getCheckRequestList(Model model, Locale locale,
			@AuthenticationPrincipal CustomUserDetails user) {

        model.addAttribute("user", user);

        //次長権限ではない場合、ログインページにリダイレクトする
        if (!user.getRole().equals(1)) {
        	return "redirect:/user/login";
        }

        // 残業申請データ一覧を取得
        List<Requests> requestList = requestService.getCheckDataList();
        model.addAttribute("requestList", requestList);


        return "check/request/list";

	}

}
