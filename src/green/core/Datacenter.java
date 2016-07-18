package green.core;

import java.util.ArrayList;
import java.util.List;

public class Datacenter {
	
	private int totalHostNum;
	private int onHostNum = 0;
	private List<Host> hostList = new ArrayList<Host>();
	//�����ݽṹ���ڴ洢ÿ��slot����ɵ�Task����
	private List<Integer> slotFinishedTaskNumList = new ArrayList<Integer>();  
	public List<Host> getHostList() {
		return hostList;
	}

	public void setHostList(List<Host> hostList) {
		this.hostList = hostList;
	}
	//��Datacenter�Ĺ��캯���д������е�host
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
		  //slotFinishedTaskNumList�ڴ洢��ÿ��slot��ɵ�Task��Ŀ
		  slotFinishedTaskNumList.add(num);
	  }

	  //����datacenter�ڸ�slot���ܺ�
	  public float calSlotPowerConsume() {
		  float datacenterPower = 0;
		  setOnHostNum(0);
		  for(int i=0;i < hostList.size();i++) {
			  Host host = hostList.get(i);
			  //����ÿ��Host�ڸ�slot�ڵ��ܺ�
			  datacenterPower += host.getPower();
			  if(host.getPower() != 0) {
				  setOnHostNum(getOnHostNum() + 1);
			  }
		  }
//		  System.out.println("onHostNum:" + onHostNum + "\n");
		  return datacenterPower;
	  }
	  
	  
	  //����datacenter�ڸ�slot���ܺ�,host������
	  public float calSlotPowerConsumeWithoutSleep() {
		  float datacenterPower = 0;
		  setOnHostNum(0);
		  for(int i=0;i < hostList.size();i++) {
			  Host host = hostList.get(i);
			  //����ÿ��Host�ڸ�slot�ڵ��ܺ�
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
	  



	//������������ʱ����Host�ػ��Ĵ���
	public int calTotalShutdownTime() {
		// TODO Auto-generated method stub
		int totalShutdownTimes = 0;
		for(int i=0;i < hostList.size();i++) {
			//��ȡÿ��host�ڷ���ʱ���ڵĹػ�����
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
	//���ڼ����������������ɵ�Task����
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
