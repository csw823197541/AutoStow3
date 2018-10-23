package com.autostow3.service.stow;

import com.autostow3.algorithm.stow.StowProcess;
import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.WorkingData;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class StowService {

    public void doAutoStow(AllRuntimeData allRuntimeData, Long berthId) {
        WorkingData workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
        workingData.getLogger().logInfo("调用单船自动配载算法，对船舶(berthId:" + berthId + ")进行自动配载。");
        try {
            StowProcess stowProcess = new StowProcess(allRuntimeData, berthId);
            stowProcess.processCwp();
        } catch (Exception e) {
            workingData.getLogger().logError("对船舶(berthId:" + berthId + ")进行自动配载时发生异常！");
            e.printStackTrace();
        }
        workingData.getLogger().logInfo("船舶(berthId:" + berthId + ")自动配载结束。");

    }

    public void doMultipleAutoStow(AllRuntimeData allRuntimeData) {

    }
}
