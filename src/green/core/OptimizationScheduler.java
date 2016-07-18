package green.core;

import green.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.lang.Math;
public class OptimizationScheduler extends SchedulerPolicyAbstract{
	private int V=500;
	//���еĻ���ȫ���ܴ����task����Ŀ
	private int AllTaskNum=(int) (Constant.HOST_NUM*Constant.VM_NUM*Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME));
	//ÿ̨��������ʱ����һ��slot���ĵ�����
	private float EveryHostConsume=Constant.PEAK_POWER;
	//ÿ��host��ÿ��slot�ܴ����task�ĸ���
	private int EverySlotTasknum=(int) (Constant.VM_NUM*Math.floor(Constant.SCHEDULE_INTERNAL/Constant.TASK_TIME));
   /* ������ɫ��Դ	
    * //����1��task��Ҫ���ܺ�
	private float EveryTaskConsume=(Constant.PEAK_POWER-Constant.STATIC_POWER)/EverySlotTasknum;
    private int GreenTaskNum=(int) (getGreenPowerNum()/EveryTaskConsume);//��ɫ��Դ�ܴ����task�ĸ���
    //�ܹ���Ҫ������̨����
	private int OnHostNum=(int) Math.ceil((getScheduleTaskList().size()-GreenTaskNum)/EverySlotTasknum);*/
	public OptimizationScheduler(Datacenter datacenter) {
		super(datacenter);// TODO Auto-generated constructor stub
	}
    public void schedule() {
		
    	setCurrentTime(getCurrentTime() + 900);
	    /*//����u(t)��ScheduleTaskNumȡ���ֵʱʹmin��С
		int min=(int) (V*getPrice()*EveryHostConsume*OnHostNum
	    		-getScheduleTaskList().size()*getTotalDelayTaskList().size());*/
        int a=(int) (V*getPrice()*EveryHostConsume/EverySlotTasknum-getTotalDelayTaskList().size());
        if(a>=0) {
    	    getScheduleTaskList().addAll(getUndelayTaskList());//���ȸ�ʱ��۲����ӳٵ�task
    	    getTotalDelayTaskList().addAll(getDelayTaskList());//�ѱ���δ��ɵĿ��ӳٵ�task������TotalDelayTaskList��
        } else {
        	int m=getUndelayTaskList().size();
        	int n=getUndelayTaskList().size()+getTotalDelayTaskList().size();
        	int k=getUndelayTaskList().size()+getTotalDelayTaskList().size()+getDelayTaskList().size();
        	//���ݻ���ȫ��������task��������������
        	if(AllTaskNum>=k){
        		getScheduleTaskList().addAll(getUndelayTaskList());//���ȸ�ʱ��۲����ӳٵ�task
        	    getScheduleTaskList().addAll(getTotalDelayTaskList());//����֮ǰû�б���ɵĿ��ӳٵ�task
        	    getScheduleTaskList().addAll(getDelayTaskList());//���ȱ��β����Ŀ��ӳٵ�task
        	    getTotalDelayTaskList().clear();
        	}
        	else if(AllTaskNum<k&&AllTaskNum>=n){
        		getScheduleTaskList().addAll(getUndelayTaskList());//���ȸ�ʱ��۲����ӳٵ�task
        	    getScheduleTaskList().addAll(getTotalDelayTaskList());//����֮ǰû�б���ɵĿ��ӳٵ�task
        	    getTotalDelayTaskList().clear();
        	    int x=AllTaskNum-n;//getDelayTaskList�б����ȵ�task�ĸ���
        	    for(int i=0;i<x;i++){
        	    	getScheduleTaskList().add(getDelayTaskList().get(i));
        	    }
        	    getDelayTaskList().remove(x);
        	    getTotalDelayTaskList().addAll(getDelayTaskList());//�ѱ���δ��ɵĿ��ӳٵ�task������TotalDelayTaskList��
        	}
        	else if(AllTaskNum<n&&AllTaskNum>m){
        		getScheduleTaskList().addAll(getUndelayTaskList());//���ȸ�ʱ��۲����ӳٵ�task
        		int y=AllTaskNum-m;//getTotalDelayTaskList�б����ȵĸ���
        		for(int i=0;i<y;i++){
        	    	getScheduleTaskList().add(getTotalDelayTaskList().get(i));
        	    }
        		getTotalDelayTaskList().remove(y);
        		getTotalDelayTaskList().addAll(getDelayTaskList());//�ѱ���δ��ɵĿ��ӳٵ�task������TotalDelayTaskList��	
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
	    		" currentPowerConsume��" + slotPowerConsume
	    		+ "kWh currentSlotGreenPower:" + getGreenPowerNum() + "kWh Price in this slot is:" + slotPrice);
	    
	   	Log.printLine("CurrentTime:" + getCurrentTime() + " currentTotalFinishedTask:" + getDatacenter().calFinishedTaskNum()
	   			+ " Electric price:" + getPrice() + " slotNewTaskNum:" + 
	   			 getSlotNewTask() + "\n" );
	   	
    }
}
