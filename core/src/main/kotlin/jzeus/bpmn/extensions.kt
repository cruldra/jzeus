package jzeus.bpmn

import jzeus.json.toJavaObject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration
import org.camunda.bpm.engine.repository.Deployment
import org.camunda.bpm.engine.runtime.ProcessInstance

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
 * @param taskId 任务ID
 * @param variables 流程变量
 */
fun ProcessEngine.completeTask(taskId: String, variables: Map<String, Any> = emptyMap()) {
    taskService.complete(taskId, variables)
}
