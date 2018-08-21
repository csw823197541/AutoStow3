package com.autostow3.test.ViewFrame;


import com.autostow3.data.AllRuntimeData;
import com.autostow3.data.single.StructureData;
import com.autostow3.data.single.WorkingData;
import com.autostow3.model.vessel.VMBay;
import com.autostow3.model.vessel.VMSlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CarloJones on 2018/7/11.
 */
public class HatchPanel1 extends JPanel {
    private static String yLocation = "yLocation";
    private static String moveOrder = "moveOrder";
    private static String dstPort = "dstPort";
    private static String sentSeq = "sentSeq";

    private Long berthId;
    private Long hatchId;
    private String dlType;
    private AllRuntimeData allRuntimeData;
    private StructureData structureData;
    private WorkingData workingData;

    private double scaleRatio = 1.2d;

    private int squareLength = (int) (scaleRatio * 20);
    private Color color20 = Color.PINK;
    private Color color40 = Color.CYAN;
    private Color colorThrough = Color.BLACK;
    private Color danger = Color.red;
    private Color colorEmptySlot = Color.LIGHT_GRAY;
    private Color colorC = Color.BLUE;
    private Color colorW = Color.ORANGE;
    private Color colorSent = new Color(0xFDFF22);
    private Color[] colors = {Color.ORANGE, Color.RED, Color.GREEN};

    private JPanel bayPanel;

//    private Map<String, java.util.List<WorkMove>> workMoveMap;

    public HatchPanel1(Long berthId, Long hatchId, String dlType, AllRuntimeData allRuntimeData) {
        this.berthId = berthId;
        this.hatchId = hatchId;
        this.dlType = dlType;
        this.allRuntimeData = allRuntimeData;
//        workingData = allRuntimeData.getWorkingDataByBerthId(berthId);
//        structureData = allRuntimeData.getStructDataByVesselCode(workingData.getVmSchedule().getVesselCode());
//        this.workMoveMap = workingData.getWorkMoveMap();
        initComponents();
    }

    private void initComponents() {

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        //最上一层菜单按钮
        JPanel bntPanel = new JPanel();
        bntPanel.setBackground(Color.GRAY);

        JButton defaultBnt = new JButton("默认");
        defaultBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBay(new JPanel(), "");
            }
        });
        bntPanel.add(defaultBnt);

        JButton moveOrderBnt = new JButton("作业顺序");
        moveOrderBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBay(new JPanel(), moveOrder);
            }
        });
        bntPanel.add(moveOrderBnt);

        JButton yLocationBnt = new JButton("场箱位");
        yLocationBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBay(new JPanel(), yLocation);
            }
        });
        bntPanel.add(yLocationBnt);

        JButton dstPortBnt = new JButton("目的港");
        dstPortBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBay(new JPanel(), dstPort);
            }
        });
        bntPanel.add(dstPortBnt);

        JButton sentSeqBnt = new JButton("发送指令顺序");
        sentSeqBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBay(new JPanel(), sentSeq);
            }
        });
        sentSeqBnt.setBackground(Color.BLUE);
        bntPanel.add(sentSeqBnt);

        //功能按钮panel
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1;
        gridBagConstraints1.weighty = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        this.add(bntPanel, gridBagConstraints1);

        //倍位图panel
        bayPanel = new JPanel();
        this.add(bayPanel);

        initBay(new JPanel(), "");

    }

    private void initBay(JPanel jPanel, String info) {
//        java.util.List<VMBay> vmBayList = structureData.getVMBayListByHatchId(hatchId);
//        java.util.List<Integer> moBayNos = structureData.getVMHatchByHatchId(hatchId).getBayNos();

        this.remove(bayPanel);

        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.weightx = 1;
        gridBagConstraints2.weighty = 1;
        gridBagConstraints2.fill = GridBagConstraints.BOTH;
        this.add(jPanel, gridBagConstraints2);

//        for (VMBay vmBay : vmBayList) {
//            GridBagConstraints gridBagConstraints = new GridBagConstraints();
//            if (vmBay.getBayNo().equals(moBayNos.get(0))) {//第一个贝
//                gridBagConstraints.gridx = 0;
//            } else {
//                gridBagConstraints.gridx = 1;
//            }
//            if (vmBay.getAboveOrBelow().equals(StowDomain.BOARD_ABOVE)) {
//                gridBagConstraints.gridy = 0;
//            } else {
//                gridBagConstraints.gridy = 1;
//            }
//            gridBagConstraints.fill = GridBagConstraints.BOTH;
//            gridBagConstraints.weightx = 1;
//            gridBagConstraints.weighty = 1;
//
//            jPanel.add(initPanel(vmBay, info), gridBagConstraints);
//        }

        this.validate();
        this.repaint();
        bayPanel = jPanel;
    }

    private JPanel initPanel(VMBay vmBay, String info) {
        Long bayId = vmBay.getBayId();
        Long hatchId = vmBay.getHatchId();
        JPanel jPanel = new JPanel();
        String panelBorderTitle = vmBay.getBayNo() + vmBay.getAboveOrBelow().substring(0, 1) + "-" + bayId;
        jPanel.setBorder(BorderFactory.createTitledBorder(panelBorderTitle));
//        java.util.List<Integer> rowSeqList = structureData.getRowSeqListByOddOrEven(bayId, StowDomain.ROW_SEQ_EVEN_ODD);
//        java.util.List<Integer> hatchRowSeqList = structureData.getRowSeqListBySeaOrLand(hatchId,StowDomain.ROW_SEQ_EVEN_ODD);
        int maxTierNo = vmBay.getMaxTier();
        int minTierNo = vmBay.getMinTier();
        int tierCount = (maxTierNo - minTierNo) / 2 + 1;
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel.setLayout(gridBagLayout);

        //添加箱信息
//        for (int i = 0; i <hatchRowSeqList.size() + 1; i++) {
//            for (int j = 0; j < tierCount + 1; j++) {
//                GridBagConstraints gridBagConstraints = new GridBagConstraints();
//                gridBagConstraints.gridx = i;
//                gridBagConstraints.gridy = j;
//                gridBagConstraints.fill = GridBagConstraints.BOTH;
//                gridBagConstraints.weightx = 1;
//                gridBagConstraints.weighty = 1;
//
//                int curTierNo = maxTierNo - 2 * j;
//                JLabel jLabel = null;
//                if(vmBay.getAboveOrBelow().equals(StowDomain.BOARD_ABOVE)){
//                    //第0列添加层号信息
//                    if (i == 0) {
//                        if (j == tierCount) {   //左下角
//                            jLabel = getSideLabel("左下角");
//                        } else {//显示层号
//                            jLabel = getSideLabel(curTierNo + "");
//                            jLabel.setOpaque(true);
//                        }
//                    } else {
//                        if (j == tierCount) { //最后一排,显示排号
//                            int rowNo = hatchRowSeqList.get(i - 1);
//                            if(rowSeqList.contains(rowNo)){
//                                jLabel = getSideLabel(rowNo + "");
//                            }else{
//                                jLabel = new JLabel("");
//                                jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
//                            }
//                        } else {
//                            int rowNo = hatchRowSeqList.get(i - 1);
//                            VMRow row = structureData.getVMBayByBayId(bayId).getVMRowByRowNo(rowNo);
//                            if(row != null){
//                                VMSlot vmSlot = row.getVMSlot(curTierNo);
//                                jLabel = getJLabel(vmSlot, info);
//                            }else{
//                                jLabel = new JLabel("");
//                                jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
//                            }
//                        }
//
//                    }
//                }else if(vmBay.getAboveOrBelow().equals(StowDomain.BOARD_BELOW)) {
//                    if (i == 0) {
//                        if (j == 0) {
//                            jLabel = getSideLabel("左上角");
//                        } else{//显示层号
//                            jLabel = getSideLabel((curTierNo+2) + "");
//                            jLabel.setOpaque(true);
//                        }
//                    } else {
//                        if (j == 0) { //第一排,显示排号
//                            int rowNo = hatchRowSeqList.get(i-1);
//                            if (rowSeqList.contains(rowNo)) {
//                                jLabel = getSideLabel(rowNo + "");
//                            } else {
//                                jLabel = new JLabel("");
//                                jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
//                            }
//                        } else {
//                            int rowNo = hatchRowSeqList.get(i-1);
//                            VMRow row = structureData.getVMBayByBayId(bayId).getVMRowByRowNo(rowNo);
//                            if (row != null) {
//                                VMSlot vmSlot = row.getVMSlot(curTierNo+2);
//                                jLabel = getJLabel(vmSlot, info);
//                            } else {
//                                jLabel = new JLabel("");
//                                jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
//                            }
//                        }
//                    }
//                }
//
//                jPanel.add(jLabel, gridBagConstraints);
//            }
//        }
        return jPanel;

    }

    private JLabel getJLabel(VMSlot vmSlot, String info) {
        JLabel jLabel = new JLabel();

        jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
//        jLabel.setFont(font);

        if (vmSlot == null) {
//            logger.logDebug("MOSlot 为空");
        } else {
            jLabel.setOpaque(true);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

//            VesselContainer vmContainer = workingData.getVMContainerByVMSlot(vmSlot, dlType);
//            java.util.List<WorkMove> workMoveList = workMoveMap.get(resultName);
//            WorkMove workMove = null;
//            if(vmContainer != null){
//                for(WorkMove wm :workMoveList){
//                    if(wm.getHatchId().equals(vmContainer.getHatchId())
//                            && wm.getVmSlotSet().contains(vmSlot)
//                            && dlType.equals(wm.getDlType())){
//                        workMove = wm;
//                    }
//                }
//            }
//            if (vmContainer == null) {
//                jLabel.setBackground(colorEmptySlot);
//            } else {
//                if (StowDomain.YES.equals(vmContainer.getCwoManualWi())) {
//                    jLabel.setBackground(colorThrough);
//                }else {
//                    if (vmContainer.getSize().startsWith("4")) {
//                        jLabel.setBackground(color40);
//                    }
//                    if (vmContainer.getSize().startsWith("2")) {
//                        jLabel.setBackground(color20);
//                    }
//                    //状态显示
//                    if(!(vmContainer.getDgCd() == null|| vmContainer.getDgCd().equals(StowDomain.DG_NORMAL))){
//                        jLabel.setBackground(danger);
//                    }
//                }
//
//                //根据info显示具体数据
//                if(workMove != null){
//                    StringBuilder str = new StringBuilder();
//                    String order = workMove.getMoveOrder() != null ? workMove.getMoveOrder().toString() : "";
//                    String workFlow = workMove.getWorkFlow() != null ? workMove.getWorkFlow().toLowerCase() : "";
//                    String yLocation = "";
//                    String dstPort = vmContainer.getGroupId() != null ? String.valueOf(vmContainer.getGroupId()) : "";
//                    String sentSeq = "";
//                    if (moveOrder.equals(info)) {
//                        jLabel.setText(order + "-" + workFlow);
//                    } else if (HatchPanel1.yLocation.equals(info)) {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("<html><div>").append(sentSeq).append("<br/>")
//                                .append("o:").append(yLocation).append("<br/>")
//                                .append("</div></html>");
//                        jLabel.setText(sb.toString());
//                    } else if (HatchPanel1.dstPort.equals(info)) {
//                        jLabel.setText(dstPort);
//                    } else if (HatchPanel1.sentSeq.equals(info)) {
//                        jLabel.setText(sentSeq);
//                    } else { //style='color:#000000;font-size:10px;font-family:宋体;'
//                        str.append("<html><div>").append(order).append("-").append(workFlow).append("<br/>")
//                                .append(yLocation).append("<br/>")
//                                .append(vmContainer.getWeightKg()).append("<br/>")
////                            .append(vmContainer.getDstPort()).append("<br/>")
//                                .append("</div></html>");
//                        jLabel.setText(str.toString());
//                    }
//                }
//            }

        }
        return jLabel;
    }

    private JLabel getSideLabel(String text) {
        JLabel jLabel = new JLabel(text, JLabel.CENTER);
        jLabel.setPreferredSize(new Dimension(squareLength, squareLength));
        return jLabel;
    }
}
