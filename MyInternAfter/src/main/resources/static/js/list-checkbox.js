/**
 * 課長/申請承認一覧画面
 */

 // 差戻、承認のどちらかにチェックを入れる処理。
 	//どちらかにチェックを入れて、どちらもチェックしない状態に戻す操作をするため。
  document.addEventListener('DOMContentLoaded', function() {
        // チェックボックスを制御するためのイベントリスナー
        const checkboxes = document.querySelectorAll('.action-checkbox');
        
        checkboxes.forEach(function(checkbox) {
            checkbox.addEventListener('change', function(event) {
                const checkbox = event.target;
                const requestId = checkbox.getAttribute('data-id');
                const actionType = checkbox.getAttribute('data-action');

                // もし差戻チェックボックスに変更があった場合
                if (actionType === 'return') {
                    // 承認チェックボックスを外す
                    const approveCheckbox = document.querySelector(`input[data-id="${requestId}"][data-action="approve"]`);
                    if (checkbox.checked) {
                        approveCheckbox.checked = false;
                    }
                }

                // もし承認チェックボックスに変更があった場合
                if (actionType === 'approve') {
                    // 差戻チェックボックスを外す
                    const returnCheckbox = document.querySelector(`input[data-id="${requestId}"][data-action="return"]`);
                    if (checkbox.checked) {
                        returnCheckbox.checked = false;
                    }
                }
            });
        });
    });