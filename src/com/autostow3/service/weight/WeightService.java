package com.autostow3.service.weight;

import com.autostow3.algorithm.weight.WeightProcess;
import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.WorkingData;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class WeightService {

    public void doWeightLevel(AllRuntimeData allRuntimeData, Long berthId) {
        WorkingData workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
        workingData.getLogger().logInfo("调用单船重量等级算法，对船舶(berthId:" + berthId + ")进行重量等级。");
        try {
            WeightProcess weightProcess = new WeightProcess(allRuntimeData, berthId);
            weightProcess.processWeightLevel();
        } catch (Exception e) {
            workingData.getLogger().logError("对船舶(berthId:" + berthId + ")进行自动配载时发生异常！");
            e.printStackTrace();
        }
        workingData.getLogger().logInfo("船舶(berthId:" + berthId + ")自动配载结束。");
    }
}
