package com.autostow3.service.parse;

import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.domain.DefaultValue;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.vessel.VMSchedule;
import com.autostow3.utils.StringUtil;
import com.autostow3.utils.ValidatorUtil;
import com.shbtos.biz.smart.cwp.pojo.SmartScheduleIdInfo;
import com.shbtos.biz.smart.cwp.pojo.SmartVesselContainerInfo;
import com.shbtos.biz.smart.cwp.service.SmartStowImportData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class ParseDataService {

    public AllRuntimeData parseAllRuntimeData(SmartStowImportData smartStowImportData) {
        AllRuntimeData allRuntimeData = new AllRuntimeData();

        List<VMSchedule> vmScheduleList = parseSchedule(smartStowImportData.getSmartScheduleIdInfoList(), allRuntimeData);
        for (VMSchedule vmSchedule : vmScheduleList) {
            allRuntimeData.addWorkingData(new WorkingData(vmSchedule));
        }

        parseVesselContainer(smartStowImportData.getSmartVesselContainerInfoList(), allRuntimeData);

        return allRuntimeData;
    }

    private List<VMSchedule> parseSchedule(List<SmartScheduleIdInfo> smartScheduleIdInfoList, AllRuntimeData allRuntimeData) {
        Logger logger = allRuntimeData.getLogger();
        List<VMSchedule> vmScheduleList = new ArrayList<>();
        logger.logInfo("当前运行的自动配载算法版本号为: " + DefaultValue.STOW_VERSION);
        logger.logError("航次信息", ValidatorUtil.isEmpty(smartScheduleIdInfoList));
        for (SmartScheduleIdInfo smartScheduleIdInfo : smartScheduleIdInfoList) {
            Long berthId = smartScheduleIdInfo.getBerthId();
            String vesselCode = smartScheduleIdInfo.getVesselCode();
            String planBerthDirect = smartScheduleIdInfo.getPlanBerthDirect();
            String vesselType = smartScheduleIdInfo.getVesselType();
            try {
                logger.logError("航次信息-靠泊Id", ValidatorUtil.isNull(berthId));
                logger.logError("航次信息-船舶代码", ValidatorUtil.isBlank(vesselCode));
                logger.logError("航次信息-停靠方向", ValidatorUtil.isBlank(planBerthDirect));
                logger.logError("航次信息-船舶类型", ValidatorUtil.isBlank(vesselType));
                VMSchedule vmSchedule = new VMSchedule(berthId, vesselCode);
                vmSchedule.setPlanBeginWorkTime(smartScheduleIdInfo.getPlanBeginWorkTime());
                vmSchedule.setPlanEndWorkTime(smartScheduleIdInfo.getPlanEndWorkTime());
                vmSchedule.setPlanStartPst(smartScheduleIdInfo.getPlanStartPst());
                vmSchedule.setPlanEndPst(smartScheduleIdInfo.getPlanEndPst());
                vmSchedule.setSendWorkInstruction(smartScheduleIdInfo.getSendWorkInstruction());
                logger.logInfo("berthId: " + berthId + ", vesselCode: " + vesselCode + ", planBerthDirect: " + planBerthDirect + ", vesselType: " + vesselType);
                planBerthDirect = planBerthDirect.equals("L") ? StowDomain.VES_BER_DIRECT_L : StowDomain.VES_BER_DIRECT_R;
                vesselType = vesselType.equals("BAR") ? StowDomain.VESSEL_TYPE_BAR : StowDomain.VESSEL_TYPE_FCS;
                vmSchedule.setVesselType(vesselType);
                vmSchedule.setPlanBerthDirect(planBerthDirect);
                vmScheduleList.add(vmSchedule);
            } catch (Exception e) {
                logger.logError("解析航次(berthId:" + berthId + ", vesselCode:" + vesselCode + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
        return vmScheduleList;
    }

    private void parseVesselContainer(List<SmartVesselContainerInfo> smartVesselContainerInfoList, AllRuntimeData allRuntimeData) {
        Logger logger = allRuntimeData.getLogger();
        logger.logError("CWP计划信息", ValidatorUtil.isEmpty(smartVesselContainerInfoList));
        WorkingData workingData;
        for (SmartVesselContainerInfo smartVesselContainerInfo : smartVesselContainerInfoList) {
            Long berthId = smartVesselContainerInfo.getBerthId();
            String vLocation = smartVesselContainerInfo.getvLocation();
            Long vpcCntId = smartVesselContainerInfo.getVpcCntrId();
            String size = smartVesselContainerInfo.getcSzCsizecd();
            String type = smartVesselContainerInfo.getcTypeCd();
            String dlType = smartVesselContainerInfo.getLduldfg();
            String throughFlag = smartVesselContainerInfo.getThroughFlag();
            Long hatchId = smartVesselContainerInfo.getHatchId();
            String workFlow = smartVesselContainerInfo.getWorkflow();
            Long moveOrder = smartVesselContainerInfo.getCwpwkMoveNum();
            Long cntWorkTime = smartVesselContainerInfo.getContainerWorkInterval(); //单位秒
            Double weight = smartVesselContainerInfo.getWeight();
            String dgCd = smartVesselContainerInfo.getDtpDnggcd(); //危险品：
            String isHeight = smartVesselContainerInfo.getIsHeight(); //是否是高箱：Y/N
            String cntHeight = smartVesselContainerInfo.getCntHeightDesc(); //箱子具体高度
            String rfFlag = smartVesselContainerInfo.getRfcfg(); //冷藏标记：Y/N
            String overrunCd = smartVesselContainerInfo.getOvlmtcd(); //超限箱标记：Y/N
            throughFlag = "N".equals(throughFlag) ? StowDomain.THROUGH_NO : StowDomain.THROUGH_YES;
            dgCd = StringUtil.isNotBlank(dgCd) ? !"N".equals(dgCd) ? StowDomain.YES : StowDomain.NO : StowDomain.NO;
            overrunCd = StringUtil.isNotBlank(overrunCd) ? !"N".equals(overrunCd) ? StowDomain.YES : StowDomain.NO : StowDomain.NO;
            try {
                workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                if (workingData != null) {
                    VesselContainer vesselContainer = new VesselContainer(vLocation, dlType);
                    if (StowDomain.THROUGH_NO.equals(throughFlag)) { //非过境箱
                        vesselContainer.setThroughFlag(throughFlag);
                        vesselContainer.setVpcCntId(vpcCntId);
                        vesselContainer.setBerthId(berthId);
                        vesselContainer.setHatchId(hatchId);
                        vesselContainer.setCntHeight(cntHeight);
                        vesselContainer.setCntWorkTime(cntWorkTime);
                        vesselContainer.setWeightKg(weight);
                        vesselContainer.setSize(size);
                        vesselContainer.setType(type);
                        vesselContainer.setDgCd(dgCd);
                        vesselContainer.setIsHeight(isHeight);
                        vesselContainer.setRfFlag(rfFlag);
                        vesselContainer.setOverrunCd(overrunCd);
                        workingData.getVesselContainerList().add(vesselContainer);
                    }
                }
            } catch (Exception e) {
                logger.logError("解析进出口船图箱(berthId:" + berthId + ", vLocation:" + vLocation + ", dlType:" + dlType + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }
}
