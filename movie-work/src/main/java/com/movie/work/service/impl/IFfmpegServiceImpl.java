package com.movie.work.service.impl;

import com.movie.common.config.ProjectConfig;
import com.movie.common.constant.MovieConstant;
import com.movie.common.utils.file.FileUploadUtils;
import com.movie.common.utils.movie.CmdUtil;
import com.movie.work.service.IFfmpegService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * 视频处理类
 */
@Service
public class IFfmpegServiceImpl implements IFfmpegService {

    /**
     * 将视频截取成三段
     *
     * @param absPath  输入文件路径
     * @param fileName 文件名
     */
    @Override
    public String dealThreePrint(String absPath, String fileName) throws IOException, InterruptedException {
        // 黑底文件的绝对路径
        String absBlackPath = FileUploadUtils.getAbsoluteFile(ProjectConfig.getUploadPath(), "source/black.jpg").getAbsolutePath();
        // 输出文件的绝对路径
        String absOutPath = FileUploadUtils.getAbsoluteFile(ProjectConfig.getUploadPath() + "/threePrint", fileName).getAbsolutePath();
        // 组装命令
        String command = String.format(MovieConstant.FILM_IMAGE_BLACK, absBlackPath, absPath, absBlackPath, absOutPath);
        System.out.println(command);
        String[] commands = {"sh", "-c", command.toString() + "&"};
        // 执行文件处理
        Process process = Runtime.getRuntime().exec(commands);
        process.waitFor();
        return absOutPath;
    }

    /**
     * 上下添加文字
     *
     * @param absPath  输入文件路径
     * @param fileName 文件名
     */
    @Override
    public String addDub(String absPath, String fileName) throws IOException, InterruptedException {
        // 输出文件的绝对路径
        String withWordOut = FileUploadUtils.getAbsoluteFile(ProjectConfig.getUploadPath() + "/withWordOut", fileName).getAbsolutePath();
        StringBuffer command = new StringBuffer();
        command.append("ffmpeg -i " + absPath + " -vf ");
        command.append(" \" drawtext=fontfile=/data/web/qipa250/font/big.ttf:text='因为一段片段 看完整部电影':y=(h-line_h)/6:x=(w-text_w)/2::fontsize=60:fontcolor=white:shadowy=2,");
        command.append(" drawtext=fontfile=/data/web/qipa250/font/big.ttf:text='盘点那些让人百看不厌的经典电影片段':y=(h-line_h)/6+150:x=(w-text_w)/2::fontsize=40:fontcolor=white:shadowy=2,");
        command.append(" drawtext=fontfile=/data/web/qipa250/font/big.ttf:text='电影名':y=3*(h-line_h)/4-20:x=(w-text_w)/2::fontsize=60:fontcolor=yellow:shadowy=2,");
        command.append(" drawtext=fontfile=/data/web/qipa250/font/big.ttf:text='点击右侧头像看全集':y=3*(h-line_h)/4+50:x=(w-text_w)/2::fontsize=40:fontcolor=red:shadowy=2 \" ");
        command.append(withWordOut);
        System.out.println(command.toString());
        String[] commands = {"sh", "-c", command.toString() + "&"};
        // 执行文件处理

        process(commands, i -> {
            System.out.println(i);

        });

        return absPath;
    }

    public void process(String[] commands, Consumer<Integer> consumer) throws IOException, InterruptedException {
        Process process = null;

        process = Runtime.getRuntime().exec(commands);
       // int i = process.waitFor();
        dealStream( process);

        consumer.accept(10);
    }


    /**
     * 处理process输出流和错误流，防止进程阻塞
     * 在process.waitFor();前调用
     * @param process
     */
    private static void dealStream(Process process) {
        if (process == null) {
            return;
        }
        // 处理InputStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    /**
     * 添加西瓜视频
     *
     * @param absPath  输入文件路径
     * @param fileName 文件名
     */
    @Override
    public String addWaterMelon(String absPath, String fileName) throws IOException, InterruptedException {
        // 输出文件的绝对路径
        String waterMelonPng = "source/watermelon.png";
        // 黑底文件的绝对路径
        String waterMelonPngPath = FileUploadUtils.getAbsoluteFile(ProjectConfig.getUploadPath(), waterMelonPng).getAbsolutePath();
        String withWordOut = FileUploadUtils.getAbsoluteFile(ProjectConfig.getUploadPath() + "/WaterMelon", fileName).getAbsolutePath();
        StringBuffer command = new StringBuffer();
        command.append("ffmpeg -i ");
        command.append(absPath);
        command.append(" -i ");
        command.append(waterMelonPngPath);
        command.append(" -filter_complex 'overlay=x=main_w/2-overlay_w/2:y=main_h/3+main_h/3+overlay_h -200' ");
        command.append(withWordOut);
        System.out.println(command.toString());
        String[] commands = {"sh", "-c", command.toString() + "&"};
        // 执行文件处理
        Process process = Runtime.getRuntime().exec(commands);
        process.waitFor();
        return absPath;
    }
}
