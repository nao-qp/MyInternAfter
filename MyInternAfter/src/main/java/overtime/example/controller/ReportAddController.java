package overtime.example.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import overtime.example.application.service.CalcOvertimeService;
import overtime.example.application.service.EditForDisplayService;
import overtime.example.application.service.TimeConverterService;
import overtime.example.domain.user.model.Overtime;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.model.WorkPatterns;
import overtime.example.domain.user.service.ReportService;
import overtime.example.domain.user.service.UserService;
import overtime.example.domain.user.service.WorkPatternService;
import overtime.example.domain.user.service.impl.CustomUserDetails;
import overtime.example.form.ReportForm;

@Controller
public class ReportAddController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private WorkPatternService workPatternService;
	
	@Autowired
	private TimeConverterService timeConverterService;

	@Autowired
	private CalcOvertimeService calcOvertimeService;
	
	@Autowired
	private EditForDisplayService editForDisplayService;

	//報告書入力画面表示
	@GetMapping("report/add/{id}")
	public String getReportAdd(Model model, Locale locale, @ModelAttribute ReportForm form,
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

        //報告データを取得
        Reports report = reportService.getReport(id);
        model.addAttribute("report", report);
        
        //バリデーションからの再表示の場合を考慮
        if (form.getWorkPatternsId() == null) {
        	//画面初期表示の場合
        	//既存データを初期値に設定
            form = modelMapper.map(report, ReportForm.class);
            //休憩時間の初期値を00:00に設定
            form.setRestStartTime(LocalTime.of(0, 0));
            form.setRestEndTime(LocalTime.of(0, 0));
            
            //残業開始時間と勤務パターン開始時間が同じならformに時間をセットしない。
        	//（同じ時間を入力できないようにバリデーションしているため。）
        	if (report.getStartTime().equals(report.getWorkPatternsStartTime())) {
        		form.setStartTime(null);
        	}
        	//終了時間も同様
        	if (report.getEndTime().equals(report.getWorkPatternsEndTime())) {
        		form.setEndTime(null);
        	}
        }
   
        model.addAttribute("reportForm", form);
        
        //報告日設定
        LocalDate reportDate = LocalDate.now();
        report.setReportDate(reportDate);

        
        //申請情報欄設定
        Map<String, String> overtimeBefAftDisplayMap = 
        		editForDisplayService.getOvertimeBefAftDisplay(
        				report.getRequestsStartTime(), report.getRequestsEndTime(),
        				report.getWorkPatternsStartTime(), report.getWorkPatternsEndTime());
        String beforeOvertimeDisplay = overtimeBefAftDisplayMap.get("before");
        String afterOvertimeDisplay = overtimeBefAftDisplayMap.get("after");
        model.addAttribute("beforeOvertimeDisplay", beforeOvertimeDisplay);
        model.addAttribute("afterOvertimeDisplay", afterOvertimeDisplay);
        //申請日編集
        LocalDate requestDate = report.getRequestDate().toLocalDate();
        model.addAttribute("requestDate", requestDate);
        
        //残業理由の改行を表示する
        String requestReasonWithBr = editForDisplayService.convertNewlinesToBr(report.getRequestsReason());
        model.addAttribute("requestReasonWithBr", requestReasonWithBr);
        

		return "report/add";
	}

	//報告書更新処理
	@PostMapping("report/add/{id}")
	public String postReportAdd(Model model, Locale locale, 
			@ModelAttribute @Valid ReportForm form, BindingResult bindingResult, @PathVariable("id") Integer id) {

		//バリデーション
		if (bindingResult.hasErrors()) {
			// クラスレベルのエラーメッセージをビューに渡す
			// bindingResultのcodeに含まれているバリデーション名でフィルターしそれぞれのエラーメッセージを振り分ける
			List<ObjectError> startEndisNullErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("ReportStartAndEndIsNull"))  
		            .collect(Collectors.toList());
			List<ObjectError> startTimeErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("ReportStartBeforeWorkStart"))  
		            .collect(Collectors.toList());
		    List<ObjectError> endTimeErrors = bindingResult.getGlobalErrors().stream()
		    		.filter(error -> error.getCode().contains("ReportEndAfterWorkEnd"))  
		            .collect(Collectors.toList());
		    List<ObjectError> restTimeStartEndErrors = bindingResult.getGlobalErrors().stream()
		    		.filter(error -> error.getCode().contains("RestStartBeforeRestEnd"))  
		            .collect(Collectors.toList());
		    
	        // モデルにエラーメッセージを追加
	    	model.addAttribute("startEndisNullErrors", startEndisNullErrors);
	        model.addAttribute("startTimeErrors", startTimeErrors);
	        model.addAttribute("endTimeErrors", endTimeErrors);
	        model.addAttribute("restTimeStartEndErrors", restTimeStartEndErrors);
		          
	        return getReportAdd(model, locale, form, id);
		}
				
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        
        ////報告データ更新
        Reports report = modelMapper.map(form, Reports.class);
        report.setId(id);
        report.setUsersId(currentUserId);
        
        //報告日設定
        LocalDate reportDate = LocalDate.now();
        report.setReportDate(reportDate);
        
        //勤務パターン開始時間終了時間取得
        WorkPatterns workPattern = workPatternService.getWorkPattern(report.getWorkPatternsId());
        //前残業開始時間、後残業終了時間が未設定の場合、通常勤務時間を設定する。
        if (report.getStartTime() == null) {
        	report.setStartTime(workPattern.getStartTime());
        }
        if (report.getEndTime() == null) {
        	report.setEndTime(workPattern.getEndTime());
        }
        
        //休憩時間設定
        report.setRestPeriod(
        		timeConverterService.toMinutesGetTimeDifference(report.getRestStartTime(), report.getRestEndTime())
        		);
        
      //残業時間計算
        //休日
        if (calcOvertimeService.isWeekend(report.getOvertimeDate())) {
        	Overtime updOvertimEmn = calcOvertimeService.toMinutesGetOvertimeHday(
	        		report.getUsersId(), 
	        		report.getStartTime(), report.getEndTime(), 
	        		report.getRestStartTime(), report.getRestEndTime());
        	
        	//作成データの実残業時間
        	report.setActualOvertime(updOvertimEmn.getActualOvertime());
        	//作成データまでの累計残業時間
        	report.setWdayDtUnder60(0);
        	report.setWdayDtOver60(0);
        	report.setWdayEmnUnder60(0);
        	report.setWdayEmnOver60(0);
            report.setHdayDtUnder60(updOvertimEmn.getHdayDtUnder60());
        	report.setHdayDtOver60(updOvertimEmn.getHdayDtOver60());
        	report.setHdayEmnUnder60(updOvertimEmn.getHdayEmnUnder60());
        	report.setHdayEmnOver60(updOvertimEmn.getHdayEmnOver60());
        	
        } else {
        //平日
        	Overtime updOvertimDt = calcOvertimeService.toMinutesGetOvertimeWday(
						        		report.getUsersId(), 
						        		workPattern.getStartTime(), workPattern.getEndTime(), 
						        		report.getStartTime(), report.getEndTime(), 
						        		report.getRestStartTime(), report.getRestEndTime());
        	
        	//作成データの実残業時間
        	report.setActualOvertime(updOvertimDt.getActualOvertime());
        	//作成データまでの累計残業時間
        	report.setWdayDtUnder60(updOvertimDt.getWdayDtUnder60());
        	report.setWdayDtOver60(updOvertimDt.getWdayDtOver60());
        	report.setWdayEmnUnder60(updOvertimDt.getWdayEmnUnder60());
        	report.setWdayEmnOver60(updOvertimDt.getWdayEmnOver60());
        	report.setHdayDtUnder60(0);
        	report.setHdayDtOver60(0);
        	report.setHdayEmnUnder60(0);
        	report.setHdayEmnOver60(0);
        }
        
        
        reportService.editReport(report);

		return "redirect:/report/list";
	}

	//新規報告書入力画面表示（事後報告）
	@GetMapping("report/new-add")
	public String getNewReportAdd(Model model, Locale locale, @ModelAttribute ReportForm form) {

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

        //報告日設定（今日）
        LocalDate reportDate = LocalDate.now();
        model.addAttribute("reportDate", reportDate);

        //バリデーションからの画面表示を考慮
        if (form.getWorkPatternsId() == null) {
        	//画面初期表示の場合
        	//勤務パターンはユーザー情報を初期値にする
        	model.addAttribute("initialDisplayWorkPatternsId", user.getWorkPatternsId());
        	
        	//残業実施日の初期値を設定
            form.setOvertimeDate(LocalDate.now());

            //休憩時間の初期値を00:00に設定
            form.setRestStartTime(LocalTime.of(0, 0));
            form.setRestEndTime(LocalTime.of(0, 0));
        } else {
        	//バリデーションからの画面再表示
        	//画面で設定していた勤務パターンを設定する
        	model.addAttribute("initialDisplayWorkPatternsId", form.getWorkPatternsId());
        }

		return "report/new-add";
	}

	//報告書新規作成（事後報告）
	@PostMapping("report/new-add")
	public String postNewReportAdd(Model model, Locale locale, 
			@ModelAttribute @Valid  ReportForm form, BindingResult bindingResult) {

		//バリデーション
		if (bindingResult.hasErrors()) {
			// クラスレベルのエラーメッセージをビューに渡す
			// bindingResultのcodeに含まれているバリデーション名でフィルターしそれぞれのエラーメッセージを振り分ける
			List<ObjectError> startEndisNullErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("ReportStartAndEndIsNull"))  
		            .collect(Collectors.toList());
			List<ObjectError> startTimeErrors = bindingResult.getGlobalErrors().stream()
					.filter(error -> error.getCode().contains("ReportStartBeforeWorkStart"))  
		            .collect(Collectors.toList());
		    List<ObjectError> endTimeErrors = bindingResult.getGlobalErrors().stream()
		    		.filter(error -> error.getCode().contains("ReportEndAfterWorkEnd"))  
		            .collect(Collectors.toList());
		    List<ObjectError> restTimeStartEndErrors = bindingResult.getGlobalErrors().stream()
		    		.filter(error -> error.getCode().contains("RestStartBeforeRestEnd"))  
		            .collect(Collectors.toList());
		    
	        // モデルにエラーメッセージを追加
	    	model.addAttribute("startEndisNullErrors", startEndisNullErrors);
	        model.addAttribute("startTimeErrors", startTimeErrors);
	        model.addAttribute("endTimeErrors", endTimeErrors);
	        model.addAttribute("restTimeStartEndErrors", restTimeStartEndErrors);
		          
	        return getNewReportAdd(model, locale, form);
		}
				
		// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //認証情報がない場合は、ログインページにリダイレクトする
        if (authentication == null) {
        	 return "redirect:/user/login";
        }

        // 認証されたユーザーのIDを取得
        Integer currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        ////報告データ作成更新
        Reports report = modelMapper.map(form, Reports.class);
        
        //報告日設定（今日）
        LocalDate reportDate = LocalDate.now();
        report.setReportDate(reportDate);
        
        //勤務パターン開始時間終了時間取得
        WorkPatterns workPattern = workPatternService.getWorkPattern(report.getWorkPatternsId());
        //前残業開始時間、後残業終了時間が未設定の場合、通常勤務時間を設定する。
        if (report.getStartTime() == null) {
        	report.setStartTime(workPattern.getStartTime());
        }
        if (report.getEndTime() == null) {
        	report.setEndTime(workPattern.getEndTime());
        }

        //休憩時間設定
        report.setRestPeriod(
        		timeConverterService.toMinutesGetTimeDifference(report.getRestStartTime(), report.getRestEndTime())
        		);
        //ユーザーID設定
        report.setUsersId(currentUserId);
        
        //残業時間計算
        //休日
        if (calcOvertimeService.isWeekend(report.getOvertimeDate())) {
        	Overtime updOvertimEmn = calcOvertimeService.toMinutesGetOvertimeHday(
	        		report.getUsersId(), 
	        		report.getStartTime(), report.getEndTime(), 
	        		report.getRestStartTime(), report.getRestEndTime());
        	
        	//作成データの実残業時間
        	report.setActualOvertime(updOvertimEmn.getActualOvertime());
        	//作成データまでの累計残業時間
        	report.setWdayDtUnder60(0);
        	report.setWdayDtOver60(0);
        	report.setWdayEmnUnder60(0);
        	report.setWdayEmnOver60(0);
            report.setHdayDtUnder60(updOvertimEmn.getHdayDtUnder60());
        	report.setHdayDtOver60(updOvertimEmn.getHdayDtOver60());
        	report.setHdayEmnUnder60(updOvertimEmn.getHdayEmnUnder60());
        	report.setHdayEmnOver60(updOvertimEmn.getHdayEmnOver60());
        	
        } else {
        //平日
        	Overtime updOvertimDt = calcOvertimeService.toMinutesGetOvertimeWday(
						        		report.getUsersId(), 
						        		workPattern.getStartTime(), workPattern.getEndTime(), 
						        		report.getStartTime(), report.getEndTime(), 
						        		report.getRestStartTime(), report.getRestEndTime());
        	
        	//作成データの実残業時間
        	report.setActualOvertime(updOvertimDt.getActualOvertime());
        	//作成データまでの累計残業時間
        	report.setWdayDtUnder60(updOvertimDt.getWdayDtUnder60());
        	report.setWdayDtOver60(updOvertimDt.getWdayDtOver60());
        	report.setWdayEmnUnder60(updOvertimDt.getWdayEmnUnder60());
        	report.setWdayEmnOver60(updOvertimDt.getWdayEmnOver60());
        	report.setHdayDtUnder60(0);
        	report.setHdayDtOver60(0);
        	report.setHdayEmnUnder60(0);
        	report.setHdayEmnOver60(0);
        }
        
        //報告データ作成更新処理
        reportService.addNewReport(report);

		return "redirect:/report/list";
	}
}
