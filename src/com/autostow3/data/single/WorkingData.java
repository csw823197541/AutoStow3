package com.autostow3.data.single;

import com.autostow3.model.config.AutoStowConfig;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.result.StowEvaluation;
import com.autostow3.model.result.StowResult;
import com.autostow3.model.stow.ContainerGroup;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.stow.YardContainer;
import com.autostow3.model.vessel.VMPosition;
import com.autostow3.model.vessel.VMSchedule;
import com.autostow3.model.vessel.VMSlot;
import com.autostow3.model.weight.WeightGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class WorkingData {

    private Logger logger;
    private VMSchedule vmSchedule;

    //以下是解析的输入数据
    private AutoStowConfig autoStowConfig;
    private Map<String, VesselContainer> vesselContainerMap; //集装箱预配数据(包括过境箱)，Map<vLocation, VMContainer> 小位置，02需要拆成01和03
    private Map<Long, ContainerGroup> containerGroupMap; //属性组数据，<groupId, ContainerGroup>
    private Map<Long, WeightGroup> weightGroupMap; //重量组数据，<weightId, WeightGroup>

    //以下是算法运行的结果数据
    private List<StowResult> stowResultList;
    private List<StowEvaluation> stowEvaluationList;

    public WorkingData(VMSchedule vmSchedule) {
        this.vmSchedule = vmSchedule;
        logger = new Logger();
        autoStowConfig = new AutoStowConfig();
        vesselContainerMap = new HashMap<>();

        containerGroupMap = new HashMap<>();
        weightGroupMap = new HashMap<>();
        stowResultList = new ArrayList<>();
        stowEvaluationList = new ArrayList<>();
    }

    public VMSchedule getVmSchedule() {
        return vmSchedule;
    }

    public Logger getLogger() {
        return logger;
    }

    public AutoStowConfig getAutoStowConfig() {
        return autoStowConfig;
    }

    public void putVesselContainer(VMPosition vmPosition, VesselContainer vesselContainer) {
        Integer bayNo = vmPosition.getBayNo();
        Integer tierNo = vmPosition.getTierNo();
        Integer rowNo = vmPosition.getRowNo();
        if (bayNo % 2 == 0) { //大倍位
            vesselContainerMap.put(new VMPosition(bayNo - 1, rowNo, tierNo).getVLocation(), vesselContainer);
            vesselContainerMap.put(new VMPosition(bayNo + 1, rowNo, tierNo).getVLocation(), vesselContainer);
        } else {
            vesselContainerMap.put(vmPosition.getVLocation(), vesselContainer);
        }
    }

    public VesselContainer getVesselContainerByVMSlot(VMSlot vmSlot) {
        if (vmSlot != null) {
            return vesselContainerMap.get(vmSlot.getVmPosition().getVLocation());
        }
        return null;
    }

    public void addContainerGroup(ContainerGroup containerGroup) {
        containerGroupMap.put(containerGroup.getGroupId(), containerGroup);
    }

    public ContainerGroup getContainerGroupById(Long groupId) {
        return containerGroupMap.get(groupId);
    }

    public void addWeightGroup(WeightGroup weightGroup) {
        weightGroupMap.put(weightGroup.getWeightId(), weightGroup);
    }

    public WeightGroup getWeightGroupById(Long weightGroupId) {
        return weightGroupMap.get(weightGroupId);
    }

    //-----------结果输出对象基础方法-----------

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
