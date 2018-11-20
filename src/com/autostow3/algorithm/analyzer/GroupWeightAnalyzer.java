package com.autostow3.algorithm.analyzer;

import com.autostow3.algorithm.method.PublicMethod;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.log.Logger;
import com.autostow3.model.stow.VesselContainer;
import com.autostow3.model.stow.YardContainer;
import com.autostow3.model.weight.WeightGroup;
import com.autostow3.utils.ValidatorUtil;

import java.util.*;

/**
 * Created by csw on 2018/10/23.
 * Description:
 */
public class GroupWeightAnalyzer {

    public void initGroupWeightNum(WorkingData workingData) {

        Logger logger = workingData.getLogger();
        List<WeightGroup> weightGroupList = workingData.getAllWeightGroups();
        sortWeightGroupListByMinWeight(weightGroupList);

        //todo: 验证重量等级分组的合理性，检查已划分重量等级的预配位符合要求的在场箱数目够不够
        if (!checkGroupAndWeightCorrect(workingData)) {

        }

        // 设置重量等级分组Seq
        for (int i = 0; i < weightGroupList.size(); i++) {
            weightGroupList.get(i).setWeightSeq(i + 1);
        }
        // 将在场箱进行分组
        Map<Long, Map<Integer, Integer>> groupWeightNumMap = new HashMap<>();
        List<String> noneGroupIdList = new ArrayList<>();
        List<String> noneWeightIdList = new ArrayList<>();
        List<YardContainer> yardContainerList = workingData.getAllCanStowContainerList();
        for (YardContainer yardContainer : yardContainerList) {
            Long groupId = PublicMethod.getContainerGroupId(yardContainer, workingData.getAllContainerGroups());
            if (groupId == null) {
                noneGroupIdList.add(yardContainer.getContainerNo());
            }
            Integer weightSeq = PublicMethod.getContainerWeightId(yardContainer, weightGroupList);
            if (weightSeq == null) {
                noneWeightIdList.add(yardContainer.getContainerNo());
            }
            if (groupId != null && weightSeq != null) {
                if (groupWeightNumMap.get(groupId) == null) {
                    groupWeightNumMap.put(groupId, new TreeMap<Integer, Integer>(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o2.compareTo(o1);
                        }
                    }));
                }
                if (groupWeightNumMap.get(groupId).get(weightSeq) == null) {
                    groupWeightNumMap.get(groupId).put(weightSeq, 0);
                }
                int num = groupWeightNumMap.get(groupId).get(weightSeq) + 1;
                groupWeightNumMap.get(groupId).put(weightSeq, num);
            }
        }
        // 将预配位进行分组
        Map<Long, Integer> groupCntNumMap = new HashMap<>();
        Set<VesselContainer> vesselContainerList = workingData.getAllVesselContainers();
        for (VesselContainer vesselContainer : vesselContainerList) {
            if (groupCntNumMap.get(vesselContainer.getGroupId()) == null) {
                groupCntNumMap.put(vesselContainer.getGroupId(), 0);
            }
            int num = groupCntNumMap.get(vesselContainer.getGroupId()) + 1;
            groupCntNumMap.put(vesselContainer.getGroupId(), num);
        }
        // 验证在场箱属性组数目与预配位属性组数目
        boolean canNotStow = false;
        for (Map.Entry<Long, Integer> entry : groupCntNumMap.entrySet()) {
            int n = 0;
            for (Integer number : groupWeightNumMap.get(entry.getKey()).values()) {
                n += number;
            }
            if (entry.getValue() > n) { //预配数目比在场箱数目大
                canNotStow = true;
                logger.logError("属性组(" + workingData.getContainerGroupById(entry.getKey()).toString() + ")的预配数目(" + entry.getValue() + ")比可配在场箱数目(" + n + ")大，由于缺少可配在场箱，算法无法划分重量等级，请检查预配和可配在场箱！");
            }
            logger.logDebug("属性组(" + workingData.getContainerGroupById(entry.getKey()).toString() + ")的预配数目为：" + entry.getValue() + "，可配在场箱数目为：" + n);
        }
        if (canNotStow) {
            logger.logError("找不到属性组的可配在场箱如下：" + noneGroupIdList.toString(), !ValidatorUtil.isEmpty(noneGroupIdList));
            logger.logError("找不到重量等级的可配在场箱如下：" + noneWeightIdList.toString(), !ValidatorUtil.isEmpty(noneWeightIdList));
        } else { //验证通过
            workingData.setGroupWeightNumMap(groupWeightNumMap);
        }

    }

    // 检查属性组、重量等级与在场箱是否匹配
    public boolean checkGroupAndWeightCorrect(WorkingData workingData) {
        return false;
    }

    private void sortWeightGroupListByMinWeight(List<WeightGroup> weightGroupList) {
        Collections.sort(weightGroupList, new Comparator<WeightGroup>() {
            @Override
            public int compare(WeightGroup o1, WeightGroup o2) {
                return o1.getMinWeight().compareTo(o2.getMinWeight());
            }
        });
    }
}
