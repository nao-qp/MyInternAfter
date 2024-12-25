'use strict';
/**
 * 申請・報告一覧
 */

 // 申請一覧の行にクリックイベントを設定
  document.addEventListener("DOMContentLoaded", function () {
    const rows = document.querySelectorAll("tr[data-href]");

    rows.forEach(row => {
      row.addEventListener("click", function () {
        window.location.href = row.getAttribute("data-href");
      });
    });
  });
  
