package com.autostow3.test.autostowData.test18_11_13;

import com.autostow3.test.ViewFrame.ImportDataFrameAllShip;
import com.autostow3.test.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shbtos.biz.smart.cwp.service.SmartStowImportData;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by csw on 2018/5/30.
 * Description:
 */
public class Test11_13 {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws ParseException {

//        String filePath = "Test.AutoStow3/autoStowData/18.11.13/11.13槽重多次配载/STOW-WEIGHT贝塔出口航次：FJBCESHIE进口航次：FJBCESHII-20181113095704481/";
//        String stowInfo = FileUtil.readFileToString(new File(filePath + "smartStowWeightImportDataJson.txt")).toString();

        String filePath = "Test.AutoStow3/autoStowData/18.11.13/11.13槽重多次配载/STOW贝塔出口航次：FJBCESHIE进口航次：FJBCESHII-20181113095705528/";
        String stowInfo = FileUtil.readFileToString(new File(filePath + "smartStowImportDataJson.txt")).toString();

        Gson gson = new GsonBuilder().create();
        SmartStowImportData smartStowImportData = gson.fromJson(stowInfo, SmartStowImportData.class);

        ImportDataFrameAllShip importDataFrame = new ImportDataFrameAllShip(smartStowImportData);
        importDataFrame.setVisible(true);
    }
}
