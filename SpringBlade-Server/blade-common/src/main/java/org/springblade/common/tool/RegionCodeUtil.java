package org.springblade.common.tool;


import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springblade.common.entity.Region;
import org.springframework.util.ObjectUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangnx
 */
public class RegionCodeUtil {
    public static final String LEVEL_PROVINCE = "provincetr";
    public static final String LEVEL_CITY = "citytr";
    public static final String LEVEL_COUNTY = "countytr";
    public static final String LEVEL_TOWN = "towntr";
    public static final String LEVEL_VILLAGE = "villagetr";


    public static final String CHARSET = "GBK";
    private static List<String> types = new ArrayList<>();

    static {
        types.add(LEVEL_PROVINCE);
        types.add(LEVEL_CITY);
        types.add(LEVEL_COUNTY);
        types.add(LEVEL_TOWN);
        types.add(LEVEL_VILLAGE);
    }
    private static List<String> specialCitys = new ArrayList<>();
    static {
        specialCitys.add("东莞市");
        specialCitys.add("中山市");
        specialCitys.add("儋州市");
    }

    public static final String webUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html";
    public static int TARGET_LEVEL = 5;

    /**
     * 读取行政区划信息
     * @return
     */
    public static Region getRegion(String area, Integer level) {
        if (ObjectUtils.isEmpty(level)){
            level = TARGET_LEVEL;
        }

        Region region = new Region("000000000000", "中国", 0);
        region.child = new ArrayList<>();
        Document doc = getDocument(webUrl);
        Elements provinceTr = doc.getElementsByClass(LEVEL_PROVINCE);
        for (Element e : provinceTr) {
            Elements a = e.getElementsByTag("a");
            for (Element ea : a) {
                String nextUrl = ea.attr("abs:href");
                String[] arr = nextUrl.split("/");
                String code = arr[arr.length - 1].split("\\.")[0] + "0000000000";
                String name = ea.text();
                if ("中国".equals(area) || area.equals(name)) {
                    Region child = new Region(code, name, 1);
                    region.child.add(child);
					System.out.println(name +" : "+ code + " : "+1);
                    int currentLevel = getLevel(LEVEL_PROVINCE);
                    if (currentLevel < level) {
                        parseNext(types.get(1), nextUrl, child,level);
                    }
                }
            }
        }
        return region;
    }


    @SneakyThrows
	private static Document getDocument(String url){
        Document doc;
        try {
            Thread.sleep(100);
            doc =Jsoup.parse(new URL(url).openStream(), CHARSET , url);
        } catch (IOException | InterruptedException e) {
            System.out.println("重新载入");
            Thread.sleep(3000);
           doc = heavyLoadIn(url);
        }
        return doc;
    }

    private static Document heavyLoadIn(String url) {
        try {
            return Jsoup.parse(new URL(url).openStream(), CHARSET, url);
        } catch (IOException e) {
            heavyLoadIn(url);
        }
        return null;
    }

    /**
     * @param type 见LEVEL_
     * @return
     */
    private static int getLevel(String type) {
        return types.indexOf(type) + 1;
    }



    /**
     * 解析下一级数据
     *
     * @param type   见LEVEL_开头
     * @param url    要抓取的网页url
     * @param region 将要保存的数据
     * @param level
     * @throws Exception
     */
    public static void parseNext(String type, String url, Region region, Integer level) {
        region.child = new ArrayList<>();
        Document doc = getDocument(url);
        Elements es = doc.getElementsByClass(type);
        if (LEVEL_VILLAGE.equals(type)) {
            for (Element e : es) {
                Elements tds = e.getElementsByTag("td");
                String code = tds.get(0).text();
                String name = tds.get(2).text();
                Region child = new Region(code, name, region.level + 1);
                region.child.add(child);
				println(child.level,code,name);
            }
        } else {
            for (Element e : es) {
                String code,name,nextUrl = null;

                Elements a = e.getElementsByTag("a");
                if (a.isEmpty()) {
                    Elements tds = e.getElementsByTag("td");
                    code = tds.get(0).text();
                    name = tds.get(1).text();
                } else {
                    nextUrl = a.get(0).attr("abs:href");
                    code = a.get(0).text();
                    name = a.get(1).text();
                }
                Region child = new Region(code, name, region.level + 1);
                region.child.add(child);
				println(child.level,code,name);
				int currentLevel = getLevel(type);
                if (!a.isEmpty() && currentLevel < level) {
                    String nextType;
                    if (specialCitys.contains(name)) {
                        nextType = LEVEL_TOWN;
                    } else {
                        nextType = types.get(types.indexOf(type) + 1);
                    }
                    parseNext(nextType, nextUrl, child, level);
                }
            }
        }

    }

	private static String space(int level) {
    	String space="";
		for (int i = 1; i <level ; i++) {
			space+="     ";
		}
		return space;
	}

	private static void println(int level, String code, String name) {
		System.out.println(space(level) + name +" : "+ code + " : "+level);
	}


}
