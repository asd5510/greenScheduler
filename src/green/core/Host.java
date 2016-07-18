package green.core;


import java.util.ArrayList;
import java.util.List;


public class Host {
	
	private int vmNum;
	private List<Vm> vmList = new ArrayList<Vm>();
	//该Host在仿真过程中的关机次数
	private int shutdownTimes = 0;
	private boolean destroySignal = false;
	private PowerModel powerModel;
	
	public Host() {
		vmNum = Constant.VM_NUM;
		setPowerModel(new PowerModelLinear(Constant.PEAK_POWER, Constant.STATIC_POWER / Constant.PEAK_POWER));
/*		for(int i=0;i < Constant.VM_NUM;i++) {
			vmList.add(new Vm());
		}*/
	}
	public List<Vm> getVmList() {
		return vmList;
	}
	public void setVmList(List<Vm> vmList) {
		this.vmList = vmList;
	}

	//这其实是给Vm分配Task的调度算法，后期尽量做成可插拔的形式	
	public boolean isAvailable() {
		if(vmList.size() < vmNum)
			return true;
		else if(vmList.size() == vmNum) {
			for(int i=0;i < vmList.size();i++) {
				if(!vmList.get(i).isFull()) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
			
	}
	//更新Vm
	public int updateVmProcessing() {
		int finishedTaskNum = 0;
		//任务完成不立即关机，等到下个时间槽若没有任务再关机
		if(vmList.size() == 0 && destroySignal == true) {
			setShutdownTimes(getShutdownTimes() + 1);
		} else {
			destroySignal = false;
		}
		for(int i=0;i < vmList.size();i++) {
			Vm vm = vmList.get(i);
			finishedTaskNum += vm.updateTaskProcessing();
			//如果vm中没有Task了则将该vm从Host中移除
			if(vm.isDestroySignal()) {
				vmList.remove(vm);
				//all vm is destroyed,then this host shutdowm
				if(vmList.size() == 0) {
//					setShutdownTimes(getShutdownTimes() + 1);  立即关机不空转
					destroySignal = true;
				}
				i--;
			}
		}
		return finishedTaskNum;
	}
	
	//cal this host's power in this slot
	public float getPower() {
/*		if(vmList.size() == 0) {
			//this host is off
			return 0;
		} else {
			return Constant.SCHEDULE_INTERNAL / 3600.0f * (Constant.STATIC_POWER + 
				(Constant.PEAK_POWER - Constant.STATIC_POWER) / vmNum * vmList.size());
		//use powerModel to cal power
		}*/
		return (float) getPowerModel().getPower(vmList.size() * 1.0f / Constant.VM_NUM);

		
	}
	
	public void bindTaskToVm(Task task) {
		// TODO Auto-generated method stub
		//优先启动VM来接收新的Task	
		if(vmList.size() < vmNum) {
			Vm vm = new Vm();
			vm.getTaskList().add(task);
			vmList.add(vm);
		} else {
			//如果已经启动了最大数量的Vm，则将Task分配给未满载的Vm
			for(int i=0;i < vmList.size();i++) {
				if(!vmList.get(i).isFull()) {
					vmList.get(i).getTaskList().add(task);
					break;
				}
			}
		}
	}
	public int getShutdownTimes() {
		return shutdownTimes;
	}
	public void setShutdownTimes(int shutdownTimes) {
		this.shutdownTimes = shutdownTimes;
	}
	public PowerModel getPowerModel() {
		return powerModel;
	}
	public void setPowerModel(PowerModel powerModel) {
		this.powerModel = powerModel;
	}
	public boolean isDestroySignal() {
		return destroySignal;
	}

	public void setDestroySignal(boolean destroySignal) {
		this.destroySignal = destroySignal;
	}
}
