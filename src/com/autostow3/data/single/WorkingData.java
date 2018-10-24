package com.autostow3.data.single;

import com.autostow3.model.config.StowConfig;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.result.StowEvaluation;
import com.autostow3.model.result.StowMove;
import com.autostow3.model.result.StowResult;
import com.autostow3.model.result.WeightResult;
import com.autostow3.model.stow.ContainerGroup;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.stow.YardContainer;
import com.autostow3.model.vessel.VMPosition;
import com.autostow3.model.vessel.VMSchedule;
import com.autostow3.model.vessel.VMSlot;
import com.autostow3.model.weight.WeightGroup;

import java.util.*;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class WorkingData {

    private Logger logger;
    private VMSchedule vmSchedule;

    //以下是解析的输入数据
    private StowConfig stowConfig;
    private Map<String, VesselContainer> vesselContainerMap; //集装箱预配数据(包括过境箱、锁定的预配)，Map<vLocation, VMContainer> 小位置，02需要拆成01和03
    private Map<Long, ContainerGroup> containerGroupMap; //属性组数据，<groupId, ContainerGroup>
    private Map<Long, WeightGroup> weightGroupMap; //重量组数据，<weightId, WeightGroup>
    private Map<Long, YardContainer> yardContainerMap; //在场箱数据，<containerId, YardContainer>

    //以下是算法运行之前分析数据产生的中间结果
    private Map<Long, Map<Integer, Integer>> groupWeightNumMap; // 每个属性组每个重量等级的在场箱个数，<groupId, <weightSeq, number>>
    private Map<String, StowMove> stowMoveMap; //配载对象Move，Map<vLocation, StowMove> 小位置，02需要拆成01和03

    //以下是算法运行的结果数据
    private Map<String, WeightResult> weightResultMap; //预配位重量等级划分结果信息(不包括过境箱)，Map<vLocation, WeightResult> 小位置，02需要拆成01和03
    private List<StowResult> stowResultList;
    private List<StowEvaluation> stowEvaluationList;

    public WorkingData(VMSchedule vmSchedule) {
        this.vmSchedule = vmSchedule;
        logger = new Logger();
        vesselContainerMap = new HashMap<>();
        containerGroupMap = new HashMap<>();
        weightGroupMap = new HashMap<>();
        yardContainerMap = new HashMap<>();
        groupWeightNumMap = new HashMap<>();
        stowMoveMap = new HashMap<>();
        weightResultMap = new HashMap<>();
        stowResultList = new ArrayList<>();
        stowEvaluationList = new ArrayList<>();
    }

    public Logger getLogger() {
        return logger;
    }

    public VMSchedule getVmSchedule() {
        return vmSchedule;
    }

    public Long getBerthId() {
        return vmSchedule.getBerthId();
    }

    public String getVesselCode() {
        return vmSchedule.getVesselCode();
    }

    public StowConfig getStowConfig() {
        return stowConfig;
    }

    public void setStowConfig(StowConfig stowConfig) {
        this.stowConfig = stowConfig;
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
        //初始化重量等级划分结果
        if (StowDomain.THROUGH_NO.equals(vesselContainer.getThroughFlag())) {
            WeightResult weightResult = new WeightResult(vesselContainer.getvLocation());
            if (bayNo % 2 == 0) { //大倍位
                weightResultMap.put(new VMPosition(bayNo - 1, rowNo, tierNo).getVLocation(), weightResult);
                weightResultMap.put(new VMPosition(bayNo + 1, rowNo, tierNo).getVLocation(), weightResult);
            } else {
                weightResultMap.put(vmPosition.getVLocation(), weightResult);
            }
        }
    }

    public VesselContainer getVesselContainerByVMSlot(VMSlot vmSlot) {
        if (vmSlot != null) {
            return vesselContainerMap.get(vmSlot.getVmPosition().getVLocation());
        }
        return null;
    }

    public Set<VesselContainer> getAllVesselContainers() {
        return new HashSet<>(vesselContainerMap.values());
    }

    public WeightResult getWeightResultByVMSlot(VMSlot vmSlot) {
        if (vmSlot != null) {
            return weightResultMap.get(vmSlot.getVmPosition().getVLocation());
        }
        return null;
    }

    public void addContainerGroup(ContainerGroup containerGroup) {
        containerGroupMap.put(containerGroup.getGroupId(), containerGroup);
    }

    public ContainerGroup getContainerGroupById(Long groupId) {
        return containerGroupMap.get(groupId);
    }

    public List<ContainerGroup> getAllContainerGroups() {
        return new ArrayList<>(containerGroupMap.values());
    }

    public void addWeightGroup(WeightGroup weightGroup) {
        weightGroupMap.put(weightGroup.getWeightId(), weightGroup);
    }

    public WeightGroup getWeightGroupById(Long weightGroupId) {
        return weightGroupMap.get(weightGroupId);
    }

    public List<WeightGroup> getAllWeightGroups() {
        return new ArrayList<>(weightGroupMap.values());
    }

    public void addYardContainer(YardContainer yardContainer) {
        yardContainerMap.put(yardContainer.getContainerId(), yardContainer);
    }

    public YardContainer getYardContainerByContainerId(Long containerId) {
        return yardContainerMap.get(containerId);
    }

    public List<YardContainer> getAllCanStowContainerList() {
        List<YardContainer> yardContainerList = new ArrayList<>();
        for (Map.Entry<Long, YardContainer> entry : yardContainerMap.entrySet()) {
            if (StowDomain.YES.equals(entry.getValue().getStowageFlag())) {
                yardContainerList.add(entry.getValue());
            }
        }
        return yardContainerList;
    }

    //-----------中间分析生成的数据-----------

    public Map<Long, Map<Integer, Integer>> getGroupWeightNumMap() {
        return groupWeightNumMap;
    }

    public void setGroupWeightNumMap(Map<Long, Map<Integer, Integer>> groupWeightNumMap) {
        this.groupWeightNumMap = groupWeightNumMap;
    }

    public Map<String, StowMove> getStowMoveMap() {
        return stowMoveMap;
    }

    public void setStowMoveMap(Map<String, StowMove> stowMoveMap) {
        this.stowMoveMap = stowMoveMap;
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
