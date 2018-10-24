package com.autostow3.model.config;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class StowConfig {

    private Long berthId; //靠泊Id
    private String reverseLoadFlag; //舱内倒配参数
    private Integer aboveWeightCntTier; //甲板上配重箱的层数，0表示不需要，1-10代表有多少层需要配重箱，默认是1
    private Integer tailLightCntBayNo; //从这个倍位号开始都配轻箱，大倍位号2、6，默认不需要考虑此参数时，则设置超过船舶最大倍位号：1000
    private String tailBayHatchWeightCntFlag; //尾舱划分轻的情况下，对应这几个舱的甲板下是否划重箱，默认为false

    private String twinWeightDifference; // 双箱吊两个箱子的重量差
    private String twinHeightDifference; //双箱吊两个箱子的高度差
    private Integer underWeightDifference; //舱内允许重量差
    private Integer aboveWeightDifference; //甲板上允许重压轻参数
    private Integer turnOverCntTime; //翻一个箱子的倒箱时间
    private Integer ascWorkCntTime; //Asc作业一个箱子的时间

    public StowConfig(Long berthId) {
        this.berthId = berthId;
    }

    public Long getBerthId() {
        return berthId;
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

    public String getReverseLoadFlag() {
        return reverseLoadFlag;
    }

    public void setReverseLoadFlag(String reverseLoadFlag) {
        this.reverseLoadFlag = reverseLoadFlag;
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

    public Integer getAboveWeightCntTier() {
        return aboveWeightCntTier;
    }

    public void setAboveWeightCntTier(Integer aboveWeightCntTier) {
        this.aboveWeightCntTier = aboveWeightCntTier;
    }

    public Integer getTailLightCntBayNo() {
        return tailLightCntBayNo;
    }

    public void setTailLightCntBayNo(Integer tailLightCntBayNo) {
        this.tailLightCntBayNo = tailLightCntBayNo;
    }

    public String getTailBayHatchWeightCntFlag() {
        return tailBayHatchWeightCntFlag;
    }

    public void setTailBayHatchWeightCntFlag(String tailBayHatchWeightCntFlag) {
        this.tailBayHatchWeightCntFlag = tailBayHatchWeightCntFlag;
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
