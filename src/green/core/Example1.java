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
 * ���裺 1 Host��ÿ��Vm��һ�� 2 ����host��һ�� 3 host��������������ػ������������������ 4 ÿ��Task��������ʱ��ɵ� 5
 * �����㷨Ĭ��15����ִ��һ��(�ɵ�) 6 DatacenterĬ����100̨host(�ɵ�)
 * ���룺ÿ������ʱ�̵ĵ��(���)����ɫ��Դ��С(���)�����ӳ��������(���)�������ӳ��������(���)
 * �������־��Ϣ(�ܵ��(Ԫ)�����ܺģ���ɵ�Task�������ر�Host�Ĵ���
 * 
 * �����㷨��ԭ����ɫ��Դ�࣬��۵ͣ�����������ӳ�������ɫ��Դ�٣���۹����������ӳ�����
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
		Charts demo = new Charts("Load&Energy View",scheduler.getSlotNewTaskList(),
				scheduler.getDatacenter().getSlotFinishedTaskNumList(),
				scheduler.getSlotEnergy(),scheduler.getSlotGreenEnergy(),
				scheduler.getSlotPrice(),scheduler.getPriceList());
		demo.pack();
		RefineryUtilities.positionFrameRandomly(demo);
		demo.setVisible(true);
	}

}
