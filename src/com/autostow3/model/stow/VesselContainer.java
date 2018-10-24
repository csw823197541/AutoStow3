package com.autostow3.model.stow;

import java.util.Date;

/**
 * Created by csw on 2017/4/20 11:36.
 * Explain: 预配信息
 */
public class VesselContainer {

    private Long vpcCntId; //箱指令唯一编号(指令模块专用)

    private String vLocation; //船箱位
    private String dlType; //装卸标识
    private String containerNo; //箱号，可空
    private Long groupId;       //属性组
    private Long weightId;       //重量组
    private Long cntWorkTime; //人工设置的箱子作业效率，时间（s）
    private String lockFlag; //预配位是否锁住标记，解析生成
    private String fixedWeightLevel; //是否指定重量等级
    private String throughFlag; //过境箱标记

    private String type; //箱型，决定了高度，以及其他一些属性
    private String size; //箱尺寸
    private String portCd; //卸货港
    private String efFlag; //E or F，箱空重
    private String rfFlag;	//冷藏箱标记
    private String dgCd; //危险品代码
    private String isHeight; // 是否高箱
    private String cntHeight; //箱子的具体高度
    private String overrunCd; //超限代码
    private Double weightKg; //重量（kg）

    private String craneNo; //桥机号
    private String workFlow; //作业工艺
    private Long moveOrder; //作业顺序
    private Date workingStartTime; //计划开始作业时间
    private Date workingEndTime; //计划结束作业时间

    private Long berthId; //靠泊Id
    private Long hatchId; //箱子所在舱

    public VesselContainer(String vLocation, String dlType) {
        this.vLocation = vLocation;
        this.dlType = dlType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VesselContainer) {
            VesselContainer vesselContainer = (VesselContainer) obj;
            return vLocation.equals(vesselContainer.getvLocation());
        } else {
            return false;
        }
    }

    public Long getVpcCntId() {
        return vpcCntId;
    }

    public void setVpcCntId(Long vpcCntId) {
        this.vpcCntId = vpcCntId;
    }

    public String getvLocation() {
        return vLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDlType() {
        return dlType;
    }

    public void setDlType(String dlType) {
        this.dlType = dlType;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getWeightId() {
        return weightId;
    }

    public void setWeightId(Long weightId) {
        this.weightId = weightId;
    }

    public Long getCntWorkTime() {
        return cntWorkTime;
    }

    public void setCntWorkTime(Long cntWorkTime) {
        this.cntWorkTime = cntWorkTime;
    }

    public String getThroughFlag() {
        return throughFlag;
    }

    public void setThroughFlag(String throughFlag) {
        this.throughFlag = throughFlag;
    }

    public String getRfFlag() {
        return rfFlag;
    }

    public void setRfFlag(String rfFlag) {
        this.rfFlag = rfFlag;
    }

    public String getEfFlag() {
        return efFlag;
    }

    public void setEfFlag(String efFlag) {
        this.efFlag = efFlag;
    }

    public String getDgCd() {
        return dgCd;
    }

    public void setDgCd(String dgCd) {
        this.dgCd = dgCd;
    }

    public String getIsHeight() {
        return isHeight;
    }

    public void setIsHeight(String isHeight) {
        this.isHeight = isHeight;
    }

    public String getCntHeight() {
        return cntHeight;
    }

    public void setCntHeight(String cntHeight) {
        this.cntHeight = cntHeight;
    }

    public String getOverrunCd() {
        return overrunCd;
    }

    public void setOverrunCd(String overrunCd) {
        this.overrunCd = overrunCd;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getCraneNo() {
        return craneNo;
    }

    public void setCraneNo(String craneNo) {
        this.craneNo = craneNo;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public Long getMoveOrder() {
        return moveOrder;
    }

    public void setMoveOrder(Long moveOrder) {
        this.moveOrder = moveOrder;
    }

    public Date getWorkingStartTime() {
        return workingStartTime;
    }

    public void setWorkingStartTime(Date workingStartTime) {
        this.workingStartTime = workingStartTime;
    }

    public Date getWorkingEndTime() {
        return workingEndTime;
    }

    public void setWorkingEndTime(Date workingEndTime) {
        this.workingEndTime = workingEndTime;
    }

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public Long getHatchId() {
        return hatchId;
    }

    public void setHatchId(Long hatchId) {
        this.hatchId = hatchId;
    }

    public String getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(String lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getFixedWeightLevel() {
        return fixedWeightLevel;
    }

    public void setFixedWeightLevel(String fixedWeightLevel) {
        this.fixedWeightLevel = fixedWeightLevel;
    }

    public String getPortCd() {
        return portCd;
    }

    public void setPortCd(String portCd) {
        this.portCd = portCd;
    }
}
