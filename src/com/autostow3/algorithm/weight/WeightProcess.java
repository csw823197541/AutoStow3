package com.autostow3.algorithm.weight;

import com.autostow3.algorithm.method.PublicMethod;
import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.vessel.*;
import com.autostow3.utils.StringUtil;

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

        Map<Long, Integer> bCurTierNoMap = getFirstTierNoMap(structureData, StowDomain.BOARD_BELOW);
        Map<Long, Integer> aCurTierNoMap = getFirstTierNoMap(structureData, StowDomain.BOARD_ABOVE);

        int allCurTierNo = 0;
        List<VMHatch> vmHatchList = getAllVMHatchIdListByMiddle(structureData);

        for (VMHatch vmHatch : vmHatchList) {
            int bCurTierNo = bCurTierNoMap.get(vmHatch.getHatchId());
            List<Integer> rowNoList = getRowNoListByMiddle(structureData, vmHatch.getHatchId());
            for (Integer rowNo : rowNoList) {
                //甲板下
                List<Integer> bayNos = vmHatch.getBayNos();
                for (Integer bayNo : bayNos) {
                    VMSlot vmSlot = structureData.getVMSlotByVLocation(new VMPosition(bayNo, rowNo, bCurTierNo).getVLocation());
                    VesselContainer vesselContainer = workingData.getVesselContainerByVMSlot(vmSlot);
                    if (vesselContainer != null) {

                    }
                }
                //甲板上
            }
            bCurTierNoMap.put(vmHatch.getHatchId(), bCurTierNo + 2);
        }

        long et = System.currentTimeMillis();
        logger.logInfo("执行结束，执行时间是：" + (et - st) / 1000 + "秒。");
    }

    private Map<Long, Integer> getFirstTierNoMap(StructureData structureData, String board) {
        Map<Long, Integer> tierNoMap = new HashMap<>();
        for (VMHatch vmHatch : structureData.getAllVMHatches()) {
            Integer tierNo = structureData.getMinTierNoByHatchIdAndBoard(vmHatch.getHatchId(), board);
            tierNoMap.put(vmHatch.getHatchId(), tierNo);
        }
        return tierNoMap;
    }

    private List<Integer> getRowNoListByMiddle(StructureData structureData, Long hatchId) {
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
