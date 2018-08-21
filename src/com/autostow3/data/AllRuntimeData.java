package com.autostow3.data;

import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class AllRuntimeData {

    private Logger logger;

    //单船数据
    private Map<Long, WorkingData> workingDataMap; //作业数据,key: berthId

    public AllRuntimeData() {
        logger = new Logger();
        workingDataMap = new HashMap<>();
    }

    public Logger getLogger() {
        return logger;
    }

    public void addWorkingData(WorkingData workingData) {
        workingDataMap.put(workingData.getVmSchedule().getBerthId(), workingData);
    }

    public WorkingData getWorkingDataByBerthId(Long berthId) {
        return workingDataMap.get(berthId);
    }

    public Map<Long, WorkingData> getWorkingDataMap() {
        return workingDataMap;
    }

    public void setWorkingDataMap(Map<Long, WorkingData> workingDataMap) {
        this.workingDataMap = workingDataMap;
    }
}
