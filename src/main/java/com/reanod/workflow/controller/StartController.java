package com.reanod.workflow.controller;

import com.reanod.workflow.utils.RestMessgae;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xugj<br>
 * @version 1.0<br>
 * @createDate 2019/05/30 10:31 <br>
 * @Description <p> 启动流程实例 </p>
 */

@RestController
@Api(tags="启动流程实例")
public class StartController {

    private final RuntimeService runtimeService;

    public StartController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping(path = "start")
    @ApiOperation(value = "根据流程key启动流程",notes = "每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey",value = "流程key",dataType = "String",paramType = "query",example = ""),
            @ApiImplicitParam(name = "user",value = "启动流程的用户",dataType = "String",paramType = "query",example = "")
    })
    public RestMessgae start(@RequestParam("user") String userKey,
                             @RequestParam("processKey") String processKey) {
        HashMap<String, Object> variables=new HashMap<>(1);
        variables.put("userKey", userKey);

        RestMessgae restMessgae = new RestMessgae();
        ProcessInstance instance = null;
        try {
            instance = runtimeService
                    .startProcessInstanceByKey(processKey, variables);
        } catch (Exception e) {
            restMessgae = RestMessgae.fail("启动失败", e.getMessage());
            e.printStackTrace();
        }

        if (instance != null) {
            Map<String, String> result = new HashMap<>(2);
            // 流程实例ID
            result.put("processID", instance.getId());

            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessgae = RestMessgae.success("启动成功", result);
        }
        return restMessgae;
    }


    @PostMapping(path = "searchByKey")
    @ApiOperation(value = "根据流程key查询流程实例",notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey",value = "流程key",dataType = "String",paramType = "query",example = ""),
    })
    public RestMessgae searchProcessInstance(@RequestParam("processKey") String processDefinitionKey){
        RestMessgae restMessgae = new RestMessgae();
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessgae = RestMessgae.fail("查询失败", e.getMessage());
            e.printStackTrace();
        }

        int size = runningList.size();
        if (size > 0) {
            List<Map<String, String>> resultList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ProcessInstance pi = runningList.get(i);
                Map<String, String> resultMap = new HashMap<>(2);
                // 流程实例ID
                resultMap.put("processID", pi.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
                resultList.add(resultMap);
            }
            restMessgae = RestMessgae.success("查询成功", resultList);
        }
        return restMessgae;
    }


    @PostMapping(path = "searchByID")
    @ApiOperation(value = "根据流程key查询流程实例",notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processID",value = "流程实例ID",dataType = "String",paramType = "query",example = ""),
    })
    public RestMessgae searchByID(@RequestParam("processID") String processDefinitionID){
        RestMessgae restMessgae  = new RestMessgae();
        ProcessInstance pi = null;
        try {
            pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processDefinitionID)
                    .singleResult();
        } catch (Exception e) {
            restMessgae = RestMessgae.fail("查询失败", e.getMessage());
            e.printStackTrace();
        }

        if (pi != null) {
            Map<String, String> resultMap = new HashMap<>(2);
            // 流程实例ID
            resultMap.put("processID", pi.getId());
            // 流程定义ID
            resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
            restMessgae = RestMessgae.success("查询成功", resultMap);
        }
        return restMessgae;
    }

    @PostMapping(path = "deleteProcessInstanceByKey")
    @ApiOperation(value = "根据流程实例key删除流程实例",notes = "根据流程实例key删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey",value = "流程实例Key",dataType = "String",paramType = "query",example = ""),
    })
    public RestMessgae deleteProcessInstanceByKey(@RequestParam("processKey") String processDefinitionKey){
        RestMessgae restMessgae = new RestMessgae();
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessgae = RestMessgae.fail("删除失败", e.getMessage());
            e.printStackTrace();
        }

        int size = runningList.size();
        if (size > 0) {
            List<Map<String, String>> resultList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ProcessInstance pi = runningList.get(i);
                runtimeService.deleteProcessInstance(pi.getId(),"删除");
            }
            restMessgae = RestMessgae.success("删除成功", resultList);
        }
        return  restMessgae;
    }

    @PostMapping(path = "deleteProcessInstanceByID")
    @ApiOperation(value = "根据流程实例ID删除流程实例",notes = "根据流程实例ID删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processID",value = "流程实例ID",dataType = "String",paramType = "query",example = ""),
    })
    public RestMessgae deleteProcessInstanceByID(@RequestParam("processID") String processDefinitionID){
        RestMessgae restMessgae = new RestMessgae();
        try {
            runtimeService.deleteProcessInstance(processDefinitionID,"删除" + processDefinitionID);
        } catch (Exception e) {
            restMessgae = RestMessgae.fail("删除失败", e.getMessage());
            return  restMessgae;
        }
        restMessgae = RestMessgae.success("删除成功", "");
        return  restMessgae;
    }
}
