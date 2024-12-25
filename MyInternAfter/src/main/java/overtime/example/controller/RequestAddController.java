package overtime.example.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class RequestAddController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private WorkPatternService workPatternService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private ReportService reportService;

	//申請書新規作成画面表示
	@GetMapping("request/add")
	public String getRequestAdd(Model model, @ModelAttribute RequestForm form, Locale locale) {

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

        //申請日に今日の日付を設定
        form.setRequestDate(LocalDateTime.now());

        //勤務パターンマスター取得
        List<WorkPatterns> workPatternList = workPatternService.getWorkPatternMaster();
        model.addAttribute("workPatternList", workPatternList);

        //勤務パターンの初期値の設定
        //画面初期表示
        if (form.getWorkPatternsId() == null) {
        	model.addAttribute("initialDisplayWorkPatternsId", user.getWorkPatternsId());
        } else {
       //バリデーションからの画面再表示の場合
        	model.addAttribute("initialDisplayWorkPatternsId", form.getWorkPatternsId());
        }

        //残業実施日の初期値を設定
        form.setOvertimeDate(LocalDate.now());

        //休憩時間の初期値を00:00に設定
        form.setRestPeriod(LocalTime.of(0, 0));

        form.setWorkPatternsStartTime(null);
        
		return "request/add";
	}

	//申請書新規作成更新処理
	@PostMapping("request/add")
	public String postRequestAdd(Model model, 
			@ModelAttribute @Valid RequestForm form, BindingResult bindingResult, Locale locale) {

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
		          
	        return getRequestAdd(model, form, locale);
	       }
		
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

		//申請データ作成更新
        Requests request = modelMapper.map(form, Requests.class);
        //勤務パターン開始時間終了時間取得
        WorkPatterns workPattern = workPatternService.getWorkPattern(request.getWorkPatternsId());

        request.setUsersId(user.getId());
        request.setDepartmentsId(user.getDepartmentsId());
        //前残業開始時間、後残業終了時間が未設定の場合、通常勤務時間を設定する。
        if (request.getStartTime() == null) {
        	request.setStartTime(workPattern.getStartTime());
        }
        if (request.getEndTime() == null) {
        	request.setEndTime(workPattern.getEndTime());
        }

        request.setRestPeriod(LocalTime.of(0, 0));	//TODO:規定休憩時間を設定する
        requestService.addRequest(request);

        //申請データに紐づく報告データを作成更新
        Integer requestId = request.getId();	//申請データ作成時に付番されたidを取得、設定
        Reports report = new Reports();
        //申請データの情報を報告データに設定
        report.setUsersId(request.getUsersId());
        report.setRequestsId(requestId);
        report.setWorkPatternsId(request.getWorkPatternsId());
        report.setRequestDate(request.getRequestDate());
        report.setOvertimeDate(request.getOvertimeDate());
        report.setStartTime(request.getStartTime());
        report.setEndTime(request.getEndTime());
        
        reportService.addReport(report);

		return "redirect:/request/list?fromMenu=true";
	}
}
