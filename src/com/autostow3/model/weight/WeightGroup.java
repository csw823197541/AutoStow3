package com.autostow3.model.weight;

/**
 * Created by csw on 2018/8/21.
 * Description: 箱重量等级分组信息
 */
public class WeightGroup {

    private Long berthId;//靠泊ID
    private Long weightId; //重量等级ID
    private Double minWeight; //最小重量
    private Double maxWeight; //最大重量

    public WeightGroup(Long berthId, Long weightId) {
        this.berthId = berthId;
        this.weightId = weightId;
    }

    public Long getBerthId() {
        return berthId;
    }

    public void setBerthId(Long berthId) {
        this.berthId = berthId;
    }

    public Long getWeightId() {
        return weightId;
    }

    public void setWeightId(Long weightId) {
        this.weightId = weightId;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }
}
