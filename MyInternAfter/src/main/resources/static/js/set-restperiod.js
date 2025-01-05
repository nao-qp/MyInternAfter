'use strict';
/**
 * 規定休憩時間設定
 * 入力した残業開始時間、終了時間をもとに規定休憩時間を設定
 */

document.addEventListener("DOMContentLoaded", function () {
	//// 要素を取得 ////
	// 前残業開始時間
	const startTime = document.getElementById("startTime");
	// 後残業終了時間
	const endTime = document.getElementById("endTime");
	// 勤務パターン開始時間
		// 文字列（勤務パターン変更時に再設定）
		let workPatternsStartTime = (document.getElementById("startTimeDisplay").textContent).replace('〜','');
		// Date型（勤務パターン変更時に再設定）
		let workPatternsStartTimeDate = new Date('1970-01-01T'
		 	+ workPatternsStartTime.split(':').map(t => t.padStart(2, '0')).join(':')); // ローカルタイムでDateオブジェクトを作成
		// 勤務パターン終了時間
		// 文字列（勤務パターン変更時に再設定）
		let workPatternsEndTime = (document.getElementById("endTimeDisplay").textContent).replace('〜','');
		// Date型（勤務パターン変更時に再設定）
		let workPatternsEndTimeDate = new Date('1970-01-01T' 
			+ workPatternsEndTime.split(':').map(t => t.padStart(2, '0')).join(':'));  // ローカルタイムでDateオブジェクトを作成
	// 規定休憩
	const restPeriod = document.getElementById("restPeriod");
	// 規定休憩（表示用）
	const displayRestPeriod = document.getElementById("displayRestPeriod");
	// 休憩時間>=規定休憩時間バリデーションエラーメッセージ
	const moreThanRestPeriodErrorsMessage = document.getElementById("moreThanRestPeriodErrorsMessage");
	
	// 開始時間最小値取得ファンクション
	function getMinStartTime(startTimeDate, workPatternsStartTimeDate) {
		let minStartTime;
		if (startTimeDate < workPatternsStartTimeDate) {
			minStartTime = startTimeDate;
		} else {
			minStartTime = workPatternsStartTimeDate;
		}
		return minStartTime;	// Date型
	}
	
	// 終了時間最大値取得ファンクション
	function getMaxEndTime(endTimeDate, workPatternsEndTimeDate) {
		let maxEndTime;
		if (endTimeDate > workPatternsEndTimeDate) {
			maxEndTime = endTimeDate;
		} else {
			maxEndTime = workPatternsEndTimeDate;
		}
		return maxEndTime;	// Date型
	}
	
	// 規定残業時間算出ファンクション
	function getRestPeriod () {
		// 前残業開始時間、後残業終了時間取得
		const startTimeStr = startTime.value;
		const startTimeDate = new Date('1970-01-01T' + startTimeStr);  // ローカルタイムでDateオブジェクトを作成
		const endTimeStr = endTime.value;
		const endTimeDate = new Date('1970-01-01T' + endTimeStr);  // ローカルタイムでDateオブジェクトを作成
		
		// 勤務パターン変更後、表示テキストの変更を検知しているので、変更後の内容を再設定
		// 勤務パターン開始時間再設定
		workPatternsStartTime = (document.getElementById("startTimeDisplay").textContent).replace('〜','');
		workPatternsStartTimeDate = new Date('1970-01-01T' 
			+ workPatternsStartTime.split(':').map(t => t.padStart(2, '0')).join(':')); // ローカルタイムでDateオブジェクトを作成
		// 勤務パターン終了時間再設定
		workPatternsEndTime = (document.getElementById("endTimeDisplay").textContent).replace('〜','');
		workPatternsEndTimeDate = new Date('1970-01-01T' 
			+ workPatternsEndTime.split(':').map(t => t.padStart(2, '0')).join(':'));  // ローカルタイムでDateオブジェクトを作成
		
		// 開始時間最小値設定
		const minStartTime = getMinStartTime(startTimeDate, workPatternsStartTimeDate);
		// 終了時間最大値設定
		const maxEndTime = getMaxEndTime(endTimeDate, workPatternsEndTimeDate);
	
		// 残業含めた勤務時間 ミリ秒から時間に変換（端数切り捨て）する
		let workTime = Math.floor((maxEndTime - minStartTime)/(1000 * 60 * 60));
		// 規定休憩時間
		let calcRestPeriod;
		// 8時間ごとに1時間の休憩が必要
		if (workTime >= 16) {
			// 通常勤務＋残業時間で16時間以上になった時に、残業での休憩時間が1時間必要
			calcRestPeriod = 1;
		} else {
			calcRestPeriod = 0;
		}
		// 規定休憩時間をセット
		
		restPeriod.value = calcRestPeriod * 60;	// 分
		// 規定休憩時間を表示
		let calcRestPeriodHours = String(calcRestPeriod).padStart(2, '0'); // 数値を文字列2桁にして0埋め
		let calcRestPeriodStr = `${calcRestPeriodHours}:00`;	// 文字列を00:00の形式にする
		displayRestPeriod.textContent = calcRestPeriodStr;
	}
	
	// ページ読み込み時に残業開始終了時間の初期値で、規定休憩時間を設定
	getRestPeriod();


	// 開始終了時間変更changeリスナー
	startTime.addEventListener('input',getRestPeriod);
	endTime.addEventListener('input',getRestPeriod);
	
	// 勤務パターンを変更し、通常勤務開始時間、終了時間が変更になった場合も
	// 規定休憩時間を設定しなおす。
	// 通常勤務開始、終了時間テキスト変更監視
	// <p>要素を取得
	const startTimeDisplay = document.getElementById("startTimeDisplay");
	const endTimeDisplay = document.getElementById("endTimeDisplay");
	
	// MutationObserverの設定 (監視用オブジェクト)
	const observer = new MutationObserver((mutationsList) => {
	    mutationsList.forEach(mutation => {
	        if (mutation.type === 'childList') {
				// エラーメッセージをいったんクリアする
				if (moreThanRestPeriodErrorsMessage !== null) {
					moreThanRestPeriodErrorsMessage.textContent = '';
				}
				// 規定休憩時間再計算、表示
				getRestPeriod();
	        }
	    });
	});
	
	// 監視設定：子要素の変更を監視
	const config = { childList: true };
	
	// 監視開始
	// 通常勤務の開始終了時間の表示が変更になったか
	observer.observe(startTimeDisplay, config);
	observer.observe(endTimeDisplay, config);
	
	
});