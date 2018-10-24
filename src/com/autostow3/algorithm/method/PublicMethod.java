package com.autostow3.algorithm.method;

import com.autostow3.data.single.StructureData;
import com.autostow3.model.domain.StowDomain;
import com.autostow3.model.stow.VesselContainer;

import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description: 重量等级划分和配载共用的静态处理方法
 */
public class PublicMethod {


    public static boolean canDrawLevelCnt(VesselContainer vesselContainer) {
        return StowDomain.THROUGH_NO.equals(vesselContainer.getThroughFlag()) && StowDomain.NO.equals(vesselContainer.getFixedWeightLevel());
    }
}
