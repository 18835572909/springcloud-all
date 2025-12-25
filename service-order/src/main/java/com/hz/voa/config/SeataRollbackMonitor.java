package com.hz.voa.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.nio.reactor.ExceptionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author rhb
 * @date 2025/12/25 16:33
 **/
@Component
@Slf4j
public class SeataRollbackMonitor {

    private final Map<String, List<RollbackEvent>> events = new ConcurrentHashMap<>();

    @EventListener
    public void onGlobalBegin(GlobalBeginEvent event) {
        String xid = event.getXid();
        events.putIfAbsent(xid, new ArrayList<>());
        events.get(xid).add(new RollbackEvent("BEGIN", "事务开始"));
        log.info("事务开始: {}", xid);
    }

    @EventListener
    public void onGlobalRollback(GlobalRollbackEvent event) {
        String xid = event.getXid();
        List<RollbackEvent> txEvents = events.get(xid);
        if (txEvents != null) {
            txEvents.add(new RollbackEvent("ROLLBACK", "事务回滚"));
            log.info("事务回滚: {}", xid);

            // 分析回滚原因
            analyzeRollbackCause(txEvents, xid);
        }
    }

    @EventListener
    public void onGlobalCommit(GlobalCommitEvent event) {
        String xid = event.getXid();
        List<RollbackEvent> txEvents = events.get(xid);
        if (txEvents != null) {
            txEvents.add(new RollbackEvent("COMMIT", "事务提交"));
            log.info("事务提交: {}", xid);
        }
    }

    @EventListener
    public void onBranchRegister(BranchRegisterEvent event) {
        String xid = event.getXid();
        List<RollbackEvent> txEvents = events.get(xid);
        if (txEvents != null) {
            txEvents.add(new RollbackEvent("BRANCH_REGISTER",
                    String.format("分支注册: %s-%s", event.getResourceId(), event.getBranchType())));
        }
    }

    @EventListener
    public void onException(ExceptionEvent event) {
        log.error("Seata 异常事件: {}", event.getException().getMessage(), event.getException());
    }

    private void analyzeRollbackCause(List<RollbackEvent> events, String xid) {
        log.info("=== 回滚分析报告 ===");
        log.info("事务 XID: {}", xid);
        log.info("事件数量: {}", events.size());

        events.forEach(event ->
                log.info("  {} - {}", event.getType(), event.getDescription()));

        // 清理
        this.events.remove(xid);
    }

    @Data
    @AllArgsConstructor
    private static class RollbackEvent {
        private String type;
        private String description;
    }
}
