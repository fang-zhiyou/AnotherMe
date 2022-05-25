package com.example.contest.Utils.algorithm.preprocess;

import com.example.contest.Utils.algorithm.geography.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVReader {
    private static long getTimeStamp(String date, String time) { //将字符串转换为时间戳
        String dateString = date + ' ' + time;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = d.getTime() / 1000; //将毫秒转换为秒
        return ts;
    }

    public static ArrayList<Point> read(BufferedReader reader) throws IOException {
        String line = "";
        ArrayList<Point> points = new ArrayList<Point>(); //保存文件中所有坐标点
        for (int i = 1; (line = reader.readLine()) != null; i++) {
            if (i <= 6) //跳过前6行
                continue;
            String[] datas = line.split(",");

            long timeStamp = getTimeStamp(datas[5], datas[6]);
            double latitude = Double.parseDouble(datas[0]);
            double longitude = Double.parseDouble(datas[1]);

            points.add(new Point(timeStamp, longitude, latitude));
        }
        return points;
    }
}
