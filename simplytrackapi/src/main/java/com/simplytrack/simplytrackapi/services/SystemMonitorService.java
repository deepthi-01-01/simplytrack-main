package com.simplytrack.simplytrackapi.services;

import com.simplytrack.simplytrackapi.dao.SystemMonitorDao;
import com.simplytrack.simplytrackapi.models.ProcessInfo;
import com.simplytrack.simplytrackapi.models.ProcessInfoEntity;
import com.simplytrack.simplytrackapi.models.SystemInfo;
import com.simplytrack.simplytrackapi.models.SystemInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemMonitorService {
    @Autowired
    private final SystemMonitorDao systemMonitorDao;

    public SystemMonitorService(SystemMonitorDao systemMonitorDao) {
        this.systemMonitorDao = systemMonitorDao;
    }

    public List<SystemInfo> getAllSystemInfo() {
        List<SystemInfoEntity> systemInfoEntities = systemMonitorDao.getAllSystemInfo();
        return systemInfoEntities.stream().map(this::mapToSystemInfo).collect(Collectors.toList());
    }

    public SystemInfo getLatestSystemInfo() {
        List<SystemInfoEntity> systemInfoEntities = systemMonitorDao.getAllSystemInfo();
        if (!systemInfoEntities.isEmpty()) {
            SystemInfoEntity latestSystemInfoEntity = systemInfoEntities.get(systemInfoEntities.size() - 1);
            return mapToSystemInfo(latestSystemInfoEntity);
        }
        return null;
    }
    public List<SystemInfo> getSystemInfoById(String systemId) {
        List<SystemInfoEntity> systemInfoEntities = systemMonitorDao.getSystemInfoById(systemId);
        return systemInfoEntities.stream().map(this::mapToSystemInfo).collect(Collectors.toList());
    }
    private SystemInfo mapToSystemInfo(SystemInfoEntity systemInfoEntity) {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setSystemId(systemInfoEntity.getSystemId());
        systemInfo.setTotalCpuUsage(systemInfoEntity.getTotalCpuUsage());
        systemInfo.setDiskUsage(systemInfoEntity.getDiskUsage());
        systemInfo.setMemoryUsage(systemInfoEntity.getMemoryUsage());

        List<ProcessInfo> processInfos = systemInfoEntity.getProcessInfos().stream().map(this::mapToProcessInfo).collect(Collectors.toList());
        systemInfo.setProcessInfos(processInfos);

        return systemInfo;
    }

    private ProcessInfo mapToProcessInfo(ProcessInfoEntity processInfoEntity) {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setPid(processInfoEntity.getPid());
        processInfo.setProcessName(processInfoEntity.getProcessName());
        processInfo.setCpuUsage(processInfoEntity.getCpuUsage());
        processInfo.setMemoryUsage(processInfoEntity.getMemoryUsage());
        return processInfo;
    }
}