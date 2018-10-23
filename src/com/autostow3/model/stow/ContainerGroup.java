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
    private String efFlag; //空/重标记 （Y/N）
    private String rfFlag; //冷藏箱标记 (Y/N)
    private String isHeight; //箱高(高箱或平箱) (Y/N)
    private String type; //箱型
    private String dgCd; //危险品代码

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
