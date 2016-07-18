package green.core;

import java.util.ArrayList;
import java.util.List;

public class Datacenter {
	
	private int totalHostNum;
	private int onHostNum = 0;
	private List<Host> hostList = new ArrayList<Host>();
	//该数据结构用于存储每个slot内完成的Task数量
	private List<Integer> slotFinishedTaskNumList = new ArrayList<Integer>();  
	public List<Host> getHostList() {
		return hostList;
	}

	public void setHostList(List<Host> hostList) {
		this.hostList = hostList;
	}
	//在Datacenter的构造函数中创建所有的host
	public Datacenter() {
		  totalHostNum = Constant.HOST_NUM;
		  for(int i=0;i < totalHostNum;i++) {
			  hostList.add(new Host());
		  }
	  }
	  
	  public void updateHostList() {
		  int num = 0;
		  for(int i=0;i < hostList.size();i++) {
			  Host host = hostList.get(i);
			  num += host.updateVmProcessing();//return this host's finished Task
			  
		  }
		  //slotFinishedTaskNumList内存储了每个slot完成的Task数目
		  slotFinishedTaskNumList.add(num);
	  }

	  //计算datacenter在该slot的能耗
	  public float calSlotPowerConsume() {
		  float datacenterPower = 0;
		  setOnHostNum(0);
		  for(int i=0;i < hostList.size();i++) {
			  Host host = hostList.get(i);
			  //计算每个Host在该slot内的能耗
			  datacenterPower += host.getPower();
			  if(host.getPower() != 0) {
				  setOnHostNum(getOnHostNum() + 1);
			  }
		  }
//		  System.out.println("onHostNum:" + onHostNum + "\n");
		  return datacenterPower;
	  }
	  
	  
	  //计算datacenter在该slot的能耗,host不休眠
	  public float calSlotPowerConsumeWithoutSleep() {
		  float datacenterPower = 0;
		  setOnHostNum(0);
		  for(int i=0;i < hostList.size();i++) {
			  Host host = hostList.get(i);
			  //计算每个Host在该slot内的能耗
			  datacenterPower += host.getPower();
			  if(host.getPower() == 0 && host.getShutdownTimes() > 0) {
				  datacenterPower += Constant.STATIC_POWER;
			  }
			  if(host.getShutdownTimes() > 0 || host.getPower() != 0) {
				  setOnHostNum(getOnHostNum() + 1);
			  }
		  }
//		  System.out.println("onHostNum:" + onHostNum + "\n");
		  return datacenterPower;
	  }
	  



	//计算整个仿真时段内Host关机的次数
	public int calTotalShutdownTime() {
		// TODO Auto-generated method stub
		int totalShutdownTimes = 0;
		for(int i=0;i < hostList.size();i++) {
			//获取每个host在仿真时段内的关机次数
			totalShutdownTimes += hostList.get(i).getShutdownTimes();
		}
		return totalShutdownTimes;
	}

	public List<Integer> getSlotFinishedTaskNumList() {
		return slotFinishedTaskNumList;
	}

	public void setSlotFinishedTaskNumList(List<Integer> slotFinishedTaskNumList) {
		this.slotFinishedTaskNumList = slotFinishedTaskNumList;
	}
	//用于计算整个仿真过程完成的Task总数
	public int calFinishedTaskNum() {
		// TODO Auto-generated method stub
		int num = 0;
		for(int i : slotFinishedTaskNumList) {
			num += i;
		}
		return num;
	}

	public int getOnHostNum() {
		return onHostNum;
	}

	public void setOnHostNum(int onHostNum) {
		this.onHostNum = onHostNum;
	}

}
