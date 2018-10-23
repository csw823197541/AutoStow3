package com.autostow3.model.config;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class AutoStowConfig {

    private Long berthId; //靠泊Id
    private String twinWeightDifference;                    // 双箱吊两个箱子的重量差
    private String twinHeightDifference;                    //双箱吊两个箱子的高度差
    private Boolean reverseLoad;//舱内倒配参数
    private Integer underWeightDifference; //舱内允许重量差
    private Integer aboveWeightDifference; //甲板上允许重压轻参数
    private Integer realSearchWidth; //实际搜索宽度
    private Integer realSearchDepth; //实际搜索深度
    private Integer aboveTier; //甲板上配重箱的层数，0表示不需要，1-10代表有多少层需要配重箱，默认是1
    private Integer hatchTail; //从这个倍位号开始都配轻箱，大倍位号2、6，默认不需要考虑此参数时，设置超过船舶最大倍位号：1000
    private Boolean underDeckPriority; //尾舱划分轻的情况下，对应这几个舱的甲板下是否划重箱，默认为false
    private Integer turnOverCntTime; //翻一个箱子的倒箱时间
    private Integer ascWorkCntTime; //Asc作业一个箱子的时间

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public String getTwinWeightDifference() {
        return twinWeightDifference;
    }

    public void setTwinWeightDifference(String twinWeightDifference) {
        this.twinWeightDifference = twinWeightDifference;
    }

    public String getTwinHeightDifference() {
        return twinHeightDifference;
    }

    public void setTwinHeightDifference(String twinHeightDifference) {
        this.twinHeightDifference = twinHeightDifference;
    }

    public Boolean getReverseLoad() {
        return reverseLoad;
    }

    public void setReverseLoad(Boolean reverseLoad) {
        this.reverseLoad = reverseLoad;
    }

    public Integer getUnderWeightDifference() {
        return underWeightDifference;
    }

    public void setUnderWeightDifference(Integer underWeightDifference) {
        this.underWeightDifference = underWeightDifference;
    }

    public Integer getAboveWeightDifference() {
        return aboveWeightDifference;
    }

    public void setAboveWeightDifference(Integer aboveWeightDifference) {
        this.aboveWeightDifference = aboveWeightDifference;
    }

    public Integer getRealSearchWidth() {
        return realSearchWidth;
    }

    public void setRealSearchWidth(Integer realSearchWidth) {
        this.realSearchWidth = realSearchWidth;
    }

    public Integer getRealSearchDepth() {
        return realSearchDepth;
    }

    public void setRealSearchDepth(Integer realSearchDepth) {
        this.realSearchDepth = realSearchDepth;
    }

    public Integer getAboveTier() {
        return aboveTier;
    }

    public void setAboveTier(Integer aboveTier) {
        this.aboveTier = aboveTier;
    }

    public Integer getHatchTail() {
        return hatchTail;
    }

    public void setHatchTail(Integer hatchTail) {
        this.hatchTail = hatchTail;
    }

    public Boolean getUnderDeckPriority() {
        return underDeckPriority;
    }

    public void setUnderDeckPriority(Boolean underDeckPriority) {
        this.underDeckPriority = underDeckPriority;
    }

    public Integer getTurnOverCntTime() {
        return turnOverCntTime;
    }

    public void setTurnOverCntTime(Integer turnOverCntTime) {
        this.turnOverCntTime = turnOverCntTime;
    }

    public Integer getAscWorkCntTime() {
        return ascWorkCntTime;
    }

    public void setAscWorkCntTime(Integer ascWorkCntTime) {
        this.ascWorkCntTime = ascWorkCntTime;
    }
}
