package green.util;

import green.core.Constant;
import green.core.SchedulerPolicyAbstract;
import green.core.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper {
	
	private static int UndelayTaskNum = 140; 
	private static int delayTaskNum = 180;
	private static float price = 0.88f; 
	private static float greenPowerNum = 2.9f;  
	//保存每个Task的平均执行时间
	private static List<Integer> waitTaskTime = new ArrayList<Integer>(); 
	
	public static void readCSVData() throws Exception {
		//读入电价数据
		CSVFileUtil cf = new CSVFileUtil("C:\\Users\\rqf\\Downloads\\greenAWH\\data\\Houston_May2009.csv");
    	cf.readLine();
        CSVFileUtil.priceData.addAll(cf.fromCSVLinetoArray(cf.readLine()));
        //读绿色能源数据
/*		CSVFileUtil cf3 = new CSVFileUtil("C:\\Users\\rqf\\Downloads\\greenAWH\\data\\green.csv");
        CSVFileUtil.greenData.addAll(cf3.fromCSVLinetoArray(cf3.readLine()));*/
        //读Task数据
		CSVFileUtil cf2 = new CSVFileUtil("C:\\Users\\rqf\\Downloads\\greenAWH\\data\\task.csv");
        CSVFileUtil.taskData.addAll(cf2.fromCSVLinetoArray(cf2.readLine()));
        CSVFileUtil.UtaskData.addAll(cf2.fromCSVLinetoArray(cf2.readLine()));
/*		for(int i=0;i<96;i++) {
			CSVFileUtil.taskData.add("5000");
			CSVFileUtil.UtaskData.add("5000");
		}*/
	}
	//进行参数初始化
	public static void initParameters(SchedulerPolicyAbstract scheduler) {
//		price = (float) (price + Math.random() / 5 - 0.1);
//		price = getPossionVariable(10) / 10f;
		int currentTime = scheduler.getCurrentTime();
		price = Float.parseFloat(CSVFileUtil.priceData.get(currentTime / Constant.SCHEDULE_INTERNAL + 3));
/*		if(price > 3f)
			price = 3f;
		if(price < 0.1f)
			price = 0.1f;
		greenPowerNum = (float) (greenPowerNum + Math.random() / 4 - 0.125);*/
		greenPowerNum = getPossionVariable(30) / 10f;
//		greenPowerNum = Float.parseFloat(CSVFileUtil.greenData.get(currentTime / Constant.SCHEDULE_INTERNAL / 4)) / 60f;
		scheduler.getPriceList().add(price);
		scheduler.setGreenPowerNum(greenPowerNum);/*(float) (2.9f + Math.random())*/; 
		scheduler.setPrice(price);	
	   
		
/*		int core = (int)(-7 + Math.random()*15);
		UndelayTaskNum = (int) (UndelayTaskNum + Math.random() * core);
		core = (int)(-7 + Math.random()*15);
		delayTaskNum = (int) (UndelayTaskNum + Math.random() * core);*/
/*		UndelayTaskNum = getPossionVariable(140);
		delayTaskNum = getPossionVariable(180);*/
		UndelayTaskNum = Integer.parseInt(CSVFileUtil.UtaskData.get(currentTime / Constant.SCHEDULE_INTERNAL / 4)) / 40 + getPossionVariable(60);
		delayTaskNum = Integer.parseInt(CSVFileUtil.taskData.get(currentTime / Constant.SCHEDULE_INTERNAL / 4)) / 40 + getPossionVariable(60);

	    scheduler.setSlotNewTask(delayTaskNum + UndelayTaskNum);
	    for(int i=0;i < UndelayTaskNum;i++) {
	    	Task task = new Task();
	    	scheduler.getUndelayTaskList().add(task);
	    }
	    
	    for(int i=0;i < delayTaskNum;i++) {
	    	Task task = new Task();
	    	scheduler.getDelayTaskList().add(task);
	    }
		
	}
	//启用日志输出
	public static void initLogOutput(String outputFolder,String schedulePolicy) throws IOException{
		
		Log.enable();
		
		File folder = new File(outputFolder);
		if(!folder.exists()) {
			folder.mkdir();
		}
		File folder2 = new File(outputFolder + "/log");
		if(!folder2.exists()) {
			folder2.mkdir();
		}
		
		File file = new File(outputFolder + "/log/"
				+ getExperimentName(schedulePolicy) + ".txt");
		file.createNewFile();
		Log.setOutput(new FileOutputStream(file));
	}
	
	private static String getExperimentName(String... args) {
		StringBuilder experimentName = new StringBuilder();
		for (int i=0;i< args.length;i++) {
			if(args[i].isEmpty()) {
				continue;
			}
			if(i != 0) {
				experimentName.append("_");
			}
			experimentName.append(args[i]);
		}
		
		return experimentName.toString();
	}

	public static List<Integer> getWaitTaskTime() {
		return waitTaskTime;
	}

	public static void setWaitTaskTime(List<Integer> waitTaskTime) {
		Helper.waitTaskTime = waitTaskTime;
	}
	//模拟泊松过程，lamda为中心值
	private static int getPossionVariable(double lamda) {
		int x = 0;
		double y = Math.random(), cdf = getPossionProbability(x, lamda);
		while (cdf < y) {
			x++;
			cdf += getPossionProbability(x, lamda);
		}
		return x;
	}

	private static double getPossionProbability(int k, double lamda) {
		double c = Math.exp(-lamda), sum = 1;
		for (int i = 1; i <= k; i++) {
			sum *= lamda / i;
		}
		return sum * c;
	}
}
