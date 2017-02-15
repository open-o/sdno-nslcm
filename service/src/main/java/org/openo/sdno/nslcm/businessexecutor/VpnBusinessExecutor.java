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

package org.openo.sdno.nslcm.businessexecutor;

import java.util.ArrayList;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnConnectionSbiService;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnGatewaySbiService;
import org.openo.sdno.nslcm.sbi.overlayvpn.VpnSbiService;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Executor class of OverlayVpn Business.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component
public class VpnBusinessExecutor {

    @Autowired
    private VpnConnectionSbiService connectionSbiService;

    @Autowired
    private VpnGatewaySbiService vpnGatewaySbiService;

    @Autowired
    private VpnSbiService vpnSbiService;

    /**
     * Deploy Vpn related business.<br>
     * 
     * @param vpnModel Vpn need to deploy
     * @return Vpn deployed
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    public NbiVpn executeDeploy(NbiVpn vpnModel) throws ServiceException {

        // Deploy Vpn
        NbiVpn vpn = vpnSbiService.createVpn(vpnModel);
        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());

        // Deploy Site Gateway
        NbiVpnGateway siteGateway = vpnGatewaySbiService.createVpnGateway(vpnModel.getVpnGateways().get(0));
        vpn.getVpnGateways().add(siteGateway);

        // Deploy Vpc Gateway
        NbiVpnGateway vpcGateway = vpnGatewaySbiService.createVpnGateway(vpnModel.getVpnGateways().get(1));
        vpn.getVpnGateways().add(vpcGateway);

        // Deploy Vpn Connection
        connectionSbiService.createVpnConnection(vpnModel.getVpnConnections().get(0));

        return vpn;
    }

    /**
     * UnDeploy vpn related business.<br>
     * 
     * @param siteModel Vpn need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public void executeUnDeploy(NbiVpn vpnModel) throws ServiceException {

        // UnDeploy VpnConnection
        connectionSbiService.deleteVpnConnection(vpnModel.getVpnConnections().get(0).getUuid());

        // UnDeploy Site Gateway
        vpnGatewaySbiService.deleteVpnGateway(vpnModel.getVpnGateways().get(0).getUuid());

        // UnDeploy Vpn Gateway
        vpnGatewaySbiService.deleteVpnGateway(vpnModel.getVpnGateways().get(1).getUuid());

        // UnDeploy Vpn
        vpnSbiService.deleteVpn(vpnModel.getUuid());
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
