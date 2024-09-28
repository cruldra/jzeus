package jzeus.bpmn

import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration
import org.camunda.bpm.engine.repository.Deployment

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
