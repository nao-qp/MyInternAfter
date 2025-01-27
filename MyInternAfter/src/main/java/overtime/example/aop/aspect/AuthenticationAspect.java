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
	
	// @Pointcutを使って、@Authenticatedアノテーションがついたメソッドをターゲットにします
//    @Pointcut("@annotation(Authenticated)") // Authenticatedアノテーションが付いているメソッド
//    public void authenticatedMethods() {}

    // @Aroundアノテーションで、実行前に認証処理を挿入
    @Around("@annotation(overtime.example.aop.annotation.Authenticated)") // 上記のPointcutを適用
    public Object checkAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {

        // 認証チェックを行う
        boolean isAuthenticated = checkUserAuthentication(); // 認証状態を確認するロジックを呼び出し
        
        if (!isAuthenticated) {
        	return "redirect:/user/login";		// 認証情報がない場合は、ログインページにリダイレクトする
        }
        // 認証されている場合は、メソッドを実行
        return joinPoint.proceed(); // メソッド実行を進める
    }

    // 仮の認証チェックメソッド
    private boolean checkUserAuthentication() {    	
    	// 現在のユーザーの認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? true : false;
    }
}
