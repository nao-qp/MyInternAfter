package overtime.example.application.service;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class TimeConverterService {
	/**
	 * 分を受け取って、時間に変換する。
	 *
	 * @param Minutes
	 * @return 分を時間変換し、四捨五入して小数点以下2位にした数値。
	 */
	public double toHoursFromMinutes(int minutes) {
		double hours = minutes / 60.0;
		//四捨五入して小数点以下2在表示にする。
		return Math.round(hours * 100.0) / 100.0;
	}

	/**
	 * LocalTime型を受け取って、分に変換する。
	 *
	 * @param localTime
	 * @return 分
	 */
	public int toMinutesFromLocalTime(LocalTime localTime) {
		//LocalTimeから秒を取得
		int seconds = localTime.toSecondOfDay();
		//秒から分へ（00:00で入力しているので、常に60で割り切れる。）
		return seconds / 60;
	}

	/**
	 * LocalTime型の時間を受け取って、時間差を分で取得する。
	 * @param startTime
	 * @param endTime
	 * @return 時間差（分）
	 */
	public int toMinutesGetTimeDifference(LocalTime startTime, LocalTime endTime) {
		// LocalTime の差を Duration で取得
        Duration duration = Duration.between(startTime, endTime);
        // Duration を分単位で取得
        long minutes = duration.toMinutes();
        return (int)minutes;
	}


}
