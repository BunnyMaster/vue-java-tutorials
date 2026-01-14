package cn.bunny.mongodb.utils;

import cn.bunny.mongodb.download.VideoDownload;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SystemControlUtils {

    /**
     * 播放音乐
     */
    public static void playMusic() {
        ClassLoader classLoader = VideoDownload.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("static/download-complete/download-complete.wav");
        if (inputStream == null) return;

        try (Clip clip = AudioSystem.getClip()) {
            clip.open(AudioSystem.getAudioInputStream(inputStream));
            clip.start();

            // 等待音乐播放完成
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    
    /**
     * 执行命令行并输出
     *
     * @param command      命令内容
     * @param currentIndex 当前下载的第几个
     */
    public static void startProcess(List<String> command, int currentIndex) {
        try {
            // 创建进程构建器
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // 设置工作目录（可选）
            processBuilder.directory(new File("D:\\software\\Plugins\\Mu3U8下载\\"));

            // 启动进程
            Process process = processBuilder.start();

            // 读取命令输出，指定使用 UTF-8 编码
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.print("\r" + line + "第" + currentIndex + "个");
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("第" + currentIndex + "个 Exited with error code " + exitCode);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
