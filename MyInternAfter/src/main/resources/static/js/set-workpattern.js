'use strict';
/**
 *  残業申請新規作成
 */

 document.addEventListener("DOMContentLoaded", function () {

		//勤務パターン初期値にユーザー情報の勤務パターンを設定
        // ドロップダウンリストを取得
        const dropdown = document.getElementById("workPatternsId");

        // ドロップダウンの全てのオプションをループ
        for (let option of dropdown.options) {
            // 初期値が一致する場合に選択
            if (option.value == initialDisplayWorkPatternsId) {
                option.selected = true;
                break;
            }
        }
        
        // 初期表示時にも選択された勤務パターンに基づいて開始時間と終了時間を設定
	    const selectedWorkPatternId = dropdown.value; // 初期選択されたIDを取得
	    updateWorkPatternTimes(selectedWorkPatternId); // 勤務パターンの時間を更新


	    // ドロップダウンの変更イベントを設定
	    dropdown.addEventListener('change', function () {
	        const selectedWorkPatternId = this.value; // 選択された勤務パターンのIDを取得
	        updateWorkPatternTimes(selectedWorkPatternId); // 勤務パターンの時間を更新
	    });
	    
		// 勤務パターンの開始時間と終了時間を表示する関数
	    function updateWorkPatternTimes(selectedWorkPatternId) {

            // 選択された勤務パターンの開始時間と終了時間を取得
            const selectedPattern = workPatterns.find(pattern => pattern.id == selectedWorkPatternId);
            
            // 開始時間と終了時間を表示部分に設定
            if (selectedPattern) {
                const startTimeDisplay = document.getElementById('startTimeDisplay');
                const endTimeDisplay = document.getElementById('endTimeDisplay');
                
                // 開始時間
                startTimeDisplay.textContent = `〜${formatTime(selectedPattern.startTime)}`;
            
                // 終了時間
                endTimeDisplay.textContent = `${formatTime(selectedPattern.endTime)}〜`;
            }
            
            // formの隠し項目に選択している勤務パターンの開始終了時間を設定する。
			// （バリデーションで使用）
			document.getElementById('workPatternsStartTime').value = selectedPattern.startTime;
			document.getElementById('workPatternsEndTime').value = selectedPattern.endTime;
            
			}
			
	
		// 時間のフォーマット関数
	    function formatTime(time) {
	        return time ? time.substring(0, 5) : '00:00';  // HH:mm形式にフォーマット
	    }
        
	});


	
	
 