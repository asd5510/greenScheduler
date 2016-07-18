package green.core;

import green.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class Vm {
	//��Vm�����е�Task�б�
	private List<Task> taskList = new ArrayList<Task>();
	
	private boolean destroySignal = false;
	
	public int updateTaskProcessing() {
//		boolean isFinished = false;
		//remainTime�����slot��ʣ��ʱ�䣬��һ��Task�ڸ�slot����ɣ�remainTime��Ҫ��ȥ
		//��Task�õ���ʱ��
		int remainTime = Constant.SCHEDULE_INTERNAL;
		int finishedNum = 0;
		int size = taskList.size();
		for(int i=0;i < size;i++) {
			Task task = taskList.get(i);	
			remainTime = task.update(remainTime);
			if(remainTime >= 0) {
				//task finished,remove		
				//��¼��task�ܵ�ִ��ʱ��
				Helper.getWaitTaskTime().add(task.calWaitTime());
				taskList.remove(task);		
				if(taskList.size() == 0)
					//��ǰVm��û�д����е�Task�����׼������Vm����
					setDestroySignal(true);
				finishedNum++;
				//��ΪtaskList�Ƴ���Ԫ�أ�������Ҫ����i��size
				i--;
				size--;
			}
			
		}
		//���ظ�slot�ڵ�ǰVm��ɵ�Task����
		return finishedNum;
	}
	
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	//�жϸ�Vm�Ƿ���������������Է����µ�Task
	public boolean isFull() {
		// TODO Auto-generated method stub
		int needTime = 0;
		for(int i=0;i < taskList.size();i++) {
			Task task = taskList.get(i);
			//task.remainTime()����Task����Ҫ������ʱ��
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
