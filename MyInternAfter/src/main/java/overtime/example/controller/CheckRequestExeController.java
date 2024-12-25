package overtime.example.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import overtime.example.domain.user.service.RequestService;

@Controller
public class CheckRequestExeController {

	@Autowired
	private RequestService requestService;

	//次長/残業申請確認更新処理
	@PostMapping("check/request/execute")
	public String postCheckRequestExecute(Model model, Locale locale,
			@RequestParam(value = "checkedRequests", required = false) List<Integer> checkedRequests) {

		if (checkedRequests == null || checkedRequests.isEmpty()) {
			//何も選択されていない場合、一覧へリダイレクト
			return "redirect:/check/request/list";
		}
		else {
			// 選択されたIDリストで isChecked を更新する
			for (Integer id : checkedRequests) {
				requestService.updChecked(id);
			}
		}
		return "redirect:/check/request/list";
	}
}
