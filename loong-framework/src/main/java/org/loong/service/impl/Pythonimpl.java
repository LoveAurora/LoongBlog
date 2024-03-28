package org.loong.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.loong.domain.ResponseResult;
import org.loong.domain.entity.IOC;
import org.springframework.stereotype.Service;

@Service("pythonimpl")
public class Pythonimpl {
//    public static void main(String[] args) {
//        try {
//            // 你的Python脚本路径
//            String pythonScriptPath = "C:\\Users\\tyl\\Desktop\\iocsearcher\\iocextract.py";
//
//            // 执行Python脚本的命令
//            String[] cmd = new String[2];
//            cmd[0] = "C:\\Users\\tyl\\.conda\\envs\\ioc\\python.exe";
//            cmd[1] = pythonScriptPath;
//
//            // 创建进程建造者
//            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
//            processBuilder.directory(new File("C:\\Users\\tyl\\Desktop\\iocsearcher"));
//            // 启动进程
//            Process process = processBuilder.start();
//            List<IOC> list = new ArrayList<>();
//            // 读取进程的标准输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                list.addAll(parseLineToIOCEntry(line));
//                System.out.println(line);
//            }
//            System.out.println(list);
//
//            // 获取错误流
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//            // 读取错误输出
//            String errorLine;
//            while ((errorLine = errorReader.readLine()) != null) {
//                System.out.println("Error: " + errorLine);
//            }
//
//
//            // 等待进程完成
//            int exitCode = process.waitFor();
//
//            if (exitCode != 0) {
//                // 根据需要处理非零退出代码
//                System.out.println("Python脚本执行出错！");
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public ResponseResult IOC() {
        List<IOC> list = new ArrayList<>();
        try {
            // 你的Python脚本路径
            String pythonScriptPath = "C:\\Users\\tyl\\Desktop\\iocsearcher\\iocextract.py";

            // 执行Python脚本的命令
            String[] cmd = new String[2];
            cmd[0] = "C:\\Users\\tyl\\.conda\\envs\\ioc\\python.exe";
            cmd[1] = pythonScriptPath;

            // 创建进程建造者
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.directory(new File("C:\\Users\\tyl\\Desktop\\iocsearcher"));
            // 启动进程
            Process process = processBuilder.start();
            // 读取进程的标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                list.addAll(parseLineToIOCEntry(line));
                System.out.println(line);
            }
            System.out.println(list);

            // 获取错误流
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 读取错误输出
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("Error: " + errorLine);
            }

            // 等待进程完成
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                // 根据需要处理非零退出代码
                System.out.println("Python脚本执行出错！");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseResult.successResult(list);
    }

    private static List<IOC> parseLineToIOCEntry(String line) {
        List<IOC> dataList = new ArrayList<>();

        // 清洗并切分每行数据
        line = line.trim();
        line = line.substring(1, line.length() - 1);  // 移除大括号{}
        String[] keyValuePairs = line.split(", ");

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split("\\s+", 2);
            if (parts.length == 2) {
                dataList.add(new IOC(parts[0], parts[1]));
            }
        }
        return dataList;
    }
}

