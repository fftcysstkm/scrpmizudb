package jsoup.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;


@Data
public class TargetLocation {

	//取得したい期間の開始日と終了日
	private String beginDate;
	private String endDate;
	//地点番号
	private String locationId;
	//取得したい開始日・終了日のMapのList
	private List<Map<String,String>>listPeriod;

	//コンストラクタ
	TargetLocation(String locationId,String beginDate,String endDate){

		this.locationId = locationId;

		this.beginDate = beginDate;
		this.endDate = endDate;

		//自動観測データは7日間ごと（開始日-終了日+1 → ８日間隔）しか取得できない。
		//そのため8日間間隔のBGNDATEとENDDATEのMapのListを作成。
		List<Map<String,String>>listPeriod = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localBeginDate = LocalDate.parse(beginDate,formatter);
		LocalDate localEndDate = LocalDate.parse(endDate,formatter);

		//取得したい期間の日数
		int dateDiff = (int)ChronoUnit.DAYS.between(localBeginDate, localEndDate) + 1;
		System.out.println(dateDiff);

		//取得したい期間の日数が8日以下の場合はListの要素はMap一つだけ。
		if(dateDiff <= 8) {
			Map<String,String>map = new LinkedHashMap<String, String>();
			map.put("BGNDATE", localBeginDate.format(formatter));
			map.put("ENDDATE", localEndDate.format(formatter));
			listPeriod.add(map);
			this.listPeriod = listPeriod;

		}else {

			//取得したい期間の日数が8日より大きい場合。8日間隔のMapを作って行き、Listにappend。
			LocalDate tmpBeginDate = localBeginDate;
			LocalDate tmpEndDate = localBeginDate.plusDays(7);


			while (tmpEndDate.isBefore(localEndDate)) {
				Map<String,String>map = new LinkedHashMap<String, String>();
				map.put("BGNDATE", tmpBeginDate.format(formatter));
				map.put("ENDDATE", tmpEndDate.format(formatter));
				listPeriod.add(map);

				tmpBeginDate = tmpEndDate.plusDays(1);
				tmpEndDate = tmpBeginDate.plusDays(7);
			}

			//ループ後半で、ちょうど設定した期間の最終日がMapのENDDATEになるよう調整。
			if (tmpBeginDate.isBefore(localEndDate) || (tmpBeginDate.isEqual(localEndDate))) {
				Map<String,String>map = new LinkedHashMap<String, String>();
				map.put("BGNDATE", tmpBeginDate.format(formatter));
				map.put("ENDDATE", localEndDate.format(formatter));
				listPeriod.add(map);
			}

			this.listPeriod=listPeriod;
		}
	}
}

