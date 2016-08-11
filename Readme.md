需要说明的是，整个执行过程不能乱：先bindTasktoVm()然后calSlotPowerConsume()再updateHostList()

1 该版本修正了shutdowntimes之前的计算错误
2 增加了每个时间槽开机数目的显示（在calSlotPowerComsume()或calSlotPowerConsumeWithoutSleep()中计算）
3 SchedulerBasic调度方式一旦开机后就不关机(主要通过calSlotPowerComsumeWithoutSleep())


![image](https://github.com/asd5510/greenScheduler/blob/master/data/f.png)

仿真框架图：
![image](https://github.com/asd5510/greenScheduler/blob/master/data/green框图.jpg)

运行结果示例：
![image](https://github.com/asd5510/greenScheduler/blob/master/data/3次方.jpg)
