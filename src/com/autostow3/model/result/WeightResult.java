package com.autostow3.model.result;

/**
 * Created by csw on 2018/10/23.
 * Description:
 */
public class WeightResult {

    private String vLocation; //船箱位

    private Long berthId; //靠泊ID
    private Long voyageId; //航次ID
    private Long hatchId; //舱ID
    private Long weightId; //重量等级ID
    private Long groupId; //属性组
    private String ldFlag; //装卸船标志
    private Boolean fixedWeightLevel; //是否指定重量等级
    private String modifyState;//重量等级修改状态，即是否为算法新增的字段：autoStowWeight表示新增重量等级信息

    public WeightResult(String vLocation) {
        this.vLocation = vLocation;
    }

    public String getvLocation() {
        return vLocation;
    }

    public void setvLocation(String vLocation) {
        this.vLocation = vLocation;
    }

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public Long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Long voyageId) {
        this.voyageId = voyageId;
    }

    public Long getHatchId() {
        return hatchId;
    }

    public void setHatchId(Long hatchId) {
        this.hatchId = hatchId;
    }

    public Long getWeightId() {
        return weightId;
    }

    public void setWeightId(Long weightId) {
        this.weightId = weightId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getLdFlag() {
        return ldFlag;
    }

    public void setLdFlag(String ldFlag) {
        this.ldFlag = ldFlag;
    }

    public Boolean getFixedWeightLevel() {
        return fixedWeightLevel;
    }

    public void setFixedWeightLevel(Boolean fixedWeightLevel) {
        this.fixedWeightLevel = fixedWeightLevel;
    }

    public String getModifyState() {
        return modifyState;
    }

    public void setModifyState(String modifyState) {
        this.modifyState = modifyState;
    }
}
