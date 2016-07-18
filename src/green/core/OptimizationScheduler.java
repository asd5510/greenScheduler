package green.core;

import green.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.lang.Math;
public class OptimizationScheduler extends SchedulerPolicyAbstract{
	private int V=500;
	//所有的机器全开能处理的task的数目
	private int AllTaskNum=(int) (Constant.HOST_NUM*Constant.VM_NUM*Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME));
	//每台机器开的时候在一个slot消耗的能量
	private float EveryHostConsume=Constant.PEAK_POWER;
	//每个host的每个slot能处理的task的个数
	private int EverySlotTasknum=(int) (Constant.VM_NUM*Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME));
   /* 考虑绿色能源	
    * //处理1个task需要的能耗
	private float EveryTaskConsume=(Constant.PEAK_POWER-Constant.STATIC_POWER)/EverySlotTasknum;
    private int GreenTaskNum=(int) (getGreenPowerNum()/EveryTaskConsume);//绿色能源能处理的task的个数
    //总共需要开多少台机器
	private int OnHostNum=(int) Math.ceil((getScheduleTaskList().size()-GreenTaskNum)/EverySlotTasknum);*/
	public OptimizationScheduler(Datacenter datacenter) {
		super(datacenter);// TODO Auto-generated constructor stub
	}
    public void schedule() {
		
    	setCurrentTime(getCurrentTime() + 900);
	    /*//计算u(t)即ScheduleTaskNum取多大值时使min最小
		int min=(int) (V*getPrice()*EveryHostConsume*OnHostNum
	    		-getScheduleTaskList().size()*getTotalDelayTaskList().size());*/
        int a=(int) (V*getPrice()*EveryHostConsume/EverySlotTasknum-getTotalDelayTaskList().size());
        if(a>=0) {
    	    getScheduleTaskList().addAll(getUndelayTaskList());//调度该时间槽不可延迟的task
    	    getTotalDelayTaskList().addAll(getDelayTaskList());//把本次未完成的可延迟的task保存在TotalDelayTaskList中
        } else {
        	int m=getUndelayTaskList().size();
        	int n=getUndelayTaskList().size()+getTotalDelayTaskList().size();
        	int k=getUndelayTaskList().size()+getTotalDelayTaskList().size()+getDelayTaskList().size();
        	//根据机器全开能做的task个数来调度任务
        	if(AllTaskNum>=k){
        		getScheduleTaskList().addAll(getUndelayTaskList());//调度该时间槽不可延迟的task
        	    getScheduleTaskList().addAll(getTotalDelayTaskList());//调度之前没有被完成的可延迟的task
        	    getScheduleTaskList().addAll(getDelayTaskList());//调度本次产生的可延迟的task
        	    getTotalDelayTaskList().clear();
        	}
        	else if(AllTaskNum<k&&AllTaskNum>=n){
        		getScheduleTaskList().addAll(getUndelayTaskList());//调度该时间槽不可延迟的task
        	    getScheduleTaskList().addAll(getTotalDelayTaskList());//调度之前没有被完成的可延迟的task
        	    getTotalDelayTaskList().clear();
        	    int x=AllTaskNum-n;//getDelayTaskList中被调度的task的个数
        	    for(int i=0;i<x;i++){
        	    	getScheduleTaskList().add(getDelayTaskList().get(i));
        	    }
        	    getDelayTaskList().remove(x);
        	    getTotalDelayTaskList().addAll(getDelayTaskList());//把本次未完成的可延迟的task保存在TotalDelayTaskList中
        	}
        	else if(AllTaskNum<n&&AllTaskNum>m){
        		getScheduleTaskList().addAll(getUndelayTaskList());//调度该时间槽不可延迟的task
        		int y=AllTaskNum-m;//getTotalDelayTaskList中被调度的个数
        		for(int i=0;i<y;i++){
        	    	getScheduleTaskList().add(getTotalDelayTaskList().get(i));
        	    }
        		getTotalDelayTaskList().remove(y);
        		getTotalDelayTaskList().addAll(getDelayTaskList());//把本次未完成的可延迟的task保存在TotalDelayTaskList中	
        	}
        }
        Log.printLine("u(t):"+getScheduleTaskList().size()+"\n" +"getTotalDelayTaskList():"+getTotalDelayTaskList().size());
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
	    		" currentPowerConsume：" + slotPowerConsume
	    		+ "kWh currentSlotGreenPower:" + getGreenPowerNum() + "kWh Price in this slot is:" + slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() + " currentTotalFinishedTask:" + getDatacenter().calFinishedTaskNum()
	   			+ " Electric price:" + getPrice() + " slotNewTaskNum:" + 
	   			 getSlotNewTask() + "\n" );
	   	
    }
}
