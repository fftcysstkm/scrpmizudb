package jsoup.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeData {

	private String baseUrl = "http://www1.river.go.jp";

	public Elements scrapeTable(Map<String, String> params)throws IOException {

		//Iframe表示ページでsrcURLを取得
		Document docu = Jsoup.connect(baseUrl + "/cgi-bin/DspWquaData.exe")
				.data(params)
				.get();
		Element iframe = docu.select("iframe").first();
		String src = iframe.attr("src");

		//Iframe先のテーブルを取得
		Document docuIframe = Jsoup.connect(baseUrl + src).get();
		Elements elements = docuIframe.select("tbody").get(1).select("tr");

		return elements;

		}

	//ヘッダーをリストとして取得するメソッド
	public List<String> getTableHeader(Elements elements){
		Elements tableHeader = elements.select("th");

		List<String>headerList=tableHeader.stream()
				.map(Element::text)
				.collect(Collectors.toList());
		return headerList;
	}

	public List<List<String>> getTableData(Elements elements){
		Elements tableData = elements.select("tr");

		tableData.remove(0);//th（ヘッダー）が不要のため削除。
		List<List<String>>dataList = new ArrayList<>();
		for (Element rowtd:tableData ) {
			dataList.add(rowtd.select("td").stream()
					.map(Element::text)
					.collect(Collectors.toList()));
		}
		return dataList;
	}


}


