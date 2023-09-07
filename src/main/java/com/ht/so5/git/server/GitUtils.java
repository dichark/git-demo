package com.ht.so5.git.server;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.ht.so5.git.server.entitiy.GitConfiguration;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GitUtils {
    public static void main(String[] args) throws Exception {
        String repoName = "git-demo";
//        createRepo(repoName);

        String remoteUrl = "https://github.com/dichark/"+repoName+".git";

        String remoteName = "origin"; // 远程仓库的名称通常是 "origin"

        GitConfiguration gitConfiguration = new GitConfiguration("dichark", "Zy20140301");

        String token = "dXNlcm5hbWU6Z2l0aHViX3BhdF8xMUFFVk5QN0EwMnRmZzFPR0xtUm9hX1hweHhlQkI2RWtHTGFHeUh1Z09kMjRWN0d4V3lPeG9NaHFIbTFJUGhER3NJUzdaM1dPVXJjRUxHVEdD";

//        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("",token);
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitConfiguration.getName(), gitConfiguration.getPassword());

        File localPath = new File("D://Users//workspace//git-demo");



        // 本地Git仓库路径

        // 配置远程Git仓库的URL
        // 初始化一个新的Git仓库
        Git git = Git.init()
                .setDirectory(localPath)
                .call();
        git.remoteAdd()
                .setName(remoteName)
                .setUri(new URIish(remoteUrl))
                .call();
//        git.pull().setRemoteBranchName("master").call();
        // 添加文件到暂存区
        git.add()
                .addFilepattern(".")
                .call();

        // 提交文件到仓库
        git.commit()
                .setMessage("Initial commit")
                .call();


        System.out.println("11");

        Git git2 = Git.open(localPath);
        PushCommand pushCommand = git2.push();
        pushCommand.setForce(true);
        pushCommand.setRemote(remoteName); // 设置远程仓库的名称
        pushCommand.setRefSpecs(new RefSpec("refs/heads/master:refs/heads/master")); // 推送到远程的 master 分支

        pushCommand.setCredentialsProvider(credentialsProvider);

        pushCommand.call();
//        // 关闭Git
        git2.close();
    }

    private static boolean addRemote(Git git, String remoteUrl, File localPath) throws Exception {
//         初始化本地仓库
        InitCommand initCommand = Git.init();
        initCommand.setDirectory(localPath);
        initCommand.call();

        git.remoteAdd()
                .setName("origin") // 远程仓库的名称通常是 "origin"
                .setUri(new URIish(remoteUrl))
                .call();
        return true;
    }

    private static boolean createRepo(String repoNAme) {

        HttpRequest httpRequest = HttpUtil.createPost("https://api.github.com/user/repos");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic dXNlcm5hbWU6Z2l0aHViX3BhdF8xMUFFVk5QN0EwMnRmZzFPR0xtUm9hX1hweHhlQkI2RWtHTGFHeUh1Z09kMjRWN0d4V3lPeG9NaHFIbTFJUGhER3NJUzdaM1dPVXJjRUxHVEdD");

        httpRequest.addHeaders(headers);
        httpRequest.body("{\"name\":\"" + repoNAme + "\"}");
        HttpResponse httpResponse = httpRequest.execute();
        return httpResponse.getStatus() == 200;
    }

}
