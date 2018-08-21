package com.autostow3.test.ViewFrame;

import com.autostow3.data.AllRuntimeData;
import com.autostow3.service.parse.ParseDataService;
import com.autostow3.service.stow.StowService;
import com.shbtos.biz.smart.cwp.pojo.*;
import com.shbtos.biz.smart.cwp.service.SmartStowImportData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2016/12/13 14:48.
 * Explain:
 */
public class ImportDataFrameAllShip extends JFrame {

    private JPanel mainPanel = null;
    private JPanel btnPanel = null;
    private CardLayout card = null;
    private JButton voyCraneBnt = null, vesselStructBnt = null, preStowBnt = null, cwpResultBnt;
    private JDesktopPane vesselStructPane = null, preStowPane = null;
    private JDesktopPane voyCranePane = null;
    private JDesktopPane cwpResultPane = null;

    private JButton executeBnt, moBnt, reWorkBnt;
    private JLabel selectVesselLabel;
    private JLabel selectVessel;
    private String vesselCodeStr;
    private String berthIdStr;

    private AllRuntimeData allRuntimeData;
    private SmartStowImportData smartStowImportData;

    public ImportDataFrameAllShip(SmartStowImportData smartStowImportData) {
        this.smartStowImportData = smartStowImportData;
        initComponents();
    }

    private void initComponents() {

        this.setTitle("算法数据导入界面");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GlobalData.width, GlobalData.height);
        this.setResizable(true);
        this.setLocationRelativeTo(null);// 居中显示

        selectVesselLabel = new JLabel("选择了哪条船: ");
        selectVessel = new JLabel("");

        moBnt = new JButton("调用重量等级");
        moBnt.setBackground(Color.BLUE);
        moBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GlobalData.selectedBerthId != null) {

                } else {
                    System.out.println("请选择相应的航次信息！");
                }
            }
        });

        executeBnt = new JButton("调用自动配载");
        executeBnt.setBackground(Color.GREEN);
        executeBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GlobalData.selectedBerthId != null) {
                    ParseDataService parseDataService = new ParseDataService();
                    allRuntimeData = parseDataService.parseAllRuntimeData(smartStowImportData);
                    StowService stowService = new StowService();
                    stowService.doAutoStow(allRuntimeData, GlobalData.selectedBerthId);

                } else {
                    System.out.println("请选择相应的航次信息！");
                }

            }
        });

        reWorkBnt = new JButton("调用多船配载");
        reWorkBnt.setBackground(Color.CYAN);
        reWorkBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        voyCraneBnt = new JButton("航次桥机");
        vesselStructBnt = new JButton("船舶结构");
        preStowBnt = new JButton("预配数据");
        cwpResultBnt = new JButton("CWP结果");

        voyCraneBnt.setMargin(new Insets(2, 2, 2, 2));
        vesselStructBnt.setMargin(new Insets(2, 2, 2, 2));
        preStowBnt.setMargin(new Insets(2, 2, 2, 2));

        btnPanel = new JPanel();
        btnPanel.setBackground(Color.LIGHT_GRAY);
        btnPanel.add(selectVesselLabel);
        btnPanel.add(selectVessel);
        btnPanel.add(voyCraneBnt);
        btnPanel.add(vesselStructBnt);
        btnPanel.add(preStowBnt);
        btnPanel.add(moBnt);
        btnPanel.add(executeBnt);
        btnPanel.add(reWorkBnt);
        btnPanel.add(cwpResultBnt);

        card = new CardLayout(0, 0);
        mainPanel = new JPanel(card);
        voyCranePane = new JDesktopPane();
        vesselStructPane = new JDesktopPane();
        preStowPane = new JDesktopPane();
        cwpResultPane = new JDesktopPane();
        voyCranePane.setBackground(Color.LIGHT_GRAY);
        vesselStructPane.setBackground(Color.CYAN);
        preStowPane.setBackground(Color.GRAY);
        cwpResultPane.setBackground(Color.GRAY);
        mainPanel.add(voyCranePane, "p1");
        mainPanel.add(vesselStructPane, "p2");
        mainPanel.add(preStowPane, "p3");
        mainPanel.add(cwpResultPane, "p4");
        voyCraneBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p1");
            }
        });
        vesselStructBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p2");
            }
        });
        preStowBnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p3");
            }
        });
        cwpResultBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(mainPanel, "p4");
            }
        });

        //----航次桥机信息----
        final BaseFrame scheduleInfoFrame = new BaseFrame("航次信息", SmartScheduleIdInfo.class, smartStowImportData.getSmartScheduleIdInfoList());
        scheduleInfoFrame.setSize(GlobalData.width - 50, GlobalData.height - 400);
        scheduleInfoFrame.setVisible(true);
        //通过选择航次，改变全局传入数据
        scheduleInfoFrame.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = scheduleInfoFrame.table;
                if (e.getClickCount() == 1) {
                    vesselCodeStr = table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("vesselCode")).toString();
                    berthIdStr = table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("berthId")).toString();
                    selectVessel.setText(vesselCodeStr);
                    GlobalData.selectedVesselCode = vesselCodeStr;
                    GlobalData.selectedBerthId = Long.valueOf(berthIdStr);
                    GlobalData.noticeValueChanged();
                    //得到相应船舶的数据
                } else if (e.getClickCount() == 2) {
                    selectVessel.setText("");
                    berthIdStr = "";
                    GlobalData.selectedVesselCode = "";
                    GlobalData.selectedBerthId = null;
                    GlobalData.noticeValueChanged();
                }
            }
        });

//        BaseFrame cranePoolInfoFrame = new BaseFrame("桥机池信息", SmartCranePoolInfo.class, smartStowImportData.getSmartCranePoolInfoList());
//        cranePoolInfoFrame.setVisible(true);
//        voyCranePane.add(cranePoolInfoFrame);
//        BaseFrame vesselCranePoolInfoFrame = new BaseFrame("船舶桥机池信息", SmartVesselCranePoolInfo.class, smartStowImportData.getSmartVesselCranePoolInfoList());
//        vesselCranePoolInfoFrame.setVisible(true);
//        voyCranePane.add(vesselCranePoolInfoFrame);
//        BaseFrame craneMaintainInfoFrame = new BaseFrame("桥机维修计划信息", SmartCraneMaintainPlanInfo.class, smartStowImportData.getSmartCraneMaintainPlanInfoList());
//        craneMaintainInfoFrame.setVisible(true);
//        voyCranePane.add(craneMaintainInfoFrame);
//        BaseFrame cranePlanInfoFrame = new BaseFrame("桥机计划工作信息", SmartCranePlanInfo.class, smartStowImportData.getSmartCranePlanInfoList());
//        cranePlanInfoFrame.setVisible(true);
//        voyCranePane.add(cranePlanInfoFrame);
        BaseFrame cwpConfigurationInfFrame = new BaseFrame("配载参数", SmartStowageConfigurationInfo.class, smartStowImportData.getSmartStowageConfigurationInfoList());
        cwpConfigurationInfFrame.setSize(GlobalData.width - 50, GlobalData.height - 600);
        cwpConfigurationInfFrame.setVisible(true);
        voyCranePane.add(cwpConfigurationInfFrame);

        //----船舶结构信息----
        BaseFrame hatchInfoFrame = new BaseFrame("舱信息", SmartVpsVslHatchsInfo.class, smartStowImportData.getSmartVpsVslHatchsInfoList());
        hatchInfoFrame.setVisible(true);
        vesselStructPane.add(hatchInfoFrame);
        BaseFrame bayInfoFrame = new BaseFrame("倍信息", SmartVpsVslBaysInfo.class, smartStowImportData.getSmartVpsVslBaysInfoList());
        bayInfoFrame.setVisible(true);
        vesselStructPane.add(bayInfoFrame);
        BaseFrame rowInfoFrame = new BaseFrame("排信息", SmartVpsVslRowsInfo.class, smartStowImportData.getSmartVpsVslRowsInfoList());
        rowInfoFrame.setVisible(true);
        vesselStructPane.add(rowInfoFrame);
        BaseFrame locationInfoFrame = new BaseFrame("船箱位信息", SmartVpsVslLocationsInfo.class, smartStowImportData.getSmartVpsVslLocationsInfoList());
        locationInfoFrame.setVisible(true);
        vesselStructPane.add(locationInfoFrame);

        try {
            cwpConfigurationInfFrame.setIcon(true);
            bayInfoFrame.setIcon(true);
            rowInfoFrame.setIcon(true);
            locationInfoFrame.setIcon(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        //----预配信息-----
        final BaseFrame containerInfoFrame = new BaseFrame("船箱信息", SmartVesselContainerInfo.class, smartStowImportData.getSmartVesselContainerInfoList());
        containerInfoFrame.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = containerInfoFrame.table;
                if (e.getClickCount() == 2) {
                    Long hatchId = Long.valueOf(table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("hatchId")).toString());
                    HatchFrame hatchFrame = new HatchFrame(GlobalData.selectedBerthId, hatchId, "D", allRuntimeData);
                    hatchFrame.setVisible(true);
                    HatchFrame hatchFrame1 = new HatchFrame(GlobalData.selectedBerthId, hatchId, "L", allRuntimeData);
                    hatchFrame1.setVisible(true);
                }
            }
        });
        containerInfoFrame.setVisible(true);
        preStowPane.add(containerInfoFrame);
        BaseFrame lockLocationInfoFrame = new BaseFrame("锁定船箱位信息", SmartStowageLockLocationsInfo.class, smartStowImportData.getSmartStowageLockLocationsInfoList());
        lockLocationInfoFrame.setVisible(true);
        preStowPane.add(lockLocationInfoFrame);
//        BaseFrame areaCntInfoFrame = new BaseFrame("箱区统计信息", SmartAreaContainerInfo.class, smartStowImportData.getSmartAreaContainerInfoList());
//        areaCntInfoFrame.setVisible(true);
//        preStowPane.add(areaCntInfoFrame);
        BaseFrame groupInfoFrame = new BaseFrame("属性组信息", SmartContainerGroupInfo.class, smartStowImportData.getSmartContainerGroupInfoList());
        groupInfoFrame.setVisible(true);
        preStowPane.add(groupInfoFrame);

        try {
            lockLocationInfoFrame.setIcon(true);
            groupInfoFrame.setIcon(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getContentPane().add(mainPanel);
        this.getContentPane().add(btnPanel, BorderLayout.SOUTH);

    }
}
