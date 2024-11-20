package com.simplytrack.simplytrackapi.models;
// ProcessInfoEntity.java

import jakarta.persistence.*;

@Entity
@Table(name = "process_info")
public class ProcessInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pid")
    private String pid;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "cpu_usage")
    private String cpuUsage;

    @Column(name = "memory_usage")
    private String memoryUsage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_info_id")
    private SystemInfoEntity systemInfo;

    public ProcessInfoEntity(String pid, String processName, String cpuUsage, String memoryUsage) {
        this.pid = pid;
        this.processName = processName;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }

    // Constructors, getters, and setters

    public ProcessInfoEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public SystemInfoEntity getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfoEntity systemInfo) {
        this.systemInfo = systemInfo;
    }
}