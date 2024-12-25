package overtime.example.application.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import overtime.example.domain.user.model.Requests;

@Service
public class EditForDisplayService {

	@Autowired
	private CalcOvertimeService calcOvertimeService;
	
	//前残業、後残業表示用に編集
	
	/**
	 * 前残業、後残業表示用に編集
	 * 表示用Stringを格納したMapを返す。Key:"before","after"（無い場合はnullが返る）
	 * "前残業　startTime 〜 workPatternsStartTime"
	 * "後残業　workPatternsEndTime 〜 endTime"
	 * 
	 * @param startTime
	 * @param endTime
	 * @param workPatternsStartTime
	 * @param workPatternsEndTime
	 * @return overtimeBefAftDisplayMap
	 */
	public Map<String, String> getOvertimeBefAftDisplay(LocalTime startTime, LocalTime endTime, 
											LocalTime workPatternsStartTime, LocalTime workPatternsEndTime) {
		
		Map<String, String> overtimeBefAftDisplayMap = new HashMap<>();
		
		//残業開始時間が勤務パターン開始時間よりも前
		if (startTime.isBefore(workPatternsStartTime)) {
			//前残業あり
			String beforeOvertimeDisplay = "前残業　" + startTime + "　〜　" + workPatternsStartTime;
			overtimeBefAftDisplayMap.put("before", beforeOvertimeDisplay);
		}
		//残業終了時間が勤務パターン終了時間よりも後
		if (endTime.isAfter(workPatternsEndTime)) {
			//後残業あり
			String afterOvertimeDisplay = "後残業　" + workPatternsEndTime + "　〜　" + endTime;
			overtimeBefAftDisplayMap.put("after", afterOvertimeDisplay);
		}
		
		return overtimeBefAftDisplayMap;
	}
	
	
	/**
	 * 残業申請書の申請欄の表示用にデータを編集し、
	 * modelに設定する。
	 * 
	 * @param model
	 * @param request
	 */
	public void editRequestForm(Model model, Requests request) {
		//日付表示フォーマット
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy年 M月 d日");
		DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy年 M月 d日 HH:mm");
		
		//申請日欄編集
		String formatRequestDate = request.getRequestDate().format(formatterDateTime);
		String requestDate = "申請日：" + formatRequestDate;
		model.addAttribute("requestDate", requestDate);
		
		//勤務パターン欄編集
        String workPatternsDisplay = "";
        //休日（「休日」を表示）
        if (calcOvertimeService.isWeekend(request.getOvertimeDate())) {
        	workPatternsDisplay = "休日";
        } else {
        //平日（勤務パターンを表示）
        	workPatternsDisplay = request.getWorkPatternsName() + "　" 
        						+ request.getWorkPatternsStartTime() + " 〜 " + request.getWorkPatternsEndTime();
        }
        model.addAttribute("workPatternsDisplay", workPatternsDisplay);
        
        //残業予定時間欄編集
        //前残業、後残業表示データ作成
        Map<String, String> overtimeBefAftDisplayMap = getOvertimeBefAftDisplay(
        								request.getStartTime(), request.getEndTime(),
        								request.getWorkPatternsStartTime(), request.getWorkPatternsEndTime());
        String beforeOvertimeDisplay = null;
        String afterOvertimeDisplay = null;
        if (overtimeBefAftDisplayMap.get("before") != null) {
        	beforeOvertimeDisplay = overtimeBefAftDisplayMap.get("before");
        }
        if (overtimeBefAftDisplayMap.get("after") != null) {
        	afterOvertimeDisplay = overtimeBefAftDisplayMap.get("after");
        }
        model.addAttribute("beforeOvertimeDisplay", beforeOvertimeDisplay);
        model.addAttribute("afterOvertimeDisplay", afterOvertimeDisplay);
        
        //承認日、承認者編集
        String approvalDate = "　　年　　月　　日";
        String approvalUsersName = "";
        //承認済の場合、承認日、承認者を表示
        //念の為nullチェックをして設定
        if (request.getApprovalStatus() != null && request.getApprovalStatus().equals("承認済")) {
        	approvalDate = (request.getApprovalDate() != null)
        					? request.getApprovalDate().format(formatterDate)
        					: "　　年　　月　　日";
        	approvalUsersName = (request.getApprovalUsersName() != null)
        							? request.getApprovalUsersName()
        							: "";
        }
        model.addAttribute("approvalDate", approvalDate);
        model.addAttribute("approvalUsersName", approvalUsersName);
 
        //報告欄フォーマット
        String reportDate = "　　年　　月　　日";
        model.addAttribute("reportDate", reportDate);

	}
	
	// 改行を <br> タグに変換するメソッド
	public String convertNewlinesToBr(String input) {
	    if (input == null) {
	        return "";
	    }
	    return input.replace("\n", "<br>");
	}
}
