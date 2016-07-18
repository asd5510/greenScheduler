package green.core;

import green.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SchedulerPolicyAbstract {
	//待调度的可延迟Task队列
	private List<Task> delayTaskList = new ArrayList<Task>();
	//待调度的不可延迟Task队列
	private List<Task> undelayTaskList = new ArrayList<Task>();
	//当前slot要调度的Task队列，*unused
	private List<Task> totalDelayTaskList=new ArrayList<Task>();//可延迟负载队列D(t)
	
	private List<Task> scheduleTaskList = new ArrayList<Task>();
	private List<Task> finishedTaskList = new ArrayList<Task>();
	//记录整个仿真周期内电价的走势
	private List<Float> priceList = new ArrayList<Float>();
	//记录仿真周期内每个slot所花费的电费
	private List<Float> slotPrice = new ArrayList<Float>();
	//记录仿真周期内每个slot的能耗
	private List<Float> slotEnergy = new ArrayList<Float>();
	//记录仿真周期内每个slot产生的绿色能源数量
	private List<Float> slotGreenEnergy = new ArrayList<Float>();
	//记录仿真周期内每个slot新生成的Task数目
	private List<Integer> slotNewTaskList = new ArrayList<Integer>();
	private float price;
	private float greenPowerNum; //kWh
	private Datacenter datacenter = null;
//	private float slotPrice = 0;
//	private float totalPrice = 0;
	private static int currentTime = 0;
	private int slotNewTask = 0;
	
	public List<Task> getFinishedTaskList() {
		return finishedTaskList;
	}

	public void setFinishedTaskList(List<Task> finishedTaskList) {
		this.finishedTaskList = finishedTaskList;
	}

	public List<Task> getScheduleTaskList() {
		return scheduleTaskList;
	}

	public void setScheduleTaskList(List<Task> scheduleTaskList) {
		this.scheduleTaskList = scheduleTaskList;
	}

	
	public List<Task> getDelayTaskList() {
		return delayTaskList;
	}

	public void setDelayTaskList(List<Task> delayTaskList) {
		this.delayTaskList = delayTaskList;
	}

	public List<Task> getUndelayTaskList() {
		return undelayTaskList;
	}

	public void setUndelayTaskList(List<Task> undelayTaskList) {
		this.undelayTaskList = undelayTaskList;
	}
	public void setTotalDelayTaskList(List<Task> totalDelayTaskList) {
		this.totalDelayTaskList = totalDelayTaskList;
	}
	public List<Task> getTotalDelayTaskList() {
		return totalDelayTaskList;
	}

	  
	public SchedulerPolicyAbstract(Datacenter datacenter) {
		this.datacenter = datacenter;
	}
	//待实现的调度算法，具体要算出每个slot要做多少不可延迟Task  
	public abstract void schedule();
	  

	public static int getCurrentTime() {
	
		return currentTime;
	}
	
	
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}


	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getGreenPowerNum() {
		return greenPowerNum;
	}

	public void setGreenPowerNum(float greenPowerNum) {
		this.greenPowerNum = greenPowerNum;
	}

	public Datacenter getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(Datacenter datacenter) {
		this.datacenter = datacenter;
	}

	
	public float calSlotPrice(float SlotPowerConsume) {
		if(SlotPowerConsume > getGreenPowerNum())
			return getPrice() * (SlotPowerConsume - getGreenPowerNum());
		else 
			return 0;
	}

	//将Task分配给Vm
	public void bindTaskToVm() {
		int scheduleTaskNum = scheduleTaskList.size();
		
		List<Host> list = datacenter.getHostList();
		//遍历host列表，如果某个host is available，则给该host分配Task
		for(int i=0;i < list.size();i++) {
			Host host = list.get(i);
			while(host.isAvailable() && scheduleTaskNum > 0) {
				
				scheduleTaskNum--;
				
				host.bindTaskToVm(scheduleTaskList.remove(scheduleTaskNum));		
			}
		}		
		//所有host都满载，仍有待调度的Task
		if(scheduleTaskNum > 0) {
			Log.printLine("The number still in scheduleTask waiting for scheduling:" +
		 scheduleTaskNum);
		}
	}

	public int getSlotNewTask() {
		return slotNewTask;
	}

	public void setSlotNewTask(int slotNewTask) {
		this.slotNewTask = slotNewTask;
	}

	public List<Float> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Float> priceList) {
		this.priceList = priceList;
	}
	//计算总的电费
	public float calTotalPrice() {
		float num = 0;
		for(float i : slotPrice) {
			num += i;
		}
		return num;
	}
	

	public List<Integer> getSlotNewTaskList() {
		return slotNewTaskList;
	}

	public void setSlotNewTaskList(List<Integer> slotNewTaskList) {
		this.slotNewTaskList = slotNewTaskList;//每个slot新生成的task的数目
	}

	public int calTotalInputTaskNum() {
		// TODO Auto-generated method stub
		int num = 0;
		for(int i : slotNewTaskList) {
			num += i;
		}
		return num;
	}

	public List<Float> getSlotEnergy() {
		return slotEnergy;
	}

	public void setSlotEnergy(List<Float> slotEnergy) {
		this.slotEnergy = slotEnergy;
	}

	public float calTotalEnergy() {
		// TODO Auto-generated method stub
		float num = 0;
		for(float i : slotEnergy) {
			num += i;
		}
		return num;
	}

	public List<Float> getSlotGreenEnergy() {
		return slotGreenEnergy;
	}

	public void setSlotGreenEnergy(List<Float> slotGreenEnergy) {
		this.slotGreenEnergy = slotGreenEnergy;
	}

	public List<Float> getSlotPrice() {
		return slotPrice;
	}

	public void setSlotPrice(List<Float> slotPrice) {
		this.slotPrice = slotPrice;
	}
	/*public float getTotalPrice() {
		return this.totalPrice;
	}
	
	public float getSlotPrice() {
		return slotPrice;
	}

	public void setSlotPrice(float slotPrice) {
		this.slotPrice = slotPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}*/
	  
	  
}
