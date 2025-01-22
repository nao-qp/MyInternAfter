package overtime.example;
import org.springframework.stereotype.Component;

@Component
public class AopTestComponent {

    public void checkAOP() {
        // インジェクトされた MyService のクラス名を表示
        System.out.println("AopTestComponentテスト");
    }
}