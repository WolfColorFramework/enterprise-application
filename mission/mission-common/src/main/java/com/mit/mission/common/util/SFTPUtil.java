package com.mit.mission.common.util;

import com.jcraft.jsch.*;
import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.properties.FtpManager;
import com.mit.mission.common.properties.FtpProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SFTPUtil {

    /**
     * 先从主服务器下载文件，再从备份服务器下载文件
     *
     * @param path       目录
     * @param fileName   文件名
     * @param response
     * @param ftpManager FTP服务配置列表
     */
    public static Boolean downloadFile(String path, String fileName, HttpServletResponse response, FtpManager ftpManager) throws IOException, InterruptedException, SftpException, ExecutionException {

        // if (ftpManager.getFtpMain() == null || ftpManager.getFtpRedundancy() == null) {
        if (ftpManager.getFtpMain() == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "资源服务器未配置");
        }

        ChannelSftp sftp = efficientConnect(ftpManager, path + "/" + fileName);
        OutputStream os = response.getOutputStream();

        if (sftp != null) {
            CompletableFuture<Void> downloadFileTask = CompletableFuture.runAsync(() -> {
                try {
                    sftp.get(path + "/" + fileName, os, null, ChannelSftp.RESUME, 0);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            });

            Boolean value = downloadFileTask.thenApplyAsync(result -> {
                close(sftp);
                return true;
            }).exceptionally(e -> {
                log.error(e.getMessage());
                close(sftp);
                return false;
            }).get();
            return value;
//
//            Future<Boolean> submit = threadPool.submit(new DownloadMonitor(Paths.get(path, fileName).toString(), os, sftp));
//            return submit.get();
        } else {
            return false;
        }
    }

    /**
     * 下载多个文件到指定目录
     *
     * @param path       文件目录
     * @param fileNames  文件集合
     * @param destDir    目标目录
     * @param ftpManager ftp管理器
     * @return
     */
    public static Boolean downloadFiles(String path, List<Map<String, String>> fileNames, File destDir, FtpManager ftpManager) {
        try {
            ChannelSftp sftp = efficientConnect(ftpManager, path);
            List<CompletableFuture> listTasks = new ArrayList<>();
            if (sftp != null) {
                sftp.cd(path);
                Vector fs = sftp.ls("/" + path);
                for (Map<String, String> fileName : fileNames) {
                    String ftpFileName = fileName.get("fileName");
                    String fileRealName = fileName.get("realName");
                    if (!StringUtils.isEmpty(ftpFileName)) {
                        for (Object obj : fs) {
                            ChannelSftp.LsEntry f = (ChannelSftp.LsEntry) obj;
                            if (f.getFilename().equals(ftpFileName)) {
//                                threadPool.submit(() -> {
//                                    FileOutputStream os = null;
//                                    try {
//                                        os = new FileOutputStream(Paths.get(destDir.getPath(), fileRealName).toString());
//                                        new DownloadMonitor(ftpFileName, os, sftp);
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//
////                                    InputStream is = null;
////                                    try {
////                                        is = sftp.get(ftpFileName);
////                                        if (is != null) {
////                                            FileUtil.fileTransferTo(is, temDir.getPath() + File.separator + fileRealName);
////                                        }
////                                    } catch (SftpException e) {
////                                        e.printStackTrace();
////                                    }
//                                });
                                FileOutputStream os = new FileOutputStream(Paths.get(destDir.getPath(), fileRealName).toString());
                                CompletableFuture<Void> downloadTask = CompletableFuture.runAsync(() -> {
                                    try {
                                        sftp.get(ftpFileName, os, null, ChannelSftp.RESUME, 0);
                                    } catch (SftpException e) {
                                        e.printStackTrace();
                                    }
                                });
                                listTasks.add(downloadTask);
                            }
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(listTasks)) {
                    CompletableFuture[] tasks = listTasks.toArray(new CompletableFuture[0]);
                    Boolean result = CompletableFuture.allOf(tasks).thenApplyAsync(c -> {
                        close(sftp);
                        return true;
                    }).exceptionally(e -> {
                        log.error(e.getMessage());
                        close(sftp);
                        return false;
                    }).get();
                    return result;
                }
                return false;
            }
        } catch (SftpException | FileNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return false;
    }

    /**
     * 上传文件
     *
     * @param path           服务器路径
     * @param fileName       资源文件名称
     * @param sourceFile     资源文件
     * @param ftpManager     ftp管理器
     * @param randomFileName 是否随机起名
     * @return
     * @throws FileNotFoundException
     */
    public static UploadFileResult uploadFile(String path, String fileName, InputStream sourceFile, FtpManager ftpManager, boolean randomFileName) throws FileNotFoundException {
        if (ftpManager.getFtpMain() == null && ftpManager.getFtpRedundancy() == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "资源服务器未配置");
        }

        UploadFileResult uploadResult = new UploadFileResult();
        Boolean result = false;
        FtpProperties ftpMain = ftpManager.getFtpMain();
        CompletableFuture<Void> mainFileTask = null;
        if (ftpMain != null) {
            try {
                // 资源服务器 创建文件夹
                ChannelSftp sftp = connect(ftpMain);
                makeFolderIfNotExist(sftp, path);

                // 上传文件
                String newFileName = getFileName(fileName, randomFileName);
                String dest = path + "/" + newFileName;
                uploadResult.setFilePath(dest);
                mainFileTask = CompletableFuture.runAsync(() -> {
                    try {
                        sftp.put(sourceFile, dest, ChannelSftp.OVERWRITE);
                    } catch (SftpException e) {
                        e.printStackTrace();
                    }
                });
                Boolean value = mainFileTask.thenApplyAsync(c -> {
                    close(sftp);
                    return true;
                }).exceptionally(e -> {
                    log.error(e.getMessage());
                    close(sftp);
                    return false;
                }).get();

                result = value;
                uploadResult.setMainSuccess(value);
            } catch (SftpException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 主服务器上传成功，备份服务器上传即可
        if (result) {
            FtpProperties ftpRedundancy = ftpManager.getFtpRedundancy();
            if (ftpRedundancy != null) {
                try {
                    // 资源服务器 创建文件夹
                    ChannelSftp sftp = connect(ftpRedundancy);
                    makeFolderIfNotExist(sftp, path);

                    // 上传文件
                    String newFileName = getFileName(fileName, randomFileName);
                    String dest = path + "/" + newFileName;
                    CompletableFuture<Void> redundancyUploadFileTask = CompletableFuture.runAsync(() -> {
                        try {
                            sftp.put(sourceFile, dest, ChannelSftp.OVERWRITE);
                        } catch (SftpException e) {
                            e.printStackTrace();
                        }
                    });

                    redundancyUploadFileTask
                            .thenAcceptAsync(c -> close(sftp))
                            .exceptionally(e -> {
                                log.error(e.getMessage());
                                close(sftp);
                                return null;
                            });
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FtpProperties ftpRedundancy = ftpManager.getFtpRedundancy();
            CompletableFuture<Void> redundancyUploadFileTask = null;
            if (ftpRedundancy != null) {
                try {
                    // 资源服务器 创建文件夹
                    ChannelSftp sftp = connect(ftpRedundancy);
                    makeFolderIfNotExist(sftp, path);

                    // 上传文件
                    String newFileName = getFileName(fileName, randomFileName);
                    String dest = path + "/" + newFileName;
                    redundancyUploadFileTask = CompletableFuture.runAsync(() -> {
                        try {
                            sftp.put(sourceFile, dest, ChannelSftp.OVERWRITE);
                        } catch (SftpException e) {
                            e.printStackTrace();
                        }
                    });

                    Boolean value = redundancyUploadFileTask.thenApplyAsync(c -> {
                        close(sftp);
                        return true;
                    }).exceptionally(e -> {
                        log.error(e.getMessage());
                        close(sftp);
                        return false;
                    }).get();
                    result = value;
                    uploadResult.setRedundancySuccess(value);
                } catch (SftpException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        return uploadResult;
    }

    /**
     * 删除文件
     *
     * @param ftpManager ftp管理器
     * @param filePath   文件路径（服务器上）
     * @return
     */
    public static boolean deleteFile(FtpManager ftpManager, String filePath) {
        // 主服务器删除
        FtpProperties ftpMain = ftpManager.getFtpMain();
        if (ftpMain != null) {
            ChannelSftp mainFtp = connect(ftpMain);
            if (mainFtp.isConnected()) {
                try {
                    mainFtp.rm(filePath);
                } catch (SftpException e) {
                    log.error("删除文件失败，filePath：" + filePath);
                    return false;
                }
            }
        }

        // 从服务器删除
        FtpProperties ftpRedundancy = ftpManager.getFtpRedundancy();
        if (ftpRedundancy != null) {
            ChannelSftp redundancyFtp = connect(ftpRedundancy);
            if (redundancyFtp.isConnected()) {
                try {
                    redundancyFtp.rm(filePath);
                } catch (SftpException e) {
                    log.error("删除文件失败，filePath：" + filePath);
                    return false;
                }
            }
        }

        return true;
    }


    // 如果文件夹不存在，则创建文件夹
    private static void makeFolderIfNotExist(ChannelSftp sftp, String folderName) throws SftpException {
        String root = sftp.pwd();   // 远程目录初始路径
        String[] dirs = folderName.split("/");

        // 创建文件夹
        for (String dir : dirs) {
            SftpATTRS attrs = null;
            try {
                attrs = sftp.stat(dir);
                if (attrs == null) {
                    sftp.mkdir(dir);
                }
                sftp.cd(dir);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        // 回退到root
        sftp.cd(root);
    }

    /**
     * 连接sftp服务器
     *
     * @param ftpManager 资源服务器管理器
     * @param path       文件路径
     * @return
     */
    private static ChannelSftp efficientConnect(FtpManager ftpManager, String path) throws SftpException {

        ChannelSftp sftp = null;

        // 主服务器资源
        FtpProperties ftpMain = ftpManager.getFtpMain();
        if (ftpMain != null) {
            sftp = connect(ftpMain);
            if (existFile(sftp, path)) {
                return sftp;
            } else {
                close(sftp);
            }
        }

        // 从服务器资源
        FtpProperties ftpRedundancy = ftpManager.getFtpRedundancy();
        if (ftpRedundancy != null) {
            sftp = connect(ftpMain);
            if (existFile(sftp, path)) {
                return sftp;
            } else {
                close(sftp);
            }
        }

        return null;
    }

    /**
     * 关闭sftp
     *
     * @param sftp
     */
    public static void close(ChannelSftp sftp) {
        if (sftp == null || !sftp.isConnected())
            return;
        try {
            sftp.getSession().disconnect();
            sftp.quit();
            sftp.disconnect();
        } catch (JSchException e) {
            log.error("close sftp error!  " + e.getMessage());
        }
    }

    /**
     * 创建连接
     *
     * @param ftp 资源服务器信息
     * @return
     */
    private static ChannelSftp connect(FtpProperties ftp) {
        if (ftp == null) return null;

        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(ftp.getLoginName(), ftp.getIp(), Integer.valueOf(ftp.getPort()));
            sshSession.setPassword(ftp.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("Connected to sftp ::" + ftp.getIp());
        } catch (Exception e) {
            log.error("Connected to sftp ::" + ftp.getIp() + " failed!  " + e.getMessage());
        }
        return sftp;
    }

    /**
     * 判断文件是否存在
     *
     * @param sftp
     * @param path
     * @return
     * @throws SftpException
     */
    private static Boolean existFile(ChannelSftp sftp, String path) throws SftpException {
        SftpATTRS attrs = sftp.lstat(path);
        return attrs != null;
    }

    private static String getFileName(String name, Boolean isRand) {
        if (isRand) {
            if (name.lastIndexOf(".") != -1) {
                return UUID.randomUUID().toString() + name.substring(name.lastIndexOf("."));
            } else {
                return UUID.randomUUID().toString();
            }
        }
        return name;
    }

    @Data
    public static class UploadFileResult {
        private String filePath = null;
        private Boolean mainSuccess = false;
        private Boolean redundancySuccess = false;
    }

}
