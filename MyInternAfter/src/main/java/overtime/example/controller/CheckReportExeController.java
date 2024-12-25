package overtime.example.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import overtime.example.domain.user.service.ReportService;

@Controller
public class CheckReportExeController {

	@Autowired
	private ReportService reportService;

	//次長/残業報告確認更新処理
	@PostMapping("check/report/execute")
	public String postCheckReportExecute(Model model, Locale locale,
			@RequestParam(value = "checkedreports", required = false) List<Integer> checkedreports) {

		if (checkedreports == null || checkedreports.isEmpty()) {
			//何も選択されていない場合、一覧へリダイレクト
			return "redirect:/check/report/list";
		}
		else {
			// 選択されたIDリストで isChecked を更新する
			for (Integer id : checkedreports) {
				reportService.updChecked(id);
			}
		}
		return "redirect:/check/report/list";
	}

}
