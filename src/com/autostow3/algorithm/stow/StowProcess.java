package com.autostow3.algorithm.stow;

import com.autostow3.algorithm.analyzer.GroupWeightAnalyzer;
import com.autostow3.algorithm.analyzer.StowAnalyzer;
import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.log.Logger;
import com.autostow3.model.result.StowMove;
import com.autostow3.model.stow.VesselContainer;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2018/8/21.
 * Description:
 */
public class StowProcess {

    private AllRuntimeData allRuntimeData; //其它船的相关数据
    private WorkingData workingData; //当前需要配载的船相关的输入数据
    private GroupWeightAnalyzer groupWeightAnalyzer;
    private StowAnalyzer stowAnalyzer;

    public StowProcess(AllRuntimeData allRuntimeData, Long berthId) {
        this.allRuntimeData = allRuntimeData;
        this.workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
        groupWeightAnalyzer = new GroupWeightAnalyzer();
        stowAnalyzer = new StowAnalyzer();
    }

    public void processCwp() {
        Logger logger = workingData.getLogger();
        logger.logInfo("开始执行...");
        long st = System.currentTimeMillis();


        // 1、属性组、重量等级与可配在场箱数目检查
        groupWeightAnalyzer.checkGroupAndWeightCorrect(workingData);


        // 配载前数据处理：连续作业单调箱拼成双箱吊、箱区每小时能力初始化
        Set<VesselContainer> vesselContainers = workingData.getAllVesselContainers();

        List<StowMove> stowMoveList = stowAnalyzer.changeToStowMove(vesselContainers);

        // 检查配载结果是否合理，主要检查槽重，进行配载结果调整
        while (!checkAndAdjustStowResult(stowMoveList, workingData)) {
            // 2.1 预配位按时间排序
            // 2.2 选择候选箱：依据berthId、groupId、weightId选择候选箱
            // 2.2 根据箱区16分钟出4箱能力筛选能出箱的箱子，考虑到双箱吊出箱的特殊情况
            // 2.3 对候选箱子进行打分：
            //      计算翻箱时间+
            //      计算AGV和ASC消耗的时间+
            //      计算initialTime，箱子到达桥机的时间点与CWP计划作业时间点的差值，（绝对值越小越接近计划作业时间）
            //      计算同排在场箱配到船上同一个舱里面的分值
            // 2.4 选择分值最合适的箱子作为配载结果，深度和宽度搜索最优配载箱
            // 2.5 更新箱区出箱能力值

            // 2.6 所有箱子配完，检查超重负荷，先计算槽重正负差值，同属性组箱子交换，交换后箱子不能超过槽重值
            // 2.7 重新执行配载，没有超过槽重值的情况结束配载，得到最优结果
        }

        long et = System.currentTimeMillis();
        logger.logInfo("执行结束，执行时间是：" + (et - st) / 1000 + "秒");
    }

    private boolean checkAndAdjustStowResult(List<StowMove> stowMoveList, WorkingData workingData) {
        // 取每根槽的船箱位对应配载结果，计算配载箱子的重量，与槽重进行对比，检查是否超过槽重

        // 1、超过槽重的依据“交换原则”调整船箱位对应的配载结果
        // 2、槽重调整后检查同属性组重压轻情况，调整重压轻
        return false;
    }
}
