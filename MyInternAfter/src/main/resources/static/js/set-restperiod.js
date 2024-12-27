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
	const workPatternsStartTime = (document.getElementById("startTimeDisplay").textContent).replace('〜','');
	const workPatternsStartTimeDate = new Date('1970-01-01T' + workPatternsStartTime); // ローカルタイムでDateオブジェクトを作成
	// 勤務パターン終了時間
	const workPatternsEndTime = (document.getElementById("endTimeDisplay").textContent).replace('〜','');
	const workPatternsEndTimeDate = new Date('1970-01-01T' + workPatternsEndTime);  // ローカルタイムでDateオブジェクトを作成
	// 規定休憩
	const restPeriod = document.getElementById("restPeriod");
	

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
		let calcRestPeriodHours = String(calcRestPeriod).padStart(2, '0'); // 数値を文字列2桁にして0埋め
		let calcRestPeriodStr = `${calcRestPeriodHours}:00`;	// 文字列を00:00の形式にする
		restPeriod.value = calcRestPeriodStr;
	}

	// 開始終了時間変更changeリスナー
	startTime.addEventListener('input',getRestPeriod);
	endTime.addEventListener('input',getRestPeriod);
	
});