package com.autostow3.algorithm.method;

import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.stow.ContainerGroup;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.stow.YardContainer;
import com.autostow3.model.weight.WeightGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description: 重量等级划分和配载共用的静态处理方法
 */
public class PublicMethod {

    public static boolean canDrawLevelCnt(VesselContainer vesselContainer) {
        return StowDomain.THROUGH_NO.equals(vesselContainer.getThroughFlag()) && StowDomain.NO.equals(vesselContainer.getFixedWeightLevel());
    }

    public static Long getContainerGroupId(YardContainer yardContainer, List<ContainerGroup> containerGroupList) {
        for (ContainerGroup containerGroup : containerGroupList) {
            if (compareContainerGroup(yardContainer, containerGroup)) {
                return containerGroup.getGroupId();
            }
        }
        return null;
    }

    private static boolean compareContainerGroup(YardContainer yardContainer, ContainerGroup containerGroup) {
        if (compareType(yardContainer.getType(), containerGroup.getType())) {
            if (yardContainer.getDstPort().equals(containerGroup.getPortCd())) { //todo: 父子港口问题，看这个目的港有没有父港口，按父港口找groupId
                if (yardContainer.getSize().equals(containerGroup.getSize())) {
                    if (yardContainer.getEfFlag().equals(containerGroup.getEfFlag())) {
                        if (yardContainer.getRfFlag().equals(containerGroup.getRfFlag())) {
                            if (yardContainer.getIsHeight().equals(containerGroup.getIsHeight())) {
                                return yardContainer.getDgCd().equals(containerGroup.getDgCd());
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean compareType(String cntType, String groupType) {
        if (groupType.contains(",")) {
            List<String> typeList = Arrays.asList(groupType.split(","));
            return typeList.contains(cntType);
        } else {
            return cntType.equals(groupType);
        }
    }

    public static Integer getContainerWeightId(YardContainer yardContainer, List<WeightGroup> weightGroupList) {
        for (WeightGroup weightGroup : weightGroupList) { //todo: 0-5,5-10，5属于哪个等级
            if ((yardContainer.getWeight().compareTo(weightGroup.getMinWeight()) > 0) && (yardContainer.getWeight().compareTo(weightGroup.getMaxWeight()) <= 0)) {
                return weightGroup.getWeightSeq();
            }
        }
        return null;
    }
}
