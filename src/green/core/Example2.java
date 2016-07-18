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
		
		//86400s = 1��
		while (scheduler.getCurrentTime() < 86400) {
			//�ȳ�ʼ��ÿ��slot�Ļ�������
			Helper.initParameters(scheduler);
			//���и�slot�ĵ���
			scheduler.schedule();
		}
		
		//������Task��ƽ��ִ��ʱ��
		int total = 0;
		int count = 0;
		for(int i : Helper.getWaitTaskTime()) {
			total += i;
			count++;
		}
		float avgTime = total / count;
		//��ӡ�����Ϣ
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
		//���ظ�����ͼ���ܺ���ͼ
		Charts demo = new Charts("Load&Energy View(OptimizationScheduler)",scheduler.getSlotNewTaskList(),
				scheduler.getDatacenter().getSlotFinishedTaskNumList(),
				scheduler.getSlotEnergy(),scheduler.getSlotGreenEnergy(),
				scheduler.getSlotPrice(),scheduler.getPriceList());
		demo.pack();
		RefineryUtilities.positionFrameRandomly(demo);
		demo.setVisible(true);
	}

}
