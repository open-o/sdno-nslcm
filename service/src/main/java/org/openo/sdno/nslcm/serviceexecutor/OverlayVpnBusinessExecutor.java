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

package org.openo.sdno.nslcm.serviceexecutor;

import java.util.ArrayList;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnConnectionSbiService;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnGatewaySbiService;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnSbiService;
import org.openo.sdno.nslcm.util.RecordProgress;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnConnection;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Executor class of OverlayVpn Business.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component
public class OverlayVpnBusinessExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverlayVpnBusinessExecutor.class);

    @Autowired
    private VpnConnectionSbiService connectionSbiService;

    @Autowired
    private VpnGatewaySbiService vpnGatewaySbiService;

    @Autowired
    private VpnSbiService vpnSbiService;

    /**
     * Deploy Vpn related business.<br>
     * 
     * @param instanceId ID of the SDN-O service instance to be instantiated
     * @param vpnModel Vpn need to deploy
     * @return Vpn deployed
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    public NbiVpn executeDeploy(String instanceId, NbiVpn vpnModel) throws ServiceException {

        // Deploy Vpn
        NbiVpn vpn = vpnSbiService.createVpn(vpnModel);
        RecordProgress.increaseCurrentStep(instanceId);

        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());
        vpn.setVpnConnections(new ArrayList<NbiVpnConnection>());

        if(CollectionUtils.isEmpty(vpnModel.getVpnGateways())
                || CollectionUtils.isEmpty(vpnModel.getVpnConnections())) {
            LOGGER.info("No Vpn Gateways or Connections in Vpn model");
            return vpn;
        }

        for(NbiVpnGateway curGateway : vpnModel.getVpnGateways()) {
            NbiVpnGateway createdVpnGateway = vpnGatewaySbiService.createVpnGateway(curGateway);
            vpn.getVpnGateways().add(createdVpnGateway);
            RecordProgress.increaseCurrentStep(instanceId);
        }

        for(NbiVpnConnection vpnConnection : vpnModel.getVpnConnections()) {
            NbiVpnConnection createdVpnConnection = connectionSbiService.createVpnConnection(vpnConnection);
            vpn.getVpnConnections().add(createdVpnConnection);
            RecordProgress.increaseCurrentStep(instanceId);
        }

        return vpn;
    }

    /**
     * UnDeploy vpn related business.<br>
     * 
     * @param instanceId ID of the SDN-O service instance to be instantiated
     * @param siteModel Vpn need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public void executeUnDeploy(String instanceId, NbiVpn vpnModel) throws ServiceException {

        // UnDeploy VpnConnections
        for(NbiVpnConnection vpnConnection : vpnModel.getVpnConnections()) {
            connectionSbiService.deleteVpnConnection(vpnConnection.getId());
            RecordProgress.increaseCurrentStep(instanceId);
        }

        // UnDeploy VpnGateways
        for(NbiVpnGateway vpnGateway : vpnModel.getVpnGateways()) {
            vpnGatewaySbiService.deleteVpnGateway(vpnGateway.getId());
            RecordProgress.increaseCurrentStep(instanceId);
        }

        // UnDeploy Vpn
        vpnSbiService.deleteVpn(vpnModel.getId());
        RecordProgress.increaseCurrentStep(instanceId);
    }

    /**
     * Query OverlayVpn Model.<br>
     * 
     * @param vpnUuid Vpn Uuid
     * @return OverlayVpn queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiVpn executeQuery(String vpnUuid) throws ServiceException {
        return vpnSbiService.queryVpn(vpnUuid);
    }

}
