package jzeus.bpmn

import org.camunda.bpm.engine.delegate.DelegateTask
import org.camunda.bpm.engine.delegate.TaskListener
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS
import org.slf4j.LoggerFactory


import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl
import org.camunda.bpm.engine.impl.util.xml.Element

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener

class LoggingExecutionListener : ExecutionListener {
    private val logger = LoggerFactory.getLogger(LoggingExecutionListener::class.java)

    override fun notify(execution: DelegateExecution) {
        logger.info("Starting execution of service task: ${execution.currentActivityName} (ID: ${execution.currentActivityId})")
    }
}

/**
 * 这个监听器用于在解析`bpmn`文件时给所有`JavaDelegate`任务添加一个日志记录监听器
 *
 * 有关`AbstractBpmnParseListener`和`ExecutionListener`的区别参考[这里](https://poe.com/s/VAVzQL7iS4ir48ivs6Ea)
 */
class LoggingBpmnParseListener : AbstractBpmnParseListener() {
    override fun parseServiceTask(serviceTaskElement: Element, scope: ScopeImpl, activity: ActivityImpl) {
        // 检查这个服务任务是否使用了 JavaDelegate
        val delegateExpression = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "delegateExpression")
        val javaDelegate = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "class")

        if (delegateExpression != null || javaDelegate != null) {
            // 添加日志记录监听器
            activity.addListener("start", LoggingExecutionListener())
        }
    }
}
