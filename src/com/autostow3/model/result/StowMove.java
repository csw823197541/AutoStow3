package com.autostow3.model.result;

import com.autostow3.model.stow.VesselContainer;

import java.util.Date;
import java.util.Set;

/**
 * Created by csw on 2018/10/23.
 * Description: 双箱成双一起考虑配载
 */
public class StowMove {

    private String craneNo; //桥机号
    private String workFlow; //作业工艺
    private Long moveOrder; //作业顺序
    private Date workingStartTime; //计划开始作业时间
    private Date workingEndTime; //计划结束作业时间

    private Set<VesselContainer> vesselContainerSet; // 预配信息，第二次配载的时候改变重量等级信息

    private Set<Long> yardCntIdSet; // 配载结果，配载选择的在场箱子

}
