package jsoup.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.select.Elements;

public class Main {

	public static void main(String[] args){

		try{

			//水文水質データベースの地点番号と開始日付、完了日付を設定
			TargetLocation targetLocation = new TargetLocation("405081285511070","20200101","20200521");
			//
			List<Map<String,String>>listPeriod = targetLocation.getListPeriod();

			//結果格納用のList（ネストされている）用意
			List<List<String>>tableList = new ArrayList<>();
			System.out.println(listPeriod);
			Boolean isFirstTime = true;

			for(Map<String,String>period : listPeriod) {
				//リクエストparam設定
				Map<String,String>params = new HashMap<>();
				params.put("KIND", "5");
				params.put("ID",targetLocation.getLocationId());
				params.put("BGNDATE",period.get("BGNDATE"));
				params.put("ENDDATE",period.get("ENDDATE"));
				params.put("KAWABOU","NO");

				//htmlを取得
				ScrapeData scrapeData = new ScrapeData();
				Elements html = scrapeData.scrapeTable(params);
				System.out.println(params);

				//ヘッダーとデータを取得し、Listに追加（初回のみヘッダー取得）
				if(isFirstTime) {
					tableList.add(scrapeData.getTableHeader(html));
					isFirstTime = false;
					}
				tableList.addAll(scrapeData.getTableData(html));
				Thread.sleep(1000);

			}

			//出力用ファイル設定
		    String dir = "src/main/resources/";
		    String filename = dir + "result.csv";
	        File file = new File(filename);
	        FileWriter fw = new FileWriter(file);
	        BufferedWriter bw = new BufferedWriter(fw);

	        //スクレイピングしたリストをカンマで結合した文字列へ変換・書き込み。
			for(List<String>row:tableList) {
				bw.write(row.stream()
						.collect(Collectors.joining(",")));
				bw.newLine();
			}
			bw.flush();
			bw.close();

		}catch(IOException e) {
			e.printStackTrace();

		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	}



