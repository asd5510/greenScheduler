package green.core;

public class Task {
	private int taskTime; //该Task运行所需时间(s)
	private int alreadyRunTime; //该Task已经运行的时间
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
	 * remainTime代表当前slot剩余的可运行时间，返回运行完该Task后，该slot剩余的可运行时间
	 * @param remainTime
	 * @return
	 */
	public int update(int remainTime) {		
		if(alreadyRunTime + remainTime >= taskTime) {
			//该Task在该slot内能够完成
			finishTime = SchedulerPolicyAbstract.getCurrentTime() - remainTime + taskTime - alreadyRunTime;
			//返回运行完该Task后，该slot剩余的可运行时间
			return remainTime - (taskTime - alreadyRunTime);
		} else {
			alreadyRunTime += remainTime;
			//该Task在该slot不能完成
			return -1;
		}
	}
	
	public int remainTime() {
		return taskTime - alreadyRunTime;
	}
	//计算Task总的执行时间
	public int calWaitTime() {

		return finishTime - createTime;
	}
}
