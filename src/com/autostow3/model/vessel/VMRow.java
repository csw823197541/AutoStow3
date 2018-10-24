package com.autostow3.model.vessel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by csw on 2017/4/20 11:38.
 * Explain: 排信息
 */
public class VMRow {

    private Long bayId;
    private String bayKey;
    private Integer rowNo;

    private Double maxWeight20;//小倍重量负荷
    private Double maxWeight40;// 大倍重量负荷

    private Integer topTierNo;
    private Integer bottomTierNo;

    private Map<Integer, VMSlot> vmSlotMap; //该排有多少层，层号对应的VMSlot信息，键值为层号

    public VMRow(Long bayId, String bayKey, Integer rowNo) {
        this.bayId = bayId;
        this.bayKey = bayKey;
        this.rowNo = rowNo;
        topTierNo = -1;
        bottomTierNo = 1000;
        vmSlotMap = new HashMap<>();
    }

    public boolean hasVMSlot() {
        return vmSlotMap.size() > 0;
    }

    public Long getBayId() {
        return bayId;
    }

    public String getBayKey() {
        return bayKey;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public Double getMaxWeight20() {
        return maxWeight20;
    }

    public void setMaxWeight20(Double maxWeight20) {
        this.maxWeight20 = maxWeight20;
    }

    public Double getMaxWeight40() {
        return maxWeight40;
    }

    public void setMaxWeight40(Double maxWeight40) {
        this.maxWeight40 = maxWeight40;
    }

    public Integer getTopTierNo() {
        return topTierNo;
    }

    public void setTopTierNo(Integer topTierNo) {
        this.topTierNo = topTierNo;
    }

    public Integer getBottomTierNo() {
        return bottomTierNo;
    }

    public void setBottomTierNo(Integer bottomTierNo) {
        this.bottomTierNo = bottomTierNo;
    }

    public void addVMSlot(VMSlot vmSlot) {
        Integer tierNo = vmSlot.getVmPosition().getTierNo();
        vmSlotMap.put(tierNo, vmSlot);
        this.setTopTierNo(tierNo > this.getTopTierNo() ? tierNo : this.getTopTierNo());
        this.setBottomTierNo(tierNo < this.getBottomTierNo() ? tierNo : this.getBottomTierNo());
    }

    public VMSlot getVMSlot(Integer curTierNo) {
        return vmSlotMap.get(curTierNo);
    }
}
