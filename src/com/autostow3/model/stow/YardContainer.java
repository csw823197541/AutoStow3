package com.autostow3.model.stow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class YardContainer {

    private Long berthId; //靠泊ID
    private Long containerId; //唯一箱id
    private String containerNo; //箱号
    private String areaNo; //箱所在箱区号
    private String yLocation; //场箱位
    private String type; //箱型
    private String size; //尺寸
    private String dstPort; //目的港
    private Double weight; //箱重
    private String dgCd; //是否危险品(Y,N)
    private String rfFlag; //是否冷藏(Y,N)
    private String efFlag;// 空/重标记
    private String UnNo; //危险品联合国编号
    private String isHeight; // 是否高箱
    private String heightCd; //箱高代码
    private Long groupId;

    private String stowageFlag; //是否可配载
    private String cntType; //箱子状态，DOC表示资料箱子
    private Integer ascWorkCntTime; //ASC作业该箱子的时间，理解成由箱区到支架上的时间

    private Date planStartWorkTime; //计划开始作业时间
    private Date planEndWorkTime; //计划结束作业时间
    private Integer workPlanCntNumber; //该箱子上面压着的的箱子数量
    private List<Long> wokPlanCntIdList; //该箱子上面压着的的箱子的箱Id,可以找到在场箱具体信息

    public YardContainer(Long berthId, Long containerId, String containerNo) {
        this.berthId = berthId;
        this.containerId = containerId;
        this.containerNo = containerNo;
        wokPlanCntIdList = new ArrayList<>();
    }

    public Long getBerthId() {
        return berthId;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getyLocation() {
        return yLocation;
    }

    public void setyLocation(String yLocation) {
        this.yLocation = yLocation;
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

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDgCd() {
        return dgCd;
    }

    public void setDgCd(String dgCd) {
        this.dgCd = dgCd;
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

    public String getUnNo() {
        return UnNo;
    }

    public void setUnNo(String unNo) {
        UnNo = unNo;
    }

    public String getIsHeight() {
        return isHeight;
    }

    public void setIsHeight(String isHeight) {
        this.isHeight = isHeight;
    }

    public String getHeightCd() {
        return heightCd;
    }

    public void setHeightCd(String heightCd) {
        this.heightCd = heightCd;
    }

    public String getStowageFlag() {
        return stowageFlag;
    }

    public void setStowageFlag(String stowageFlag) {
        this.stowageFlag = stowageFlag;
    }

    public String getCntType() {
        return cntType;
    }

    public void setCntType(String cntType) {
        this.cntType = cntType;
    }

    public Date getPlanStartWorkTime() {
        return planStartWorkTime;
    }

    public void setPlanStartWorkTime(Date planStartWorkTime) {
        this.planStartWorkTime = planStartWorkTime;
    }

    public Date getPlanEndWorkTime() {
        return planEndWorkTime;
    }

    public void setPlanEndWorkTime(Date planEndWorkTime) {
        this.planEndWorkTime = planEndWorkTime;
    }

    public Integer getWorkPlanCntNumber() {
        return workPlanCntNumber;
    }

    public void setWorkPlanCntNumber(Integer workPlanCntNumber) {
        this.workPlanCntNumber = workPlanCntNumber;
    }

    public List<Long> getWokPlanCntIdList() {
        return wokPlanCntIdList;
    }

    public void setWokPlanCntIdList(List<Long> wokPlanCntIdList) {
        this.wokPlanCntIdList = wokPlanCntIdList;
    }

    public Integer getAscWorkCntTime() {
        return ascWorkCntTime;
    }

    public void setAscWorkCntTime(Integer ascWorkCntTime) {
        this.ascWorkCntTime = ascWorkCntTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
