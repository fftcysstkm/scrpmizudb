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


	private String beginDate;
	private String endDate;
	private String locationId;
	private List<Map<String,String>>listPeriod;

	//コンストラクタ
	TargetLocation(String locationId,String beginDate,String endDate){
		this.locationId = locationId;
		this.beginDate = beginDate;
		this.endDate = endDate;

		//自動観測データは7日間ごとしか取得できない。
		//そのため7日間間隔のBGNDATEとENDDATEのMapのListを作成。
		List<Map<String,String>>listPeriod = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localBeginDate = LocalDate.parse(beginDate,formatter);
		LocalDate localEndDate = LocalDate.parse(endDate,formatter);

		int dateDiff = (int)ChronoUnit.DAYS.between(localBeginDate, localEndDate) + 1;
		System.out.println(dateDiff);

		//設定した期間、そのまま
		if(dateDiff <= 8) {
			Map<String,String>map = new LinkedHashMap<String, String>();
			map.put("BGNDATE", localBeginDate.format(formatter));
			map.put("ENDDATE", localEndDate.format(formatter));
			listPeriod.add(map);
			this.listPeriod = listPeriod;

		}else {

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

