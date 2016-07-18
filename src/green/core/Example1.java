package green.core;


import green.util.CSVFileUtil;
import green.util.Charts;
import green.util.Helper;
import green.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import org.jfree.ui.RefineryUtilities;

/**
 * 假设： 1 Host上每个Vm都一样 2 所有host都一样 3 host不跑任务就立即关机，来任务就立即开机 4 每个Task运行所需时间可调 5
 * 调度算法默认15分钟执行一次(可调) 6 Datacenter默认有100台host(可调)
 * 输入：每个调度时刻的电价(随机)，绿色能源大小(随机)，可延迟任务个数(随机)，不可延迟任务个数(随机)
 * 输出：日志信息(总电费(元)，总能耗，完成的Task总数，关闭Host的次数
 * 
 * 调度算法总原则：绿色能源多，电价低，则多做不可延迟任务；绿色能源少，电价贵，少做不可延迟任务
 * 
 * @author rqf
 * 
 */
public class Example1 {
	public static void main(String[] args) throws Exception {

		Datacenter datacenter = new Datacenter();
//		SchedulerSimple scheduler = new SchedulerSimple(datacenter);
		SchedulerBasic scheduler = new SchedulerBasic(datacenter);

//		Helper.initLogOutput("d:/logs", "schedulePolicy");
		Helper.readCSVData();   	
		//86400s = 1天
		while (scheduler.getCurrentTime() < 86400) {
			//先初始化每个slot的环境参数
			Helper.initParameters(scheduler);
			//进行该slot的调度
			scheduler.schedule();
		}
		//求所有Task的平均执行时间
		int total = 0;
		int count = 0;
		for(int i : Helper.getWaitTaskTime()) {
			total += i;
			count++;
		}
		float avgTime = total / count;
		//打印输出信息
		Log.printLine("============OUTPUT=========");
		Log.printLine("Total simulation time:" + scheduler.getCurrentTime()
				+ " sec");
		Log.printLine("Total ShutdownTimes:"
				+ scheduler.getDatacenter().calTotalShutdownTime());
		Log.printLine("Total Price:" + scheduler.calTotalPrice() + " cents");
		Log.printLine("Total EnergyConsume:" + scheduler.calTotalEnergy()
				+ " kWh");
		Log.printLine("Total Input Task Num:"
				+ scheduler.calTotalInputTaskNum());
		Log.printLine("Average Task Running Time:" + avgTime + " s");
		//加载负载视图和能耗视图
		Charts demo = new Charts("Load&Energy View",scheduler.getSlotNewTaskList(),
				scheduler.getDatacenter().getSlotFinishedTaskNumList(),
				scheduler.getSlotEnergy(),scheduler.getSlotGreenEnergy(),
				scheduler.getSlotPrice(),scheduler.getPriceList());
		demo.pack();
		RefineryUtilities.positionFrameRandomly(demo);
		demo.setVisible(true);
	}

}
