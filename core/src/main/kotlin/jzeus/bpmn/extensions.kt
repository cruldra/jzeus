package jzeus.bpmn

import cn.hutool.core.util.ReflectUtil
import jzeus.json.toJavaObject
import org.camunda.bpm.engine.ManagementService
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.history.HistoricActivityInstance
import org.camunda.bpm.engine.impl.bpmn.delegate.JavaDelegateInvocation
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration
import org.camunda.bpm.engine.impl.delegate.DelegateInvocation
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity
import org.camunda.bpm.engine.repository.Deployment
import org.camunda.bpm.engine.runtime.Incident
import org.camunda.bpm.engine.runtime.Job
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder
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

fun DelegateInvocation.getVariable(name: String): Any? {
    if (this is JavaDelegateInvocation) {
        val execution = ReflectUtil.getFieldValue(this, "execution") as ExecutionEntity
        return execution.getVariable(name)
    }

    return null
}

val DelegateInvocation.activityId: String?
    get() {
        if (this is JavaDelegateInvocation) {
            val execution = ReflectUtil.getFieldValue(this, "execution") as ExecutionEntity
            return execution.activityId
        }

        return null
    }

fun <R> Incident.use(block: Incident.() -> R): R {
    return block()
}

/**
 * 统计[流程实例][instanceId]中已经成功执行过的[指定任务][taskId]的数量
 * @param instanceId 流程实例ID
 * @param taskId 任务ID
 * @return 成功执行过的任务数量
 */
fun ProcessEngine.countFinishedTask(instanceId: String?, taskId: String): Long {
    return historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(instanceId)
        .activityId(taskId)
        .finished()
        .count()
}

fun <R> ProcessEngine.use(block: ProcessEngine.() -> R): R {
    return block()
}
//        val query = camundaEngine.historyService.createHistoricTaskInstanceQuery()
//            .processInstanceId(this.camundaInstanceId)
//.taskDefinitionKey("ScriptRewriteTask")
//.finished()

/**
 * 查询[指定任务][taskId]当前活动中的作业
 */
fun ManagementService.activateJobs(taskId: String): List<Job> =
    createJobQuery()
        .activityId(taskId)
        .active()
        .list()


/**
 * 获取[流程][instanceId]最近一次成功执行过的任意任务
 * @param instanceId 流程实例ID
 * @return 最近一次成功执行过的任务
 */
fun ProcessEngine.recentlyRecentFinishedActivityInstances(instanceId: String): HistoricActivityInstance? {
    return historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(instanceId)
        .finished()
        .orderByHistoricActivityInstanceEndTime().desc()
        .list().firstOrNull()
}

/**
 * 获取[流程][instanceId]最近一次出现的错误
 * @param instanceId 流程实例ID
 * @return 最近一次出现的错误
 */
fun ProcessEngine.recentlyErrorIncident(instanceId: String): Incident? {
    return runtimeService.createIncidentQuery()
        .processInstanceId(instanceId)
        .orderByIncidentTimestamp().desc()
        .incidentType("failedJob")
        .list().firstOrNull()
}


/**
 * 获取[流程实例][instanceId]当前正在等待执行的活动
 * @param instanceId 流程实例ID
 * @return 正在等待执行的活动
 */
fun ProcessEngine.getWaitingActivities(instanceId: String): List<WaitingActivity> {

    return runtimeService.getActiveActivityIds(instanceId)
        .groupingBy { it }
        .eachCount()
        .map { (activityId, count) ->
            WaitingActivity(activityId, count)
        }
}

fun ProcessEngine.getInstances(processDefinitionKey: String): List<ProcessInstance> {
    return runtimeService.createProcessInstanceQuery()
        .processDefinitionKey(processDefinitionKey)
        .list()
}

/**
 * 创建流程实例
 *
 * ## 示例: 创建流程并从指定的步骤开始执行
 * ```kotlin
 * camundaEngine.use{
 *
 *  createProcessInstance("sample-process"){
 *    .startBeforeActivity("activityId")
 *  }
 * }
 * ```
 *
 * @param processDefinitionKey 流程定义的key
 * @param block 流程实例构建器
 */
fun ProcessEngine.createProcessInstance(
    processDefinitionKey: String,
    block: ProcessInstantiationBuilder.() -> Unit
): ProcessInstance {
    return runtimeService.createProcessInstanceByKey(processDefinitionKey).apply(block).execute()
}
