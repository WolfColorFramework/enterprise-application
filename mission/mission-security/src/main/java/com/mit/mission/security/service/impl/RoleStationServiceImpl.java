package com.mit.mission.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mit.mission.security.domain.RoleStation;
import com.mit.mission.security.repository.RoleStationRepository;
import com.mit.mission.security.service.RoleStationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class RoleStationServiceImpl implements RoleStationService {

    @Autowired
    private RoleStationRepository roleStationRepository;

    @Override
    public List<String> getLineIdsByRoleIds(List<String> roleIds) {
        List<String> lineIds = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                List<RoleStation> roleStations = roleStationRepository.findAllByRoleId(roleId);
                if (!CollectionUtils.isEmpty(roleStations)) {
                    for (RoleStation roleStation : roleStations) {
                        String lineId = roleStation.getLineId();
                        if (!StringUtils.isEmpty(lineId) && !lineIds.contains(lineId)) {
                            lineIds.add(lineId);
                        }
                    }
                }

            }

        }
        return lineIds;
    }

    @Override
    public List<String> getStationIdsByRoleIds(List<String> roleIds) {
        List<String> stationIds = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                List<RoleStation> roleStations = roleStationRepository.findAllByRoleId(roleId);
                if (!CollectionUtils.isEmpty(roleStations)) {
                    for (RoleStation roleStation : roleStations) {
                        String stationId = roleStation.getStationId();
                        if (!StringUtils.isEmpty(stationId) && !stationIds.contains(stationId)) {
                            stationIds.add(stationId);
                        }
                    }
                }
            }

        }
        return stationIds;
    }

}
