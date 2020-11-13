package com.swust.activiti7.service;

import com.swust.activiti7.model.DeploymentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Chen Yixing
 * @date 2020/11/2 16:00
 */
public interface IActivitiService {
    /**
     * 部署流程图
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:31:29
     * @param bpmnXMLFile   流程图文件
     **/
    void insertBpmnByXMLFile(MultipartFile bpmnXMLFile);

    /**
     * 查看流程定义
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:31:41
     * @param username 用户名
     **/
    String listAvailableProcesses(String username);

    /**
     * 开始流程
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:08
     * @param username              用户名称
     * @param decision              网关值
     * @param processDefinitionKey  流程定义key
     **/
    void startProcess(String username, Integer decision, String processDefinitionKey);

    /**
     * 查询用户当前任务
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:33
     * @param username  用户名称
     **/
    String listReportTask(String username);

    /**
     * 处理任务
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:52
     * @param username  用户信息
     * @param taskId    任务ID
     **/
    void handleReport(String username, String taskId);

    /**
     * 查询流程部署信息
     *
     * @author Chen Yixing
     * @date 2020-11-04 16:36:49
     *
     * */
    List<DeploymentVO> selectBpmn();

    /**
     *流程部署资源删除，需要注意的是，会级联删除资源下面已经实例化的流程实例记录！
     *
     * @author Chen Yixing
     * @date 2020-11-04 17:39:31
     * @param deploymentId 流程部署ID
     **/
    void deleteBpmn(String deploymentId);

    /**
     * 查询部署资源流
     *
     * @author Chen Yixing
     * @date 2020-11-10 10:19:54
     * deploymentId 部署ID
     * resourceName 资源名称
     **/
    InputStream getResource(String deploymentId, String resourceName);

    /**
     * 新增 bpmn 文件
     *
     * @author Chen Yixing
     * @date 2020-11-10 15:08:09
     * @param stringFile
     * @param resourceName
     * @param resourceKey
     **/
    void insertBpmn(String stringFile, String resourceName, String resourceKey);
}
