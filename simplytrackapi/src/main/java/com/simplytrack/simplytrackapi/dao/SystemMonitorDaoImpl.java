package com.simplytrack.simplytrackapi.dao;

import com.simplytrack.simplytrackapi.SystemMonitorAgent;
import com.simplytrack.simplytrackapi.models.ProcessInfoEntity;
import com.simplytrack.simplytrackapi.models.SystemInfoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class SystemMonitorDaoImpl implements SystemMonitorDao {
    @Autowired
    private final SystemMonitorAgent agent;
    @PersistenceContext
    private EntityManager entityManager;

    public SystemMonitorDaoImpl(SystemMonitorAgent agent) {
        this.agent = agent;
    }

    @Transactional
    @Override
    public SystemInfoEntity saveSystemInfo(SystemInfoEntity systemInfo) {
        // Get the system ID
        String systemId = systemInfo.getSystemId();

        // Delete the existing SystemInfoEntity by system_id
        deleteSystemInfoBySystemId(systemId);

        // Save the new SystemInfoEntity
        entityManager.persist(systemInfo);
        return systemInfo;
    }

    private void deleteSystemInfoBySystemId(String systemId) {
        List<SystemInfoEntity> existingSystemInfos = entityManager.createQuery("SELECT s FROM SystemInfoEntity s WHERE s.systemId = :systemId", SystemInfoEntity.class).setParameter("systemId", systemId).getResultList();

        for (SystemInfoEntity existingSystemInfo : existingSystemInfos) {
            entityManager.remove(existingSystemInfo);
        }
    }
    @Override
    public List<SystemInfoEntity> getSystemInfoById(String systemId) {
        return entityManager.createQuery("SELECT s FROM SystemInfoEntity s WHERE s.systemId = :systemId", SystemInfoEntity.class)
                .setParameter("systemId", systemId)
                .getResultList();
    }
    @Override
    public List<SystemInfoEntity> getAllSystemInfo() {
        return entityManager.createQuery("SELECT s FROM SystemInfoEntity s", SystemInfoEntity.class).getResultList();
    }

    public SystemInfoEntity getSystemInfo() {
        double totalCpuUsage = agent.getTotalCpuUsage();
        long diskUsage = agent.getDiskUsage();
        long memoryUsage = agent.getMemoryUsage();
        Map<String, String> processesInfoMap = agent.getProcessesInfo();

        SystemInfoEntity systemInfo = new SystemInfoEntity();
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

        return systemInfo;
    }
}