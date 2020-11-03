package com.swust.activiti7.service;

import com.swust.activiti7.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Chen Yixing
 * @date 2020/11/2 16:00
 */
@Service
public class ActivitiService {
    private Logger logger = LoggerFactory.getLogger(ActivitiService.class);

    private ProcessRuntime processRuntime;
    private TaskRuntime taskRuntime;
    private SecurityUtil securityUtil;
    private RuntimeService runtimeService;
    private RepositoryService repositoryService;
    private TaskService taskService;

    @Autowired
    public ActivitiService(
            ProcessRuntime processRuntime,
            TaskRuntime taskRuntime,
            SecurityUtil securityUtil,
            RuntimeService runtimeService,
            RepositoryService repositoryService,
            TaskService taskService
    ) {
        this.processRuntime = processRuntime;
        this.taskRuntime = taskRuntime;
        this.securityUtil = securityUtil;
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
    }

    public String listAvailableProcesses(String username) {
        securityUtil.logInAs(username);

        StringBuilder sb = new StringBuilder();
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0,10));
        logger.info("> Available Process definitions: " + processDefinitionPage.getTotalItems());
        sb.append("> Available Process definitions: " + processDefinitionPage.getTotalItems());
        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            logger.info("\t > Process definition: " + pd);
            sb.append("\t > Process definition: " + pd);
        }

        return sb.toString();
    }

    public void startProcess(String username, Integer decision, String processDefinitionKey) {
//        List<org.activiti.engine.repository.ProcessDefinition> pds = repositoryService
//                .createProcessDefinitionQuery()
//                .processDefinitionKey("daily_report")
//                .list();
//        org.activiti.engine.repository.ProcessDefinition pd = pds.get(pds.size() - 1);
//        runtimeService.startProcessInstanceByKey(pd.getKey());

        securityUtil.logInAs(username);
        processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
//                .withName("daily_report")
                .withVariable("decision",decision)
                .build());
    }

    public String listReportTask(String username) {
        StringBuilder sb = new StringBuilder();
//        List<org.activiti.engine.task.Task> tasks = taskService
//                .createTaskQuery()
//                .taskAssignee("jingli")
//                .list();
//        tasks.forEach(arg -> System.out.println(arg));

        securityUtil.logInAs(username);//认证
        Page<Task> taskPage=taskRuntime.tasks(Pageable.of(0,100));
        if (taskPage.getTotalItems()>0){
            for (Task task:taskPage.getContent()){
                System.out.println("任务："+task);
                sb.append("任务："+task);
            }
        }

        return StringUtils.isEmpty(sb.toString()) ? "当前用户查无任务" : sb.toString();
    }

    public void handleReport(String username, String taskId) {
        securityUtil.logInAs(username);//认证
        // 领取任务
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).build());
        // 执行任务
        taskRuntime
                .complete(TaskPayloadBuilder
                .complete()
                .withTaskId(taskId)
//                .withVariable("rating", 5)
                .build());
    }

    public void insertBpmnByXMLFile(MultipartFile bpmnXMLFile) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 创建 DeploymentBuilder 实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // 创建 BpmnModel 实例
        BpmnModel bpmnModel = createProcessModel(bpmnXMLFile);
        String id = bpmnModel.getProcesses().get(0).getId();
        String name = bpmnModel.getProcesses().get(0).getName();
        // 流程部署
        deploymentBuilder
                .addBpmnModel(id + ".bpmn20.xml", bpmnModel)
                .name(name)
                .enableDuplicateFiltering()
                .deploy();
    }

    private BpmnModel createProcessModel(MultipartFile bpmnXMLFile) {
        InputStream inputStream = null;
        BpmnModel bpmnModel = null;
        try {
            //获取部署资源输入流
            inputStream = bpmnXMLFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            //创建转换器
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            InputStreamProvider inputStreamProvider = new InputStreamSource(inputStream);
            bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, true, false, "UTF-8");
        }
        return bpmnModel;
    }
}
