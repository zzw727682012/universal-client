package com;

import com.code.Code20220715;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {


        int[] nums1 = {1,2,3,2,1};

        int[] nums2 = {3,2,1,4,7};
        Code20220715.findLength(nums1,nums2);
    }

    private static void codeGenerator() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            File templateFile = new File("C:\\Users\\kingdee\\IdeaProjects\\zzw\\src\\test\\java\\com\\code\\Code.java.vm");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String targetFileName = "Code" + dateFormat.format(date);
            File targetFile = new File(templateFile.getParent() + "\\" + targetFileName + ".java");
            while (targetFile.exists()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();

                targetFileName = "Code" + dateFormat.format(date);
                targetFile = new File(templateFile.getParent() + "\\" + targetFileName + ".java");
            }
            targetFile.createNewFile();

            reader = new BufferedReader(new FileReader(templateFile));
            writer = new BufferedWriter(new FileWriter(targetFile));

            List<String> readList= reader.lines().collect(Collectors.toList());
            for (String readLine : readList) {
                if (readLine.contains("${className}")) {
                    System.out.println(readLine);
                    System.out.println(targetFileName);
                    readLine = readLine.replace("${className}", targetFile.getName().split("\\.")[0]);
                }
                writer.write(readLine);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
