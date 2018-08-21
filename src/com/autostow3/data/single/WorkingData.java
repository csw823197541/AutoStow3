package com.autostow3.data.single;

import com.autostow3.model.log.Logger;
import com.autostow3.model.result.StowEvaluation;
import com.autostow3.model.result.StowResult;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.vessel.VMSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class WorkingData {

    private Logger logger;
    private VMSchedule vmSchedule;

    //以下是解析的输入数据
    private List<VesselContainer> vesselContainerList;


    //以下是算法运行的结果数据
    private List<StowResult> stowResultList;
    private List<StowEvaluation> stowEvaluationList;

    public WorkingData(VMSchedule vmSchedule) {
        this.vmSchedule = vmSchedule;
        logger = new Logger();
        vesselContainerList = new ArrayList<>();
        stowResultList = new ArrayList<>();
        stowEvaluationList = new ArrayList<>();
    }

    public VMSchedule getVmSchedule() {
        return vmSchedule;
    }

    public Logger getLogger() {
        return logger;
    }

    public List<VesselContainer> getVesselContainerList() {
        return vesselContainerList;
    }

    public void setVesselContainerList(List<VesselContainer> vesselContainerList) {
        this.vesselContainerList = vesselContainerList;
    }

    public List<StowResult> getStowResultList() {
        return stowResultList;
    }

    public void setStowResultList(List<StowResult> stowResultList) {
        this.stowResultList = stowResultList;
    }

    public List<StowEvaluation> getStowEvaluationList() {
        return stowEvaluationList;
    }

    public void setStowEvaluationList(List<StowEvaluation> stowEvaluationList) {
        this.stowEvaluationList = stowEvaluationList;
    }
}
