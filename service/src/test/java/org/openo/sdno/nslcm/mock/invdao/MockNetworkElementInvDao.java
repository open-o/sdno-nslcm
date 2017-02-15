/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.nslcm.mock.invdao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;

import mockit.Mock;
import mockit.MockUp;

public class MockNetworkElementInvDao extends MockUp<NetworkElementInvDao> {

    private List<NetworkElementMO> neMOList = new ArrayList<NetworkElementMO>();

    private NetworkElementMO localCpeNeMO = new NetworkElementMO();

    private NetworkElementMO cloudCpeNeMO = new NetworkElementMO();

    private NetworkElementMO dcGwNeMO = new NetworkElementMO();

    private NetworkElementMO dcFwNeMO = new NetworkElementMO();

    private NetworkElementMO dcLbNeMO = new NetworkElementMO();

    public MockNetworkElementInvDao() {
        neMOList.add(loadLocalCpeNe());
        neMOList.add(loadCloudCpeNe());
        neMOList.add(loadDcGwNe());
        neMOList.add(loadDcFwNe());
        neMOList.add(loadDcLbNe());
    }

    private NetworkElementMO loadLocalCpeNe() {
        localCpeNeMO.setControllerID(Arrays.asList("ControllerId"));
        localCpeNeMO.setName("LocalCpe");
        localCpeNeMO.setNativeID("LocalCpeNativeID");
        localCpeNeMO.setId("LocalCpeNeId");
        localCpeNeMO.setNeRole(CpeRoleType.THIN_CPE.getName());
        localCpeNeMO.setProductName("AR169FW-L");
        return localCpeNeMO;
    }

    private NetworkElementMO loadCloudCpeNe() {
        cloudCpeNeMO.setControllerID(Arrays.asList("ControllerId"));
        cloudCpeNeMO.setName("CloudCpe");
        cloudCpeNeMO.setNativeID("CloudCpeNativeID");
        cloudCpeNeMO.setId("CloudCpeNeId");
        cloudCpeNeMO.setNeRole(CpeRoleType.CLOUD_CPE.getName());
        return cloudCpeNeMO;
    }

    private NetworkElementMO loadDcGwNe() {
        dcGwNeMO.setId("DcGwNeId");
        dcGwNeMO.setName("DcGw");
        dcGwNeMO.setIpAddress("192.168.1.1");
        return dcGwNeMO;
    }

    private NetworkElementMO loadDcFwNe() {
        dcFwNeMO.setId("DcFwNeId");
        dcFwNeMO.setName("DcFw");
        dcFwNeMO.setIpAddress("192.168.1.2");
        return dcFwNeMO;
    }

    private NetworkElementMO loadDcLbNe() {
        dcLbNeMO.setId("DcLbNeId");
        dcLbNeMO.setName("DcLb");
        dcLbNeMO.setIpAddress("192.168.1.3");
        return dcLbNeMO;
    }

    @Mock
    public NetworkElementMO query(String id) throws ServiceException {
        for(NetworkElementMO curNe : neMOList) {
            if(curNe.getId().equals(id)) {
                return curNe;
            }
        }
        return null;
    }

    @Mock
    public List<NetworkElementMO> query(Map<String, String> condition) throws ServiceException {
        if(condition.containsKey("neRole")) {
            if(condition.containsValue(CpeRoleType.THIN_CPE.getName())) {
                return Arrays.asList(localCpeNeMO);
            } else {
                return Arrays.asList(cloudCpeNeMO);
            }
        } else if(condition.containsKey("ipAddress")) {
            for(NetworkElementMO curNe : neMOList) {
                if(condition.containsValue(curNe.getIpAddress())) {
                    return Arrays.asList(curNe);
                }
            }
        }
        return Arrays.asList();
    }

    @Mock
    public void updateMO(NetworkElementMO curNeMO) throws ServiceException {
        return;
    }

}
