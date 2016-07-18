package green.core;

import green.util.Log;

public class SchedulerBasic extends SchedulerPolicyAbstract {

	public SchedulerBasic(Datacenter datacenter) {
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
	    
	    float slotPowerConsume = getDatacenter().calSlotPowerConsumeWithoutSleep();
	    getSlotEnergy().add(slotPowerConsume);
	    getDatacenter().updateHostList();    
	    
	    float slotPrice = calSlotPrice(slotPowerConsume);
	    getSlotPrice().add(slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() +
	    		" currentPowerConsume��" + slotPowerConsume
	    		+ "kWh currentSlotGreenPower:" + getGreenPowerNum() + "kWh Price in this slot is:" + slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() + " currentTotalFinishedTask:" + getDatacenter().calFinishedTaskNum()
	   			+ " Electric price:" + getPrice() + " slotNewTaskNum:" + 
	   			 getSlotNewTask() + " OnHostNum:" + getDatacenter().getOnHostNum() + "\n" );
	   	
	
	}
}
