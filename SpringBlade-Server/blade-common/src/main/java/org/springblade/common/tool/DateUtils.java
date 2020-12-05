package org.springblade.common.tool;


import lombok.SneakyThrows;
import org.springblade.common.entity.DateVo;
import org.springblade.core.tool.utils.Func;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日期工具类
 * @author zhangnx
 */
public class DateUtils {

	public static final String yMdHmsS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String yMdHms = "yyyy-MM-dd HH:mm:ss";
	public static final String yMdHm = "yyyy-MM-dd HH:mm";
	public static final String yMd = "yyyy-MM-dd";
	public static final String Hms = "HH:mm:ss";
	public static final String Hm = "HH:mm";

	public static final String yMdHmsS_Number = "yyyyMMddHHmmssSSS";
	public static final String yMd_Number = "yyyyMMdd";



	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date,yMdHms);
	}

	public static String format(Date date,String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}



	/**
	 * 生成id
	 * @return
	 */
	public static long getId() {
		String format = format(new Date(), DateUtils.yMdHmsS_Number);
		long randomNum = getRandomNum(2);
		String result = format+randomNum;
		return Long.valueOf(result);
	}



	/**
	 * 获取随机数
	 * @param n
	 * @return
	 */
	protected static long getRandomNum(int n){
		if(n<1){
			return 0;
		}
		return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
	}



	/**
	 * 日期是否有空的
	 * @param dates
	 * @return
	 */
	public static boolean isNull(Object... dates) {
		for (Object date : dates) {
			if (Func.isEmpty(date)){
				return true;
			}
		}
		return false;
	}



	/**
	 * 获取两个日期时间的时长
	 * @param startDte
	 * @param endDate
	 * @return
	 */
	public static String getDuration(Date startDte, Date endDate) {
		if (isNull(startDte,endDate)){
			return "";
		}
		long millisecond = endDate.getTime() - startDte.getTime();
		return getDuration(millisecond);
	}

	public static String getDuration(long millisecond) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;

		long day = millisecond / nd;
		// 计算差多少小时
		long hour = millisecond % nd / nh;
		// 计算差多少分钟
		long min = millisecond % nd % nh / nm;
		// 计算差多少秒//输出结果
		long sec = millisecond % nd % nh % nm / ns;
		List<String> list = new ArrayList<String>();
		if (day > 0) {
			list.add(day + "天");
		}
		if (hour > 0) {
			list.add(hour + "小时");
		}
		if (min > 0) {
			list.add(min + "分钟");
		}
		if (sec > 0) {
			list.add(sec + "秒");
		}

		return String.join("", list);

	}


	/**
	 * 获取N分钟后的日期时间
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date getNewDateAddMinutes(Date date,int minutes){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE,minutes);
		 return c.getTime();
	}

	/**
	 * 获取N天后的日期时间
	 * @param date
	 * @param days 天后
	 * @return
	 */
	public static Date getNewDateAddDays(Date date,int days) {
		if (date==null){
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}



	/**
	 * 获取N月后的日期时间
	 * @param date
	 * @param months 个月后
	 * @return
	 */
	public static Date  getNewDateAddMonths(Date date,int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,months);
		return c.getTime();
	}



	/**
	 * 获取两个日期之间的天
	 * @param startDte
	 * @param endDate
	 * @return
	 */
	public static long getDay(Date startDte, Date endDate) {
		if (isNull(startDte,endDate)) {
			return 0;
		}
		long nd = 1000 * 24 * 60 * 60;
		long diff = endDate.getTime() - startDte.getTime();
		long diffDay = diff / nd;
		if (diffDay >= 0) {
			diffDay++;
		} else {
			diffDay--;
		}
		return diffDay;
	}



	/**
	 * LocalDateTime转 Date
	 * @param localDate
	 * @return
	 */
	public static Date localTimeToDate(LocalDateTime localDate) {
		if (isNull(localDate)){
			return null;
		}
		return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
	}





	/**
	 * 根据类型获取分割出来的 年|月|日|时|分|秒|豪秒
	 * @param date
	 * @param type [年:Calendar.YEAR,
	 *                月:Calendar.MONTH,
	 *                日: Calendar.DAY_OF_MONTH,
	 *                时:Calendar.HOUR_OF_DAY,
	 *                分: Calendar.MINUTE,
	 *                秒:Calendar.SECOND,
	 *                豪秒:Calendar.MILLISECOND]
	 * @return 2020|12|31|23|59|59|999
	 */
	public static int getDateCutStr(Date date,int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (type==Calendar.MONTH) {
			return calendar.get(type)+1;
		}
		return calendar.get(type);
	}



	/**
	 *获取两个日期之间的分钟数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getMinute(Date startDate, Date endDate) {
		if (isNull(startDate,endDate)){
			return 0;
		}
		long l = endDate.getTime() - startDate.getTime();
		return l/(1000*60);
	}



	/**
	 * 获取总分钟数
	 * @param HsmStr 14:25:32
	 * @return
	 */
	public static int getTotalMinute(String HsmStr) {
		if (Func.isBlank(HsmStr)){
			return 0;
		}
		String[] split = HsmStr.split(":");
		if (split.length<3){
			throw new IllegalArgumentException("参数时间格式不正确，正确格式应为：14:25:32，传入的格式为："+HsmStr);
		}

		Integer h = Integer.valueOf(split[0]);
		Integer m = Integer.valueOf(split[1]);

		return h*60+m;
	}

	public static int getTotalMinute(Date date) {
		if (isNull(date)){
			return 0;
		}
		return getTotalMinute(format(date,Hms));
	}



	/**
	 * 获取日期新时间
	 * @param date
	 * @param Hms
	 * @return
	 */
	public static Date getDateNewTime(Date date,String Hms){
		if (Func.isBlank(Hms)){
			return date;
		}
		SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
		String yMdStr = yMd.format(date);
		String[] split = Hms.split(":");
		if (split.length<2){
			Hms+=":00:00";
		}else if (split.length<3){
			Hms+=":00";
		}

		String yMdHmsStr = yMdStr + " " + Hms;
		SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		return yMdHms.parse(yMdHmsStr, pos);
	}


	/**
	 * 获取今天日期时间
	 * @param minute
	 * @return
	 */
	public static Date getToDayDateTime(int minute){
		String yMdStr = format(new Date(),yMd);
		String hmsStr = getHms(minute);
		String yMdHmsStr = yMdStr + " " + hmsStr;
		SimpleDateFormat format = new SimpleDateFormat(yMdHms);
		return format.parse(yMdHmsStr, new ParsePosition(0));

	}

	/**
	 * 获取时分秒格式
	 * @param minute
	 * @return
	 */
	public static String getHms(long minute){
		long h = minute/60;
		if (h>=24){
			h=h%24;
		}
		long m = minute%60;

		StringBuilder Hms = new StringBuilder();
		Hms.append(h<10? "0"+h : h).append(":");
		Hms.append(m<10? "0"+m : m).append(":");
		Hms.append("00");

		return Hms.toString();
	}



	public static Date toDate(String dateStr) {
		return toDate(dateStr,yMdHms);
	}

	@SneakyThrows
	public static Date toDate(String dateStr,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateStr);

	}

	/**
	 *获取某一天的23点59分59秒
	 * @param date
	 * @return
	 */
	public static Date getDateMaxTime(Date date) {
		if (Func.isEmpty(date)){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),23, 59, 59);
		return calendar.getTime();

	}

	/**
	 *获取某一天的00点00分00秒
	 * @param date
	 * @return
	 */
	public static Date getDateMinTime(Date date) {
		if (Func.isEmpty(date)){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return calendar.getTime();
	}


	public static void main(String[] args) throws Exception {
		List<DateVo> dateVoList = new ArrayList<>();
		Date date = new Date();
		dateVoList.add(new DateVo(date,getNewDateAddMinutes(date,60)));
		dateVoList.add(new DateVo(getNewDateAddMinutes(date,60),getNewDateAddMinutes(date,120)));
		dateVoList.add(new DateVo(getNewDateAddMinutes(date,120),getNewDateAddMinutes(date,180)));
		dateVoList.add(new DateVo(getNewDateAddMinutes(date,200),getNewDateAddMinutes(date,240)));

		Date startDate = getNewDateAddMinutes(date,-2);
		Date endDate = getNewDateAddMinutes(date,242);

		List<DateVo> dateVoList1 = removeRedundancyDate(dateVoList, startDate, endDate);


		for (DateVo v:dateVoList) {
			System.out.println(format(v.getStartDate())+" 至 "+format(v.getEndDate()));
		}

		System.out.println("=================================================================");
		for (DateVo v:dateVoList1) {
			System.out.println(format(v.getStartDate())+" 至 "+format(v.getEndDate()));
		}


	}


	/**
	 * 去除冗余的时间
	 * @param dateList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<DateVo> removeRedundancyDate(List<DateVo> dateList, Date startDate, Date endDate){
		List<DateVo> dateVoList = new ArrayList<>();
		if (isNull(startDate,endDate) || Func.isEmpty(dateList) || dateList.size()<1){
			dateVoList.add(new DateVo(startDate,endDate));
			return dateVoList;
		}

		dateList = dateList.stream().sorted(Comparator.comparing(DateVo::getStartDate)).collect(Collectors.toList());

		int diffMillisecond = 1000;

		for (int i = 0; i <dateList.size() ; i++) {
			if (isNull(dateList.get(i).getStartDate(), dateList.get(i).getEndDate())) {
				continue;
			}

			if (i==0) {
				if (startDate.getTime() < dateList.get(0).getStartDate().getTime()) {
					if (endDate.getTime() <= dateList.get(0).getStartDate().getTime()) {
						if (endDate.getTime() - startDate.getTime() > diffMillisecond) {
							dateVoList.add(new DateVo(startDate, endDate));
						}
					} else {
						if (dateList.get(0).getStartDate().getTime() - startDate.getTime() > diffMillisecond) {
							dateVoList.add(new DateVo(startDate, dateList.get(0).getStartDate()));
						}
					}
				}
			}

			if (i>0 && i<dateList.size()-1){
				if (startDate.getTime()<=dateList.get(i).getEndDate().getTime()){
					if (endDate.getTime()<=dateList.get(i+1).getStartDate().getTime()){
						if (endDate.getTime()-dateList.get(i).getEndDate().getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(dateList.get(i).getEndDate(), endDate));
						}
					}else {
						if (dateList.get(i+1).getStartDate().getTime()-dateList.get(i).getEndDate().getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(dateList.get(i).getEndDate(), dateList.get(i + 1).getStartDate()));
						}
					}

				}else {
					if (endDate.getTime()<=dateList.get(i+1).getStartDate().getTime()){
						if (endDate.getTime()-startDate.getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(startDate, endDate));
						}
					}else {
						if (dateList.get(i+1).getStartDate().getTime()-startDate.getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(startDate, dateList.get(i + 1).getStartDate()));
						}
					}
				}
			}

			if (i==dateList.size()-1){
				if (endDate.getTime()>dateList.get(dateList.size()-1).getEndDate().getTime()){
					if (startDate.getTime()<dateList.get(dateList.size()-1).getEndDate().getTime()){
						if (endDate.getTime()-dateList.get(dateList.size()-1).getEndDate().getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(dateList.get(dateList.size() - 1).getEndDate(), endDate));
						}
					}else {
						if (endDate.getTime()-startDate.getTime()>diffMillisecond) {
							dateVoList.add(new DateVo(startDate, endDate));
						}
					}
				}
			}
		}

		return dateVoList;
	}


}

