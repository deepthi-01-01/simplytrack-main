package com.simplytrack.simplytrackapi.dao;

import com.simplytrack.simplytrackapi.models.SystemInfoEntity;

import java.util.List;

public interface SystemMonitorDao {
    SystemInfoEntity saveSystemInfo(SystemInfoEntity systemInfo);

    List<SystemInfoEntity> getAllSystemInfo();

    public List<SystemInfoEntity> getSystemInfoById(String systemId);
}
