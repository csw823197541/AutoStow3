package com.autostow3.service.parse;

import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.config.StowConfig;
import com.autostow3.model.domain.DefaultValue;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.log.Logger;
import com.autostow3.model.stow.ContainerGroup;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.stow.YardContainer;
import com.autostow3.model.vessel.*;
import com.autostow3.model.weight.WeightGroup;
import com.autostow3.utils.StringUtil;
import com.autostow3.utils.ValidatorUtil;
import com.shbtos.biz.smart.cwp.pojo.*;
import com.shbtos.biz.smart.cwp.service.SmartStowImportData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class ParseDataService {

    private Logger logger;

    public AllRuntimeData parseAllRuntimeDataByWeight(SmartStowImportData smartStowImportData) {
        AllRuntimeData allRuntimeData = new AllRuntimeData();
        logger = allRuntimeData.getLogger();

        List<VMSchedule> vmScheduleList = parseSchedule(smartStowImportData.getSmartScheduleIdInfoList());
        for (VMSchedule vmSchedule : vmScheduleList) {
            allRuntimeData.addWorkingData(new WorkingData(vmSchedule));
            allRuntimeData.addStructData(new StructureData(vmSchedule.getVesselCode()));
        }

        parseStructData(smartStowImportData, allRuntimeData);

        parseStowConfig(smartStowImportData.getSmartStowageConfigurationInfoList(), allRuntimeData);
        parseWeightVesselContainer(smartStowImportData.getSmartVesselContainerInfoList(), allRuntimeData);
        parseContainerGroup(smartStowImportData.getSmartContainerGroupInfoList(), allRuntimeData);
        parseWeightGroup(smartStowImportData.getSmartWeightGroupInfoList(), allRuntimeData);
        parseYardContainer(smartStowImportData.getSmartYardContainerInfoList(), allRuntimeData);

//        parseYardContainerWorkTime(smartStowImportData.getSmartCntrWorkTimeInfoList(), allRuntimeData);

        return allRuntimeData;
    }

    private List<VMSchedule> parseSchedule(List<SmartScheduleIdInfo> smartScheduleIdInfoList) {
        List<VMSchedule> vmScheduleList = new ArrayList<>();
        logger.logInfo("当前运行的自动配载算法版本号为: " + DefaultValue.STOW_VERSION);
        logger.logInfo("解析所有船舶航次信息。");
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

    private void parseStructData(SmartStowImportData smartStowImportData, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析船舶结构信息：舱、倍、排、船箱位。");
        parseHatchInfo(smartStowImportData.getSmartVpsVslHatchsInfoList(), allRuntimeData);
        parseBayInfo(smartStowImportData.getSmartVpsVslBaysInfoList(), allRuntimeData);
        parseRowInfo(smartStowImportData.getSmartVpsVslRowsInfoList(), allRuntimeData);
        parseLocationInfo(smartStowImportData.getSmartVpsVslLocationsInfoList(), allRuntimeData);
    }

    private void parseHatchInfo(List<SmartVpsVslHatchsInfo> smartVpsVslHatchsInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("舱信息", ValidatorUtil.isEmpty(smartVpsVslHatchsInfoList));
        for (SmartVpsVslHatchsInfo smartVpsVslHatchsInfo : smartVpsVslHatchsInfoList) {
            String vesselCode = smartVpsVslHatchsInfo.getVesselCode();
            Long hatchId = smartVpsVslHatchsInfo.getHatchId();
            try {
                if (allRuntimeData.getStructDataByVesselCode(vesselCode) != null) {
                    logger.logError("舱信息-舱(Id:" + hatchId + ")信息为null", ValidatorUtil.isNull(hatchId));
                    VMHatch vmHatch = new VMHatch(hatchId);
                    logger.logError("舱信息-舱(Id:" + hatchId + ")位置坐标为null", ValidatorUtil.isNull(smartVpsVslHatchsInfo.getHatchPosition()));
                    vmHatch.setHatchPosition(smartVpsVslHatchsInfo.getHatchPosition());
                    logger.logError("舱信息-舱(Id:" + hatchId + ")长度为null", ValidatorUtil.isNull(smartVpsVslHatchsInfo.getHatchLength()));
                    vmHatch.setHatchLength(smartVpsVslHatchsInfo.getHatchLength());
                    logger.logError("舱信息-舱(Id:" + hatchId + ")序号为null", ValidatorUtil.isNull(smartVpsVslHatchsInfo.getHatchSeq()));
                    vmHatch.setHatchSeq(smartVpsVslHatchsInfo.getHatchSeq());
                    allRuntimeData.getStructDataByVesselCode(vesselCode).addVMHatch(vmHatch);
                }
            } catch (Exception e) {
                logger.logError("解析船舶结构(vesselCode:" + vesselCode + ")舱(Id:" + hatchId + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseBayInfo(List<SmartVpsVslBaysInfo> smartVpsVslBaysInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("倍位信息", ValidatorUtil.isEmpty(smartVpsVslBaysInfoList));
        StructureData structureData;
        for (SmartVpsVslBaysInfo smartVpsVslBaysInfo : smartVpsVslBaysInfoList) {
            Long bayId = smartVpsVslBaysInfo.getBayId();
            Long hatchId = smartVpsVslBaysInfo.getHatchId();
            String aboveOrBelow = smartVpsVslBaysInfo.getDeckOrHatch();
            String vesselCode = smartVpsVslBaysInfo.getVesselCode();
            Integer bayNo = null;
            try {
                structureData = allRuntimeData.getStructDataByVesselCode(vesselCode);
                if (structureData != null) {
                    logger.logError("倍位信息-倍位号", ValidatorUtil.isNull(smartVpsVslBaysInfo.getBayNo()));
                    bayNo = Integer.valueOf(smartVpsVslBaysInfo.getBayNo());
                    logger.logError("倍位信息-倍位信息中甲板上、下字段为null", ValidatorUtil.isNull(aboveOrBelow));
                    aboveOrBelow = aboveOrBelow.equals("D") ? StowDomain.BOARD_ABOVE : StowDomain.BOARD_BELOW;
                    String bayKey = StringUtil.getKey(bayNo, aboveOrBelow);
                    VMBay vmBay = new VMBay(bayId, bayKey, bayNo, aboveOrBelow, hatchId);
                    structureData.addVMBay(vmBay);
                    //设置舱内bay1、bay2，数字小在前
                    VMHatch vmHatch = structureData.getVMHatchByHatchId(hatchId);
                    vmHatch.addByNo(bayNo);
                }
            } catch (Exception e) {
                logger.logError("解析船舶结构(vesselCode:" + vesselCode + ")倍位(bayNo:" + bayNo + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseRowInfo(List<SmartVpsVslRowsInfo> smartVpsVslRowsInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("排信息", ValidatorUtil.isEmpty(smartVpsVslRowsInfoList));
        StructureData structureData;
        for (SmartVpsVslRowsInfo smartVpsVslRowsInfo : smartVpsVslRowsInfoList) {
            Long bayId = smartVpsVslRowsInfo.getBayId();
            String vesselCode = smartVpsVslRowsInfo.getVesselCode();
            Integer rowNo = null;
            try {
                structureData = allRuntimeData.getStructDataByVesselCode(vesselCode);
                if (structureData != null) {
                    logger.logError("排信息-排号", ValidatorUtil.isNull(smartVpsVslRowsInfo.getRowNo()));
                    rowNo = Integer.valueOf(smartVpsVslRowsInfo.getRowNo());
                    VMBay vmBay = structureData.getVMBayByBayId(bayId);
                    VMRow vmRow = new VMRow(vmBay.getBayId(), vmBay.getBayKey(), rowNo);
                    vmRow.setMaxWeight20(smartVpsVslRowsInfo.getMaxWeight20());
                    vmRow.setMaxWeight40(smartVpsVslRowsInfo.getMaxWeight40());
                    vmBay.addVMRow(vmRow);
                }
            } catch (Exception e) {
                logger.logError("解析船舶结构(vesselCode:" + vesselCode + ")排(rowNo:" + rowNo + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseLocationInfo(List<SmartVpsVslLocationsInfo> smartVpsVslLocationsInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("船箱位信息", ValidatorUtil.isEmpty(smartVpsVslLocationsInfoList));
        StructureData structureData;
        for (SmartVpsVslLocationsInfo smartVpsVslLocationsInfo : smartVpsVslLocationsInfoList) {
            String vLocation = smartVpsVslLocationsInfo.getLocation();
            Long bayId = smartVpsVslLocationsInfo.getBayId();
            String vesselCode = smartVpsVslLocationsInfo.getVesselCode();
            String size = smartVpsVslLocationsInfo.getSize();
            try {
                structureData = allRuntimeData.getStructDataByVesselCode(vesselCode);
                if (structureData != null) {
                    VMPosition vmPosition = new VMPosition(vLocation);
                    VMBay vmBay = structureData.getVMBayByBayId(bayId);
                    VMContainerSlot vmContainerSlot = new VMContainerSlot(vmPosition, vmBay, size);
                    structureData.addVMSlot(vmContainerSlot);
                    //要根据船箱位信息，初始化该倍位下每排的最大层号和最小层号
                    Integer rowNo = vmPosition.getRowNo();
                    VMRow vmRow = vmBay.getVMRowByRowNo(rowNo);
                    logger.logError("船箱位信息-查找不到排(" + rowNo + ")信息！", ValidatorUtil.isNull(vmRow));
                    vmRow.addVMSlot(vmContainerSlot);
                }
            } catch (Exception e) {
                logger.logError("解析船舶结构(vesselCode:" + vesselCode + ")船箱位(vLocation:" + vLocation + ")信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseStowConfig(List<SmartStowageConfigurationInfo> smartStowageConfigurationInfoList, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析配载配置参数信息。");
        WorkingData workingData;
        for (SmartStowageConfigurationInfo smartStowageConfigurationInfo : smartStowageConfigurationInfoList) {
            Long berthId = smartStowageConfigurationInfo.getBerthId();
            try {
                workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                if (workingData != null) {
                    StowConfig stowConfig = new StowConfig(berthId);
                    //舱内倒配参数
                    String reverseLoadFlag = smartStowageConfigurationInfo.getReverseLoad() ? StowDomain.YES : StowDomain.NO;
                    stowConfig.setReverseLoadFlag(reverseLoadFlag);
                    //甲板上配重箱的层数，0表示不需要，1-10代表有多少层需要配重箱，默认是1
                    Integer aboveWeightCntTier = smartStowageConfigurationInfo.getAboveTier();
                    aboveWeightCntTier = aboveWeightCntTier != null ? aboveWeightCntTier : DefaultValue.aboveWeightCntTier;
                    stowConfig.setAboveWeightCntTier(aboveWeightCntTier);
                    //从这个倍位号开始都配轻箱，大倍位号2、6，默认不需要考虑此参数时，则设置超过船舶最大倍位号：1000
                    Integer tailLightCntBayNo = smartStowageConfigurationInfo.getHatchTail();
                    stowConfig.setTailLightCntBayNo(tailLightCntBayNo);
                    //尾舱划分轻的情况下，对应这几个舱的甲板下是否划重箱，默认为false
                    String tailBayBelowWeightCntFlag = smartStowageConfigurationInfo.getUnderDeckPriority() ? StowDomain.YES : StowDomain.NO;
                    stowConfig.setTailBayBelowWeightCntFlag(tailBayBelowWeightCntFlag);
                    workingData.setStowConfig(stowConfig);
                }
            } catch (Exception e) {
                logger.logError("解析配载(berthId:" + berthId + ")配置参数信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseWeightVesselContainer(List<SmartVesselContainerInfo> smartVesselContainerInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("重量等级划分，出口船图箱信息", ValidatorUtil.isEmpty(smartVesselContainerInfoList));
        WorkingData workingData;
        for (SmartVesselContainerInfo smartVesselContainerInfo : smartVesselContainerInfoList) {
            String dlType = smartVesselContainerInfo.getLduldfg();
            if ("L".equals(dlType)) {
                Long berthId = smartVesselContainerInfo.getBerthId();
                String vLocation = smartVesselContainerInfo.getvLocation();
                Long vpcCntId = smartVesselContainerInfo.getVpcCntrId();
                String size = smartVesselContainerInfo.getcSzCsizecd();
                String type = smartVesselContainerInfo.getcTypeCd();
                String throughFlag = smartVesselContainerInfo.getThroughFlag();
                throughFlag = "N".equals(throughFlag) ? StowDomain.THROUGH_NO : StowDomain.THROUGH_YES;
                Long hatchId = smartVesselContainerInfo.getHatchId();
                Long cntWorkTime = smartVesselContainerInfo.getContainerWorkInterval(); //单位秒
                Double weight = smartVesselContainerInfo.getWeight();
                String dgCd = smartVesselContainerInfo.getDtpDnggcd(); //危险品：
                dgCd = StringUtil.isNotBlank(dgCd) ? !"N".equals(dgCd) ? StowDomain.YES : StowDomain.NO : StowDomain.NO;
                String isHeight = smartVesselContainerInfo.getIsHeight(); //是否是高箱：Y/N
                String cntHeight = smartVesselContainerInfo.getCntHeightDesc(); //箱子具体高度
                String rfFlag = smartVesselContainerInfo.getRfcfg(); //冷藏标记：Y/N
                String overrunCd = smartVesselContainerInfo.getOvlmtcd(); //超限箱标记：Y/N
                overrunCd = StringUtil.isNotBlank(overrunCd) ? !"N".equals(overrunCd) ? StowDomain.YES : StowDomain.NO : StowDomain.NO;
                String fixWeightLevelFlag = smartVesselContainerInfo.getFixedWeightlevel() ? StowDomain.YES : StowDomain.NO;
                Long groupId = smartVesselContainerInfo.getGroupId();
                try {
                    workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                    if (workingData != null) {
                        VesselContainer vesselContainer = new VesselContainer(vLocation, dlType);
                        if (groupId == null) {
                            logger.logError("预配位没有属性组Id");
                            continue;
                        }
                        vesselContainer.setGroupId(groupId);
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
                        vesselContainer.setFixedWeightLevel(fixWeightLevelFlag);
                        vesselContainer.setEfFlag(smartVesselContainerInfo.getEffg());
                        workingData.putVesselContainer(new VMPosition(vLocation), vesselContainer);
                    }
                } catch (Exception e) {
                    logger.logError("解析进口船图箱(berthId:" + berthId + ", vLocation:" + vLocation + ", dlType:" + dlType + ")信息过程中发生数据异常！");
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseContainerGroup(List<SmartContainerGroupInfo> smartContainerGroupInfoList, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析属性组信息。");
        WorkingData workingData;
        for (SmartContainerGroupInfo smartContainerGroupInfo : smartContainerGroupInfoList) {
            Long berthId = smartContainerGroupInfo.getBerthId();
            try {
                workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                if (workingData != null) {
                    ContainerGroup containerGroup = new ContainerGroup(berthId, smartContainerGroupInfo.getGroupId());
                    containerGroup.setPortCd(smartContainerGroupInfo.getPortCd());
                    containerGroup.setSize(smartContainerGroupInfo.getCszCsizecd());
                    containerGroup.setType(smartContainerGroupInfo.getCtypeCd());
                    containerGroup.setEfFlag(smartContainerGroupInfo.getEffg());
                    containerGroup.setRfFlag(smartContainerGroupInfo.getRfcfg());
                    containerGroup.setIsHeight(smartContainerGroupInfo.getHeiCheightcd());
                    containerGroup.setDgCd(smartContainerGroupInfo.getUnNo());
                    workingData.addContainerGroup(containerGroup);
                }
            } catch (Exception e) {
                logger.logError("解析(berthId:" + berthId + ")属性组信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseWeightGroup(List<SmartWeightGroupInfo> smartWeightGroupInfoList, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析重量组信息。");
        WorkingData workingData;
        for (SmartWeightGroupInfo smartWeightGroupInfo : smartWeightGroupInfoList) {
            Long berthId = smartWeightGroupInfo.getBerthId();
            try {
                workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                if (workingData != null) {
                    WeightGroup weightGroup = new WeightGroup(berthId, smartWeightGroupInfo.getWeightId());
                    weightGroup.setMinWeight(smartWeightGroupInfo.getMinWeight() * 1000);
                    weightGroup.setMaxWeight(smartWeightGroupInfo.getMaxWeight() * 1000);
                    workingData.addWeightGroup(weightGroup);
                }
            } catch (Exception e) {
                logger.logError("解析(berthId:" + berthId + ")重量组信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseYardContainer(List<SmartYardContainerInfo> smartYardContainerInfoList, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析在场箱信息。");
        WorkingData workingData;
        for (SmartYardContainerInfo smartYardContainerInfo : smartYardContainerInfoList) {
            Long berthId = smartYardContainerInfo.getBerthId();
            Long containerId = smartYardContainerInfo.getContainerId();
            String containerNo = smartYardContainerInfo.getContainerNo();
            try {
                workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
                if (workingData != null) {
                    YardContainer yardContainer = new YardContainer(berthId, containerId, containerNo);
                    yardContainer.setAreaNo(smartYardContainerInfo.getAreaNo());
                    String yLocation = smartYardContainerInfo.getYlocation();
                    yardContainer.setyLocation(yLocation);
                    yardContainer.setType(smartYardContainerInfo.getCtypeCd());
                    yardContainer.setSize(smartYardContainerInfo.getCszCsizecd());
                    yardContainer.setDstPort(smartYardContainerInfo.getDstPort());
                    yardContainer.setWeight(smartYardContainerInfo.getWeight());
                    yardContainer.setDgCd(smartYardContainerInfo.getUnNo());
                    yardContainer.setRfFlag(smartYardContainerInfo.getRfcfg());
                    yardContainer.setEfFlag(smartYardContainerInfo.getEffg());
                    yardContainer.setUnNo(smartYardContainerInfo.getUnNo());
                    yardContainer.setIsHeight(smartYardContainerInfo.getIsHeight());
                    String stowageFlag = smartYardContainerInfo.getStowagfg();
                    stowageFlag = StringUtil.isNotBlank(stowageFlag) ? "Y".equals(stowageFlag) ? StowDomain.YES : StowDomain.NO : StowDomain.NO;
                    yardContainer.setStowageFlag(stowageFlag);
                    String cntType = smartYardContainerInfo.getCntType();
                    yardContainer.setCntType(cntType);
                    yardContainer.setPlanStartWorkTime(smartYardContainerInfo.getPlanStartWorkTime());
                    yardContainer.setPlanEndWorkTime(smartYardContainerInfo.getPlanEndWorkTime());
                    List<Long> cntIndList = smartYardContainerInfo.getWokPlanCntIdList() != null ? smartYardContainerInfo.getWokPlanCntIdList() : new ArrayList<Long>();
                    yardContainer.getWokPlanCntIdList().addAll(cntIndList);
                    //保存数据
                    workingData.addYardContainer(yardContainer);
                }
            } catch (Exception e) {
                logger.logError("解析(berthId:" + berthId + ")在场箱信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }

    private void parseYardContainerWorkTime(List<SmartCntrWorkTimeInfo> smartCntrWorkTimeInfoList, AllRuntimeData allRuntimeData) {
        logger.logInfo("解析在场箱子被ASC作业到支架时间信息。");
        WorkingData workingData;
        for (SmartCntrWorkTimeInfo smartCntrWorkTimeInfo : smartCntrWorkTimeInfoList) {
            try {

            } catch (Exception e) {
//                logger.logError("解析(berthId:" + berthId + ")在场箱子被ASC作业到支架时间信息过程中发生数据异常！");
                e.printStackTrace();
            }
        }
    }


    private void parseStowVesselContainer(List<SmartVesselContainerInfo> smartVesselContainerInfoList, AllRuntimeData allRuntimeData) {
        logger.logError("自动配载，出口船图箱信息", ValidatorUtil.isEmpty(smartVesselContainerInfoList));
        WorkingData workingData;
        for (SmartVesselContainerInfo smartVesselContainerInfo : smartVesselContainerInfoList) {
            String dlType = smartVesselContainerInfo.getLduldfg();
            if ("L".equals(dlType)) {
                Long berthId = smartVesselContainerInfo.getBerthId();
                String vLocation = smartVesselContainerInfo.getvLocation();
                Long vpcCntId = smartVesselContainerInfo.getVpcCntrId();
                String size = smartVesselContainerInfo.getcSzCsizecd();
                String type = smartVesselContainerInfo.getcTypeCd();
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
                        vesselContainer.setWorkFlow(workFlow);
                        vesselContainer.setMoveOrder(moveOrder);
                    }
                } catch (Exception e) {
                    logger.logError("解析进口船图箱(berthId:" + berthId + ", vLocation:" + vLocation + ", dlType:" + dlType + ")信息过程中发生数据异常！");
                    e.printStackTrace();
                }
            }
        }
    }

}
