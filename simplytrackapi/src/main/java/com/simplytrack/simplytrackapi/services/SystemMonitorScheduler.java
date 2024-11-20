package com.simplytrack.simplytrackapi.services;

import com.simplytrack.simplytrackapi.SystemMonitorAgent;
import com.simplytrack.simplytrackapi.dao.SystemMonitorDao;
import com.simplytrack.simplytrackapi.models.ProcessInfoEntity;
import com.simplytrack.simplytrackapi.models.SystemInfoEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SystemMonitorScheduler {
    private final SystemMonitorDao systemMonitorDao;
    private final SystemMonitorAgent agent;

    public SystemMonitorScheduler(SystemMonitorDao systemMonitorDao, SystemMonitorAgent agent) {
        this.systemMonitorDao = systemMonitorDao;
        this.agent = agent;
    }

    @Scheduled(fixedRate = 600000) // Run every 1 minute
    public void collectSystemInfo() {
        String systemId = agent.getSystemId();
        double totalCpuUsage = agent.getTotalCpuUsage();
        long diskUsage = agent.getDiskUsage();
        long memoryUsage = agent.getMemoryUsage();
        Map<String, String> processesInfoMap = agent.getProcessesInfo();

        SystemInfoEntity systemInfo = new SystemInfoEntity();
        systemInfo.setSystemId(systemId);
        systemInfo.setTotalCpuUsage(totalCpuUsage);
        systemInfo.setDiskUsage(diskUsage);
        systemInfo.setMemoryUsage(memoryUsage);

        for (Map.Entry<String, String> entry : processesInfoMap.entrySet()) {
            String pid = entry.getKey();
            String[] parts = entry.getValue().split(",");
            if (parts.length >= 3) {
                String processName = parts[0];
                String cpuUsage = parts[1];
                String memoryusage = parts[2];
                ProcessInfoEntity processInfo = new ProcessInfoEntity(pid, processName, cpuUsage, memoryusage);
                processInfo.setSystemInfo(systemInfo);
                systemInfo.getProcessInfos().add(processInfo);
            }
        }

        systemMonitorDao.saveSystemInfo(systemInfo);
    }
}