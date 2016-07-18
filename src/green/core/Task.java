package green.core;

public class Task {
	private int taskTime; //��Task��������ʱ��(s)
	private int alreadyRunTime; //��Task�Ѿ����е�ʱ��
	private int createTime;
	private int finishTime = 0;
	public Task() {
		this.taskTime = Constant.TASK_TIME;
		alreadyRunTime = 0;
		createTime = SchedulerPolicyAbstract.getCurrentTime();
	}
	
	public Task(int taskTime) {
		this.taskTime = taskTime;
		alreadyRunTime = 0;
		createTime = SchedulerPolicyAbstract.getCurrentTime();
	}
	/**
	 * remainTime����ǰslotʣ��Ŀ�����ʱ�䣬�����������Task�󣬸�slotʣ��Ŀ�����ʱ��
	 * @param remainTime
	 * @return
	 */
	public int update(int remainTime) {		
		if(alreadyRunTime + remainTime >= taskTime) {
			//��Task�ڸ�slot���ܹ����
			finishTime = SchedulerPolicyAbstract.getCurrentTime() - remainTime + taskTime - alreadyRunTime;
			//�����������Task�󣬸�slotʣ��Ŀ�����ʱ��
			return remainTime - (taskTime - alreadyRunTime);
		} else {
			alreadyRunTime += remainTime;
			//��Task�ڸ�slot�������
			return -1;
		}
	}
	
	public int remainTime() {
		return taskTime - alreadyRunTime;
	}
	//����Task�ܵ�ִ��ʱ��
	public int calWaitTime() {

		return finishTime - createTime;
	}
}
