package overtime.example.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.model.WorkPatterns;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.RequestService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.WorkPatternService;
import overtime.example.domain.user.service.impl.CustomUserDetails;
import overtime.example.form.RequestForm;

@Controller
public class RequestEditController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private WorkPatternService workPatternService;
	
	@Autowired
	private ReportService reportService;

	//差し戻し後、申請書修正画面表示
	@GetMapping("request/edit/{id}")
	public String getRequestEdit(Model model, @ModelAttribute RequestForm form, Locale locale,
									@PathVariable("id") Integer id) {

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

        //勤務パターンマスター取得
        List<WorkPatterns> workPatternList = workPatternService.getWorkPatternMaster();
        model.addAttribute("workPatternList", workPatternList);

        // 残業申請データ取得
        Requests request = requestService.getRequest(id);	//requestsテーブルのid
        model.addAttribute("request", request);

        //既存requestデータをformに設定する前に、
        //勤務パターンの初期値の設定
        //バリデーションからの画面再表示の場合
        if (form.getWorkPatternsId() != null) {
        	model.addAttribute("initialDisplayWorkPatternsId", form.getWorkPatternsId());
        } else {
        //画面初期表示の場合
        	//既存データを初期値に設定
        	form = modelMapper.map(request, RequestForm.class);
        	//残業開始時間と勤務パターン開始時間が同じならformに時間をセットしない。
        	//（同じ時間を入力できないようにバリデーションしているため。）
        	if (request.getStartTime().equals(request.getWorkPatternsStartTime())) {
        		form.setStartTime(null);
        	}
        	//終了時間も同様
        	if (request.getEndTime().equals(request.getWorkPatternsEndTime())) {
        		form.setEndTime(null);
        	}
        }
        	
        model.addAttribute("requestForm", form);

		return "request/edit";
	}

	//差し戻し後、申請書修正更新処理
	@PostMapping("request/edit/{id}")
	public String postRequestEdit(Model model, @ModelAttribute @Valid RequestForm form, BindingResult bindingResult, 
					Locale locale, @PathVariable("id") Integer id) {

		//バリデーション
		if (bindingResult.hasErrors()) {
			// クラスレベルのエラーメッセージをビューに渡す
			// bindingResultのcodeに含まれているバリデーション名でフィルターしそれぞれのエラーメッセージを振り分ける
			List<ObjectError> startEndisNullErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("StartAndEndIsNull"))  
		            .collect(Collectors.toList());
			List<ObjectError> startTimeErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("StartBeforeWorkStart"))  
		            .collect(Collectors.toList());
		    List<ObjectError> endTimeErrors = bindingResult.getGlobalErrors().stream()
		    		.filter(error -> error.getCode().contains("EndAfterWorkEnd"))  
		            .collect(Collectors.toList());
		    
	        // モデルにエラーメッセージを追加
	    	model.addAttribute("startEndisNullErrors", startEndisNullErrors);
	        model.addAttribute("startTimeErrors", startTimeErrors);
	        model.addAttribute("endTimeErrors", endTimeErrors);
		          
	        return getRequestEdit(model, form, locale, id);
	       }
				
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        //修正更新
        Requests request = modelMapper.map(form, Requests.class);
        request.setId(id);
        
        //勤務パターン開始時間終了時間取得
        WorkPatterns workPattern = workPatternService.getWorkPattern(request.getWorkPatternsId());
        //前残業開始時間、後残業終了時間が未設定の場合、通常勤務時間を設定する。
        if (request.getStartTime() == null) {
        	request.setStartTime(workPattern.getStartTime());
        }
        if (request.getEndTime() == null) {
        	request.setEndTime(workPattern.getEndTime());
        }
        
        request.setRestPeriod(LocalTime.of(0, 0));	//TODO:規定休憩時間を設定する
        requestService.updateEdit(request);

        //申請データに紐づく報告データを修正更新
        Integer requestId = request.getId();	//申請データ作成時に付番されたidを取得、設定
        Reports report = new Reports();
        //申請データの情報を報告データに設定
        report.setRequestsId(requestId);
        report.setWorkPatternsId(request.getWorkPatternsId());
        report.setStartTime(request.getStartTime());
        report.setEndTime(request.getEndTime());
        
        reportService.updateEditReport(report);
        
		return "redirect:/request/list?fromMenu=true";
	}
}
