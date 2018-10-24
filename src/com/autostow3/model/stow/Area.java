package com.autostow3.model.stow;

import java.util.Map;

/**
 * Created by csw on 2018/8/21.
 * Description: 箱区信息
 */
public class Area {

    private String areaNo; //箱区号，主键
    private String ascNo; //机械号
    private String ascStatus;//作业状态，正常、故障
    private String ascMoveStatus; //是否可以移动
    private Double ascSpeed;//吊机移动速度,海陆侧一样
    private String ascType;//箱区吊机的类型
    private Integer workEfficiency;//吊机的机械效率  箱/小时
    private Integer suggestWorkEfficiency;//吊机的建议机械效率  箱/小时
    private String sideType; //asc是在陆侧还是海侧，W：海侧；L：陆侧，我们只需要海测ASC信息
    private Map<Integer, Long> bayNoDistanceTime; //箱区到倍位的距离时间

    public Area(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public String getAscNo() {
        return ascNo;
    }

    public void setAscNo(String ascNo) {
        this.ascNo = ascNo;
    }

    public String getAscStatus() {
        return ascStatus;
    }

    public void setAscStatus(String ascStatus) {
        this.ascStatus = ascStatus;
    }

    public String getAscMoveStatus() {
        return ascMoveStatus;
    }

    public void setAscMoveStatus(String ascMoveStatus) {
        this.ascMoveStatus = ascMoveStatus;
    }

    public Double getAscSpeed() {
        return ascSpeed;
    }

    public void setAscSpeed(Double ascSpeed) {
        this.ascSpeed = ascSpeed;
    }

    public String getAscType() {
        return ascType;
    }

    public void setAscType(String ascType) {
        this.ascType = ascType;
    }

    public Integer getWorkEfficiency() {
        return workEfficiency;
    }

    public void setWorkEfficiency(Integer workEfficiency) {
        this.workEfficiency = workEfficiency;
    }

    public Integer getSuggestWorkEfficiency() {
        return suggestWorkEfficiency;
    }

    public void setSuggestWorkEfficiency(Integer suggestWorkEfficiency) {
        this.suggestWorkEfficiency = suggestWorkEfficiency;
    }

    public String getSideType() {
        return sideType;
    }

    public void setSideType(String sideType) {
        this.sideType = sideType;
    }

    public Map<Integer, Long> getBayNoDistanceTime() {
        return bayNoDistanceTime;
    }

    public void setBayNoDistanceTime(Map<Integer, Long> bayNoDistanceTime) {
        this.bayNoDistanceTime = bayNoDistanceTime;
    }
}
