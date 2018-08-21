package com.autostow3.algorithm.stow;

import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.log.Logger;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class StowProcess {

    private AllRuntimeData allRuntimeData; //其它船的相关数据
    private WorkingData workingData; //当前需要配载的船相关的输入数据

    public StowProcess(AllRuntimeData allRuntimeData, Long berthId) {
        this.allRuntimeData = allRuntimeData;
        this.workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
    }

    public void processCwp() {
        Logger logger = workingData.getLogger();
        logger.logInfo("开始执行...");
        long st = System.currentTimeMillis();

        //..............

        long et = System.currentTimeMillis();
        logger.logInfo("运行结束，执行时间是：" + (et - st) / 1000 + "秒");
    }
}
