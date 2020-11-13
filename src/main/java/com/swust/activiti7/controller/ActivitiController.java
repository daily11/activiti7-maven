package com.swust.activiti7.controller;

import com.alibaba.fastjson.JSONObject;
import com.swust.activiti7.model.DeploymentVO;
import com.swust.activiti7.service.IActivitiService;
import com.swust.activiti7.util.CODE;
import com.swust.activiti7.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Chen Yixing
 * @date 2020/11/2 15:11
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController {
    private IActivitiService activitiService;

    @Autowired
    public ActivitiController(IActivitiService activitiService) {
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
     * 新增 bpmn 文件
     *
     * stringFile   文件字符串
     * resourceName 资源名称
     */
    @RequestMapping("/insertBpmnByString")
    public Result insertBpmnByStringFile(
            @RequestBody JSONObject jsonObject
    ) {
        String stringFile = jsonObject.getString("stringFile");
        String resourceName = jsonObject.getString("resourceName");
        String resourceKey = jsonObject.getString("resourceKey");
        if (StringUtils.isEmpty(stringFile) || StringUtils.isEmpty(resourceKey)) {
            return new Result(CODE.ERROR, null, "参数不能为空！");
        }

        activitiService.insertBpmn(stringFile, resourceName, resourceKey);
        return new Result(CODE.SUCCESS, null, "success");
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

    /**
     * 查询流程部署信息
     *
     * @author Chen Yixing
     * @date 2020-11-04 16:45:47
     **/
    @RequestMapping("/selectBpmn")
    public Result selectBpmn(

    ) {
        List<DeploymentVO> list = activitiService.selectBpmn();
        return new Result(CODE.SUCCESS, list, "success");
    }

    /**
     * 流程部署资源删除，需要注意的是，会级联删除资源下面已经实例化的流程实例记录！
     *
     * @author Chen Yixing
     * @date 2020-11-04 17:38:58
     * @param jsonObject    流程部署ID
     **/
    @RequestMapping("/deleteBpmn")
    public Result deleteBpmn(
            @RequestBody JSONObject jsonObject
    ) {
        String deploymentId = jsonObject.getString("deploymentId");

        activitiService.deleteBpmn(deploymentId);
        return new Result(CODE.SUCCESS, null, "success");
    }

    /**
     * 查询部署资源流
     *
     * @author Chen Yixing
     * @date 2020-11-10 10:19:54
     * deploymentId 部署ID
     * resourceName 资源名称
     **/
    @RequestMapping("/getResource")
    public Result getResource(
            @RequestBody JSONObject jsonObject
    ) {
        String deploymentId = jsonObject.getString("deploymentId");
        String resourceName = jsonObject.getString("resourceName");

        Result result = null;
        InputStream fis = null;
        try {
            fis = activitiService.getResource(deploymentId, resourceName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = new Result(CODE.SUCCESS, stringBuilder.toString(), "success");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
