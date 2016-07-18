package green.core;

import green.util.Charts;
import green.util.Helper;
import green.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import org.jfree.ui.RefineryUtilities;
public class Example2 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Datacenter datacenter = new Datacenter();
		OpScheduler scheduler = new OpScheduler(datacenter);

		// Helper.initLogOutput("d:/logs", "schedulePolicy");
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
		Charts demo = new Charts("Load&Energy View(OptimizationScheduler)",scheduler.getSlotNewTaskList(),
				scheduler.getDatacenter().getSlotFinishedTaskNumList(),
				scheduler.getSlotEnergy(),scheduler.getSlotGreenEnergy(),
				scheduler.getSlotPrice(),scheduler.getPriceList());
		demo.pack();
		RefineryUtilities.positionFrameRandomly(demo);
		demo.setVisible(true);
	}

}
