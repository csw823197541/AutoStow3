package com.autostow3.algorithm.weight;

import com.autostow3.algorithm.analyzer.GroupWeightAnalyzer;
import com.autostow3.algorithm.method.PublicMethod;
import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.result.WeightResult;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.vessel.*;

import java.util.*;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class WeightProcess {

    private WorkingData workingData;
    private StructureData structureData;
    private GroupWeightAnalyzer groupWeightAnalyzer;

    public WeightProcess(AllRuntimeData allRuntimeData, Long berthId) {
        workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
        structureData = allRuntimeData.getStructDataByVesselCode(workingData.getVesselCode());
        groupWeightAnalyzer = new GroupWeightAnalyzer();
    }

    public void processWeightLevel() {
        Logger logger = workingData.getLogger();
        logger.logInfo("开始执行...");
        long st = System.currentTimeMillis();

        // 根据属性组统计每个重量等级的在场箱个数，并且按等级重量降序排序
        groupWeightAnalyzer.initGroupWeightNum(workingData);

        // 限定的重量等级检查是否符合要求：甲板上从轻一个等级的找，没有就找重一个等级的，继续这样找；甲板下相反

        // 优先划分等级较重的给预配位
        drawWeightLocation(workingData, structureData);

        // 剩余未划的重量等级都是较轻的，同样的规律划给剩余预配位
        drawLightLocation(workingData, structureData);

        // 检查重压轻、检查槽重、...，交换重量等级

        // 舱下倒配参数，交换重量等级

        long et = System.currentTimeMillis();
        logger.logInfo("执行结束，执行时间是：" + (et - st) / 1000 + "秒。");
    }

    private void drawWeightLocation(WorkingData workingData, StructureData structureData) {
        //每个舱第一层层号
        Map<Long, Integer> bCurTierNoMap = getFirstTierNoMap(StowDomain.BOARD_BELOW, structureData);
        Map<Long, Integer> aCurTierNoMap = getFirstTierNoMap(StowDomain.BOARD_ABOVE, structureData);
        //甲板下最低层层号
        int allBelowCurMinTierNo = getAllCurMinTierNo(bCurTierNoMap);
        //舱号由中间到两边
        List<VMHatch> vmHatchList = getAllVMHatchIdListByMiddle(structureData);
        while (weightLocationNotDone(vmHatchList, bCurTierNoMap, aCurTierNoMap)) {
            for (VMHatch vmHatch : vmHatchList) {
                int bCurTierNo = bCurTierNoMap.get(vmHatch.getHatchId());
                int belowWeightTierNo = getBelowWeightTierNo(vmHatch.getHatchId(), workingData, structureData);
                int aCurTierNo = aCurTierNoMap.get(vmHatch.getHatchId());
                int aboveWeightTierNo = getAboveWeightTierNo(vmHatch.getHatchId(), workingData, structureData);
                if (bCurTierNo <= allBelowCurMinTierNo || bCurTierNo >= 1000) { //船头有些舱甲板下第一层层号大于最低层时，该舱暂时不进行划分重量等级
                    List<Integer> rowNoList = getHatchRowNoListByMiddle(vmHatch.getHatchId(), structureData);
                    for (Integer rowNo : rowNoList) {
                        List<Integer> bayNos = vmHatch.getBayNos();
                        //划分甲板下一个预配箱的重量等级，由参数控制只划分到第几层
                        if (bCurTierNo <= belowWeightTierNo) {
                            drawLocationWithWeightLevel(bayNos, rowNo, bCurTierNo, workingData, structureData);
                        }
                        //划分甲板上一个预配箱的重量等级，由参数控制只划分到第几层
                        if (aCurTierNo <= aboveWeightTierNo) {
                            drawLocationWithWeightLevel(bayNos, rowNo, aCurTierNo, workingData, structureData);
                        }
                    }
                    bCurTierNoMap.put(vmHatch.getHatchId(), bCurTierNo + 2);
                    aCurTierNoMap.put(vmHatch.getHatchId(), aCurTierNo + 2);
                }
            }
            allBelowCurMinTierNo = getAllCurMinTierNo(bCurTierNoMap);
        }
    }

    private boolean weightLocationNotDone(List<VMHatch> vmHatchList, Map<Long, Integer> bCurTierNoMap, Map<Long, Integer> aCurTierNoMap) {
        for (VMHatch vmHatch : vmHatchList) {
            int allCurMinTierNo = getAllCurMinTierNo(bCurTierNoMap);
            int bCurTierNo = bCurTierNoMap.get(vmHatch.getHatchId());
            int belowWeightTierNo = getBelowWeightTierNo(vmHatch.getHatchId(), workingData, structureData);
            if (bCurTierNo <= allCurMinTierNo) {
                int aCurTierNo = aCurTierNoMap.get(vmHatch.getHatchId());
                int aboveWeightTierNo = getAboveWeightTierNo(vmHatch.getHatchId(), workingData, structureData);
                List<Integer> rowNoList = getHatchRowNoListByMiddle(vmHatch.getHatchId(), structureData);
                for (Integer rowNo : rowNoList) {
                    List<Integer> bayNos = vmHatch.getBayNos();
                    //甲板下
                    if (bCurTierNo <= belowWeightTierNo) {
                        if (notDone(bayNos, rowNo, bCurTierNo, structureData, workingData)) {
                            return true;
                        }
                    }
                    //甲板上
                    if (aCurTierNo <= aboveWeightTierNo) {
                        if (notDone(bayNos, rowNo, aCurTierNo, structureData, workingData)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void drawLightLocation(WorkingData workingData, StructureData structureData) {
        //每个舱第一层层号
        Map<Long, Integer> bCurTierNoMap = getFirstTierNoMap(StowDomain.BOARD_BELOW, structureData);
        Map<Long, Integer> aCurTierNoMap = getFirstTierNoMap(StowDomain.BOARD_ABOVE, structureData);

        //舱号由中间到两边
        List<VMHatch> vmHatchList = getAllVMHatchIdListByMiddle(structureData);
        while (lightLocationNotDone(vmHatchList, bCurTierNoMap, aCurTierNoMap)) {
            for (VMHatch vmHatch : vmHatchList) {
                int bCurTierNo = bCurTierNoMap.get(vmHatch.getHatchId());
                int aCurTierNo = aCurTierNoMap.get(vmHatch.getHatchId());
                List<Integer> rowNoList = getHatchRowNoListByMiddle(vmHatch.getHatchId(), structureData);
                for (Integer rowNo : rowNoList) {
                    List<Integer> bayNos = vmHatch.getBayNos();
                    //划分甲板下一个预配箱的重量等级，由参数控制只划分到第几层
                    drawLocationWithWeightLevel(bayNos, rowNo, bCurTierNo, workingData, structureData);
                    //划分甲板上一个预配箱的重量等级，由参数控制只划分到第几层
                    drawLocationWithWeightLevel(bayNos, rowNo, aCurTierNo, workingData, structureData);
                }
                bCurTierNoMap.put(vmHatch.getHatchId(), bCurTierNo + 2);
                aCurTierNoMap.put(vmHatch.getHatchId(), aCurTierNo + 2);
            }
        }
    }

    private boolean lightLocationNotDone(List<VMHatch> vmHatchList, Map<Long, Integer> bCurTierNoMap, Map<Long, Integer> aCurTierNoMap) {
        for (VMHatch vmHatch : vmHatchList) {
            int bCurTierNo = bCurTierNoMap.get(vmHatch.getHatchId());
            int aCurTierNo = aCurTierNoMap.get(vmHatch.getHatchId());
            List<Integer> rowNoList = getHatchRowNoListByMiddle(vmHatch.getHatchId(), structureData);
            for (Integer rowNo : rowNoList) {
                List<Integer> bayNos = vmHatch.getBayNos();
                //甲板下
                if (notDone(bayNos, rowNo, bCurTierNo, structureData, workingData)) {
                    return true;
                }
                //甲板上
                if (notDone(bayNos, rowNo, aCurTierNo, structureData, workingData)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean notDone(List<Integer> bayNos, Integer rowNo, int curTierNo, StructureData structureData, WorkingData workingData) {
        for (Integer bayNo : bayNos) {
            VMSlot vmSlot = structureData.getVMSlotByVLocation(new VMPosition(bayNo, rowNo, curTierNo).getVLocation());
            WeightResult weightResult = workingData.getWeightResultByVMSlot(vmSlot);
            if (weightResult != null && weightResult.getWeightId() == null) {
                return true;
            }
        }
        return false;
    }

    private void drawLocationWithWeightLevel(List<Integer> bayNos, Integer rowNo, int curTierNo, WorkingData workingData, StructureData structureData) {
        Logger logger = workingData.getLogger();
        for (Integer bayNo : bayNos) {
            VMSlot vmSlot = structureData.getVMSlotByVLocation(new VMPosition(bayNo, rowNo, curTierNo).getVLocation());
            VesselContainer vesselContainer = workingData.getVesselContainerByVMSlot(vmSlot);
            if (vesselContainer != null && PublicMethod.canDrawLevelCnt(vesselContainer)) {
                WeightResult weightResult = workingData.getWeightResultByVMSlot(vmSlot);
                if (weightResult.getWeightSeq() == null) { // 预配位没有划分过重量等级
                    Map<Integer, Integer> weightNumMap = workingData.getGroupWeightNumMap().get(vesselContainer.getGroupId());
                    if (weightNumMap != null) {
                        Integer weightSeq = null;
                        int num = 0;
                        for (Map.Entry<Integer, Integer> entry : weightNumMap.entrySet()) {
                            if (entry.getValue() > 0) { //找到最大重量等级
                                weightSeq = entry.getKey();
                                num = entry.getValue();
                                break;
                            }
                        }
                        if (weightSeq != null) {
                            weightResult.setWeightSeq(weightSeq);
                            weightNumMap.put(weightSeq, num - 1);
                        } else {
                            logger.logError("在给预配位(" + vesselContainer.getvLocation() + ")划分重量等级时，发现可配在场箱不够！");
                        }
                    }
                }
            }
        }
    }

    private int getAboveWeightTierNo(Long hatchId, WorkingData workingData, StructureData structureData) {
        int aboveWeightCntTier = workingData.getStowConfig().getAboveWeightCntTier();
        VMHatch vmHatch = structureData.getVMHatchByHatchId(hatchId);
        if (vmHatch.getBayNoD() >= workingData.getStowConfig().getTailLightCntBayNo()) {
            aboveWeightCntTier = 0;
        }
        int minTierNo = structureData.getMinTierNoByHatchIdAndBoard(hatchId, StowDomain.BOARD_ABOVE);
        if (aboveWeightCntTier <= 0) {
            return minTierNo - 2;
        } else {
            return minTierNo + (aboveWeightCntTier - 1) * 2;
        }
    }

    private int getBelowWeightTierNo(Long hatchId, WorkingData workingData, StructureData structureData) {
        VMHatch vmHatch = structureData.getVMHatchByHatchId(hatchId);
        if (vmHatch.getBayNoD() >= workingData.getStowConfig().getTailLightCntBayNo())
            if (StowDomain.YES.equals(workingData.getStowConfig().getTailBayBelowWeightCntFlag())) {
                return 0;
            }
        return 50;
    }

    private int getAllCurMinTierNo(Map<Long, Integer> curTierNoMap) {
        int min = 1000;
        for (Map.Entry<Long, Integer> entry : curTierNoMap.entrySet()) {
            min = entry.getValue() < min ? entry.getValue() : min;
        }
        return min;
    }

    private Map<Long, Integer> getFirstTierNoMap(String board, StructureData structureData) {
        Map<Long, Integer> tierNoMap = new HashMap<>();
        for (VMHatch vmHatch : structureData.getAllVMHatches()) {
            Integer tierNo = structureData.getMinTierNoByHatchIdAndBoard(vmHatch.getHatchId(), board);
            tierNoMap.put(vmHatch.getHatchId(), tierNo);
        }
        return tierNoMap;
    }

    private List<Integer> getHatchRowNoListByMiddle(Long hatchId, StructureData structureData) {
        List<Integer> rowNoList = structureData.getRowSeqListBySeaOrLand(hatchId, StowDomain.ROW_SEQ_EVEN_ODD);
        Collections.sort(rowNoList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        return rowNoList;
    }

    private List<VMHatch> getAllVMHatchIdListByMiddle(StructureData structureData) {
        List<VMHatch> vmHatchList = structureData.getAllVMHatches();
        final int middle = vmHatchList.get(vmHatchList.size() - 1).getHatchSeq() / 2;
        Collections.sort(vmHatchList, new Comparator<VMHatch>() {
            @Override
            public int compare(VMHatch o1, VMHatch o2) {
                Integer m1 = Math.abs(o1.getHatchSeq() - middle);
                Integer m2 = Math.abs(o2.getHatchSeq() - middle);
                return m1.compareTo(m2);
            }
        });
        return vmHatchList;
    }


}
