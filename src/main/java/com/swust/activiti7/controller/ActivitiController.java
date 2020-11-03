package com.swust.activiti7.controller;

import com.swust.activiti7.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Chen Yixing
 * @date 2020/11/2 15:11
 */
@RestController
public class ActivitiController {
    private ActivitiService activitiService;

    @Autowired
    public ActivitiController(ActivitiService activitiService) {
        this.activitiService = activitiService;
    }

    @RequestMapping("/test")
    public String test() {
        return "success";
    }

    /**
     * 部署流程图
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:31:29
     * @param bpmnXMLFile   流程图文件
     **/
    @RequestMapping("/insertBpmnByXMLFile")
    public String insertBpmnByXMLFile(
            @RequestParam("bpmnXMLFile") MultipartFile bpmnXMLFile
    ) {
        activitiService.insertBpmnByXMLFile(bpmnXMLFile);
        return "success";
    }

    /**
     * 查看流程定义
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:31:41
     * @param username 用户名
     **/
    @RequestMapping("/listAvailableProcesses")
    public String listAvailableProcesses(String username) {
        return activitiService.listAvailableProcesses(username);
    }

    /**
     * 开始流程
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:08
     * @param username              用户名称
     * @param decision              网关值
     * @param processDefinitionKey  流程定义key
     **/
    @RequestMapping("/startProcess")
    public String startProcess(String username, Integer decision, String processDefinitionKey) {
        activitiService.startProcess(username, decision, processDefinitionKey);
        return "success";
    }

    /**
     * 查询用户当前任务
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:33
     * @param username  用户名称
     **/
    @RequestMapping("/listReportTask")
    public String listReportTask(String username) {
        return activitiService.listReportTask(username);
    }

    /**
     * 处理任务
     *
     * @author Chen Yixing
     * @date 2020-11-03 16:33:52
     * @param username  用户信息
     * @param taskId    任务ID
     **/
    @RequestMapping(value = "/handleReport", method = RequestMethod.POST)
    public String handleReport(
            String username, String taskId
    ) {
        activitiService.handleReport(username, taskId);
        return "success";
    }
}
