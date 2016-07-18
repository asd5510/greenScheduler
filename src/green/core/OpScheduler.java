package green.core;

import green.util.Log;

public class OpScheduler extends SchedulerPolicyAbstract {

	private float MEAN= 25.0f;
	//���еĻ���ȫ���ܴ����task����Ŀ
	private int FULL_LOAD =(int) (Constant.HOST_NUM*Constant.VM_NUM*Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME));
	//ÿ��host��ÿ��slot�ܴ����task�ĸ���
	private float TASK_POWER = (float) (Constant.PEAK_POWER * Constant.SCHEDULE_INTERNAL / 3600.0f / (Constant.VM_NUM * Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME)));
	
	public OpScheduler(Datacenter datacenter) {
		super(datacenter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void schedule() {
		// TODO Auto-generated method stub
		setCurrentTime(getCurrentTime() + 900);
		
		int delayTaskNum = /*(int)(getGreenPowerNum() / TASK_POWER - getUndelayTaskList().size())*/(int)(getDelayTaskList().size()* 0.5f - 0.2f * Math.pow((getPrice()-25.0f),3));
//		int delayTaskNum = /*(int)(getGreenPowerNum() / TASK_POWER - getUndelayTaskList().size())*/(int)(getDelayTaskList().size()* 0.7f - 10 * (getPrice()-MEAN));

		//���㱾��Ҫ���ȵ�Task��Ŀ����ǰ�ǽ����д����ȵ�Taskȫ������
		if (delayTaskNum < 0 )
			delayTaskNum = 0;
	    getScheduleTaskList().addAll(getUndelayTaskList());
	    if(delayTaskNum + getScheduleTaskList().size() < (int)(getGreenPowerNum() / TASK_POWER)) {
	    	delayTaskNum = (int)(getGreenPowerNum() / TASK_POWER) - getScheduleTaskList().size();
	    }
	    
	    if(delayTaskNum > getDelayTaskList().size()) {
	    	delayTaskNum = getDelayTaskList().size();
	    }
	    if (getCurrentTime() != 86400) {
	    	for(int i=0;i<delayTaskNum;i++) {
	    		getScheduleTaskList().add(getDelayTaskList().remove(i));
	    		i--;
	    		delayTaskNum--;
	    	}
	    } else {
	    	getScheduleTaskList().addAll(getDelayTaskList());
	    	getDelayTaskList().clear();
	    }

	    
	    getUndelayTaskList().clear();
//	    getDelayTaskList().clear();
	    
	    
	    getSlotGreenEnergy().add(getGreenPowerNum());
	    
	    getSlotNewTaskList().add(getSlotNewTask());
	    bindTaskToVm();
	    
	    float slotPowerConsume = getDatacenter().calSlotPowerConsume();
	    if(slotPowerConsume < getGreenPowerNum()) {
	    	System.out.println("");
	    }
	    getSlotEnergy().add(slotPowerConsume);
	    //����Host״̬
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
