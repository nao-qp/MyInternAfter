package overtime.example.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {
//	@Autowired
//	private AuthenticationInfo authenticationInfo;

	
	// @Pointcutを使って、@Authenticatedアノテーションがついたメソッドをターゲットにします
//    @Pointcut("@annotation(Authenticated)") // Authenticatedアノテーションが付いているメソッド
//    public void authenticatedMethods() {}

    // @Aroundアノテーションで、実行前に認証処理を挿入
    @Around("@annotation(Authenticated)") // 上記のPointcutを適用
    public Object checkAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	System.out.println("テスト");

		 
		
        // 認証チェックを行う
        boolean isAuthenticated = checkUserAuthentication(); // 認証状態を確認するロジックを呼び出し
        
        if (!isAuthenticated) {
            throw new SecurityException("User not authenticated"); // 認証されていない場合は例外をスロー
        }

        // 認証されている場合は、メソッドを実行
        return joinPoint.proceed(); // メソッド実行を進める
    }

    // 仮の認証チェックメソッド
    private boolean checkUserAuthentication() {    	
    	// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 取得した認証情報をセッションに設定
        if (authentication != null) {
//        	authenticationInfo.setUserId(((CustomUserDetails) authentication.getPrincipal()).getId());
        	return true;
        } else {
        	// 認証情報なし
        	return false;
        }

    }
}
