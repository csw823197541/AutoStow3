package com.autostow3.model.stow;

/**
 * Created by csw on 2018/8/21.
 * Description:港口分组信息
 */
public class PortGroup {

    private Long berthId; //靠泊ID
    private Long voyageId; //航次ID
    private String  parentPortCd; //父卸货港，只有一个
    private String childPortCd; //子卸货港，可能多个，会写成多条记录

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

    public String getParentPortCd() {
        return parentPortCd;
    }

    public void setParentPortCd(String parentPortCd) {
        this.parentPortCd = parentPortCd;
    }

    public String getChildPortCd() {
        return childPortCd;
    }

    public void setChildPortCd(String childPortCd) {
        this.childPortCd = childPortCd;
    }
}
