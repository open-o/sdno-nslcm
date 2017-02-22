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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.model.template.OverlayVpnBusinessModel;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business executor of OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component
public class OverlayVpnBusinessExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverlayVpnBusinessExecutor.class);

    @Autowired
    private ServiceChainBusinessExecutor serviceChainBusinessExceutor;

    @Autowired
    private SiteBusinessExecutor siteBusinessExecutor;

    @Autowired
    private VpcBusinessExecutor vpcBusinessExecutor;

    @Autowired
    private VpnBusinessExecutor vpnBusinessExecutor;

    /**
     * Deploy OverlayVpn business.<br>
     * 
     * @param businessModel OverlayVpnBusinessModel need to deploy
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    public NbiVpn executeDeploy(OverlayVpnBusinessModel businessModel) throws ServiceException {
        vpcBusinessExecutor.executeDeploy(businessModel.getVpcModel());
        siteBusinessExecutor.executeDeploy(businessModel.getSiteModel());
        serviceChainBusinessExceutor.executeDeploy(businessModel.getServiceChainPathModel());
        return vpnBusinessExecutor.executeDeploy(businessModel.getVpnModel());
    }

    /**
     * UnDeploy OverlayVpn business.<br>
     * 
     * @param businessModel OverlayVpnBusinessModel need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public Map<String, String> executeUnDeploy(OverlayVpnBusinessModel businessModel) throws ServiceException {
        serviceChainBusinessExceutor.executeUnDeploy(businessModel.getServiceChainPathModel());
        vpnBusinessExecutor.executeUnDeploy(businessModel.getVpnModel());
        siteBusinessExecutor.executeUnDeploy(businessModel.getSiteModel());
        vpcBusinessExecutor.executeUnDeploy(businessModel.getVpcModel());
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("errorCode", businessModel.getVpnModel().getId());
        return resultMap;
    }

    /**
     * Query OverlayVpn business.<br>
     * 
     * @param vpnUuid Vpn Uuid
     * @return OverlayVpn business queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public OverlayVpnBusinessModel executeQuery(String vpnUuid) throws ServiceException {

        OverlayVpnBusinessModel businessModel = new OverlayVpnBusinessModel();

        NbiVpn vpn = vpnBusinessExecutor.executeQuery(vpnUuid);
        businessModel.setVpnModel(vpn);

        // ServiceChain has the same uuid with vpn
        ServiceChainPath sfp = serviceChainBusinessExceutor.executeQuery(vpnUuid);
        businessModel.setServiceChainPathModel(sfp);

        Set<String> siteUuidList = vpn.getSiteList();
        if(CollectionUtils.isEmpty(siteUuidList)) {
            LOGGER.error("No site related to vpn");
            throw new ServiceException("No site related to vpn");
        }

        String siteUuid = siteUuidList.iterator().next();
        NbiSiteModel siteModel = siteBusinessExecutor.executeQuery(siteUuid);
        businessModel.setSiteModel(siteModel);

        Set<String> vpcUuidList = vpn.getVpcList();
        if(CollectionUtils.isEmpty(vpcUuidList)) {
            LOGGER.error("No vpc related to vpn");
            throw new ServiceException("No vpc related to vpn");
        }

        String vpcUuid = vpcUuidList.iterator().next();
        Vpc vpcModel = vpcBusinessExecutor.executeQuery(vpcUuid);
        businessModel.setVpcModel(vpcModel);

        return businessModel;
    }

}
