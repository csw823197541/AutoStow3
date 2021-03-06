package com.autostow3.model.stow;

/**
 * Created by csw on 2018/8/21.
 * Description:箱属性分组信息
 */
public class ContainerGroup {

    private Long berthId; //靠泊ID
    private Long groupId; //属性组
    private String portCd; //卸货港
    private String size; //箱尺寸
    private String type; //箱型
    private String efFlag; //空/重标记 （Y/N）
    private String rfFlag; //冷藏箱标记 (Y/N)
    private String isHeight; //箱高(高箱或平箱) (Y/N)
    private String dgCd; //危险品代码

    public ContainerGroup(Long berthId, Long groupId) {
        this.berthId = berthId;
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "groupId：" + groupId + "，卸货港：" + portCd + "，尺寸：" + size + "，箱型：" + type + "，空重：" + efFlag + "，冷藏箱：" + rfFlag + "，高平箱：" + isHeight + "，危险品：" + dgCd;
    }

    public Long getBerthId() {
        return berthId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getPortCd() {
        return portCd;
    }

    public void setPortCd(String portCd) {
        this.portCd = portCd;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getEfFlag() {
        return efFlag;
    }

    public void setEfFlag(String efFlag) {
        this.efFlag = efFlag;
    }

    public String getRfFlag() {
        return rfFlag;
    }

    public void setRfFlag(String rfFlag) {
        this.rfFlag = rfFlag;
    }

    public String getIsHeight() {
        return isHeight;
    }

    public void setIsHeight(String isHeight) {
        this.isHeight = isHeight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDgCd() {
        return dgCd;
    }

    public void setDgCd(String dgCd) {
        this.dgCd = dgCd;
    }
}
