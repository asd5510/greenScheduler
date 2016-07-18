package green.core;

import green.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class Vm {
	//该Vm内运行的Task列表
	private List<Task> taskList = new ArrayList<Task>();
	
	private boolean destroySignal = false;
	
	public int updateTaskProcessing() {
//		boolean isFinished = false;
		//remainTime代表该slot的剩余时间，当一个Task在该slot内完成，remainTime需要减去
		//该Task用掉的时间
		int remainTime = Constant.SCHEDULE_INTERNAL;
		int finishedNum = 0;
		int size = taskList.size();
		for(int i=0;i < size;i++) {
			Task task = taskList.get(i);	
			remainTime = task.update(remainTime);
			if(remainTime >= 0) {
				//task finished,remove		
				//记录该task总的执行时间
				Helper.getWaitTaskTime().add(task.calWaitTime());
				taskList.remove(task);		
				if(taskList.size() == 0)
					//当前Vm内没有待运行的Task，因此准备将该Vm销毁
					setDestroySignal(true);
				finishedNum++;
				//因为taskList移除了元素，所以需要调整i和size
				i--;
				size--;
			}
			
		}
		//返回该slot内当前Vm完成的Task数量
		return finishedNum;
	}
	
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	//判断该Vm是否满，如果不满可以分配新的Task
	public boolean isFull() {
		// TODO Auto-generated method stub
		int needTime = 0;
		for(int i=0;i < taskList.size();i++) {
			Task task = taskList.get(i);
			//task.remainTime()返回Task还需要的运行时间
			needTime += task.remainTime();
		}
		if(needTime + Constant.TASK_TIME> Constant.SCHEDULE_INTERNAL)
			return true;
		else 
			return false;
	}

	public boolean isDestroySignal() {
		return destroySignal;
	}

	public void setDestroySignal(boolean destroySignal) {
		this.destroySignal = destroySignal;
	}
}
