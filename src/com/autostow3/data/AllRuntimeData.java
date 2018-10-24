package com.autostow3.data;

import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.log.Logger;
import com.autostow3.model.stow.Area;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by csw on 2018/8/21.
 * Description: 整个解析进来的数据封装对象
 */
public class AllRuntimeData {

    private Logger logger;

    //单船数据
    private Map<Long, WorkingData> workingDataMap; //作业数据，key: berthId
    private Map<String, StructureData> structureDataMap; //船舶结构数据，key: vesselCode

    //多船共享数据
    private Map<String, Area> areaMap;

    public AllRuntimeData() {
        logger = new Logger();
        workingDataMap = new HashMap<>();
        structureDataMap = new HashMap<>();
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

    public void addStructData(StructureData structureData) {
        structureDataMap.put(structureData.getVesselCode(), structureData);
    }

    public StructureData getStructDataByVesselCode(String vesselCode) {
        return structureDataMap.get(vesselCode);
    }


}
