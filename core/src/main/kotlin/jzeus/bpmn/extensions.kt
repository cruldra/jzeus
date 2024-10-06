package jzeus.bpmn

import jzeus.json.toJavaObject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration
import org.camunda.bpm.engine.repository.Deployment
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.task.Task

fun camunda(block: StandaloneProcessEngineConfiguration.() -> Unit): ProcessEngine =
    StandaloneProcessEngineConfiguration()
        .apply(block).buildProcessEngine()

/**
 * 在`camunda`引擎中部署一个位于`classpath`中的流程定义
 *
 * ```kotlin
 *
 * val engine = camunda {
 *     jdbcUrl = "jdbc:h2:file:./szr-api;TRACE_LEVEL_FILE=0"
 *     jdbcDriver = "org.h2.Driver"
 *     databaseSchemaUpdate = "true"
 * }
 * engine.deployClassPathFlow("flows/sample-process.bpmn")
 * ```
 *
 * @param flow 流程定义文件名
 * @return 部署结果
 */
fun ProcessEngine.deployClassPathFlow(flow: String): Deployment = repositoryService.createDeployment()
    .addClasspathResource(flow)
    .deploy()

/**
 * 在`camunda`引擎中启动一个流程实例
 *
 *
 * ```kotlin
 * val engine = camunda {
 *     jdbcUrl = "jdbc:h2:file:./szr-api;TRACE_LEVEL_FILE=0"
 *     jdbcDriver = "org.h2.Driver"
 *     databaseSchemaUpdate = "true"
 * }
 * engine.deployClassPathFlow("flows/sample-process.bpmn")
 * val processInstance = engine.runProcess("sample-process")
 * println("Process instance started: " + processInstance.id)
 * ```
 *
 * @param processKey 流程定义的`key`
 * @param variables 流程变量
 */
fun ProcessEngine.runProcess(processKey: String, variables: Map<String, Any> = emptyMap()): ProcessInstance =
    runtimeService.startProcessInstanceByKey(processKey, variables)

fun <T> DelegateExecution.getJsonVariable(name: String, clazz: Class<T>): T {
    return (getVariable(name) as String).toJavaObject(clazz)
}

/**
 * 在`camunda`引擎中完成一个任务,通常用于用户任务
 * @param_task_id_任务id
 * @param_variables_流程变量
 */
fun ProcessEngine.completeTask(taskId: String, variables: Map<String, Any> = emptyMap()) {
    taskService.complete(taskId, variables)
}

/**
 * 根据任务ID获取任务
 * @author dongjak
 * @created 2024/10/06
 * @version 1.0
 * @since 1.0
 */
fun ProcessEngine.getTaskById(taskId: String): Task {
    return taskService.createTaskQuery().taskId(taskId).singleResult()
}

/**
 * 获取任务变量
 * @author dongjak
 * @created 2024/10/06
 * @version 1.0
 * @since 1.0
 */
fun ProcessEngine.getTaskVariable(taskId: String, name: String): Any? {
    val task = getTaskById(taskId)
    val processInstanceId = task.processInstanceId
    return runtimeService.getVariable(processInstanceId, name)
}
