package green.core;

import green.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchedulerSimple extends SchedulerPolicyAbstract {
	

	public SchedulerSimple(Datacenter datacenter) {
		super(datacenter);
		// TODO Auto-generated constructor stub
	}

	public void schedule() {
		
		setCurrentTime(getCurrentTime() + 900);
		
		
	    //���㱾��Ҫ���ȵ�Task��Ŀ����ǰ�ǽ����д����ȵ�Taskȫ������
	    getScheduleTaskList().addAll(getUndelayTaskList());
	    getScheduleTaskList().addAll(getDelayTaskList());
	    
	    getUndelayTaskList().clear();
	    getDelayTaskList().clear();
	    
	    getSlotGreenEnergy().add(getGreenPowerNum());
	    
	    getSlotNewTaskList().add(getSlotNewTask());
	    bindTaskToVm();
	    
	    float slotPowerConsume = getDatacenter().calSlotPowerConsume();
	    getSlotEnergy().add(slotPowerConsume);
	    getDatacenter().updateHostList();    
	    
	    float slotPrice = calSlotPrice(slotPowerConsume);
	    getSlotPrice().add(slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() +
	    		" currentPowerConsume��" + slotPowerConsume
	    		+ "kWh currentSlotGreenPower:" + getGreenPowerNum() + "kWh Price in this slot is:" + slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() + " currentTotalFinishedTask:" + getDatacenter().calFinishedTaskNum()
	   			+ " Electric price:" + getPrice() + " slotNewTaskNum:" + 
	   			 getSlotNewTask() + "\n" );
	   	
	
	}

}
