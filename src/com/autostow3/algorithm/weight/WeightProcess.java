package com.autostow3.algorithm.weight;

import com.autostow3.algorithm.analyzer.WeightAnalyzer;
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

    private AllRuntimeData allRuntimeData;
    private WorkingData workingData;
    private StructureData structureData;
    private WeightAnalyzer weightAnalyzer;

    public WeightProcess(AllRuntimeData allRuntimeData, Long berthId) {
        this.allRuntimeData = allRuntimeData;
        workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
        structureData = allRuntimeData.getStructDataByVesselCode(workingData.getVesselCode());
        weightAnalyzer = new WeightAnalyzer();
    }

    public void processWeightLevel() {
        Logger logger = workingData.getLogger();
        logger.logInfo("开始执行...");
        long st = System.currentTimeMillis();

        // 根据属性组统计每个重量等级的在场箱个数，并且按等级重量降序排序
        weightAnalyzer.analyzeGroupAndWeight(workingData);

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
                if (bCurTierNo <= allBelowCurMinTierNo) { //船头有些舱甲板下第一层层号大于最低层时，该舱暂时不进行划分重量等级
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

    private void drawLightLocation(WorkingData workingData, StructureData structureData) {

    }

    private void drawLocationWithWeightLevel(List<Integer> bayNos, Integer rowNo, int curTierNo, WorkingData workingData, StructureData structureData) {
        for (Integer bayNo : bayNos) {
            VMSlot vmSlot = structureData.getVMSlotByVLocation(new VMPosition(bayNo, rowNo, curTierNo).getVLocation());
            VesselContainer vesselContainer = workingData.getVesselContainerByVMSlot(vmSlot);
            if (vesselContainer != null && PublicMethod.canDrawLevelCnt(vesselContainer)) {

            }
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

    private int getAboveWeightTierNo(Long hatchId, WorkingData workingData, StructureData structureData) {
        int aboveWeightCntTier = workingData.getStowConfig().getAboveWeightCntTier();
        VMHatch vmHatch = structureData.getLeftVMHatch(hatchId);
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
        VMHatch vmHatch = structureData.getLeftVMHatch(hatchId);
        if (vmHatch.getBayNoD() >= workingData.getStowConfig().getTailLightCntBayNo())
            if (StowDomain.YES.equals(workingData.getStowConfig().getTailBayHatchWeightCntFlag())) {
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
