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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.sbi.site.CloudCpeSbiService;
import org.openo.sdno.nslcm.sbi.site.LocalCpeSbiService;
import org.openo.sdno.nslcm.sbi.site.SiteSbiService;
import org.openo.sdno.nslcm.sbi.site.SubnetSbiService;
import org.openo.sdno.nslcm.sbi.site.VlanSbiService;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Executor class of Site Business.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component
public class SiteBusinessExecutor {

    @Autowired
    private CloudCpeSbiService cloudCpeSbiService;

    @Autowired
    private LocalCpeSbiService localCpeSbiService;

    @Autowired
    private SiteSbiService siteSbiService;

    @Autowired
    private SubnetSbiService subnetSbiService;

    @Autowired
    private VlanSbiService vlanSbiService;

    /**
     * Deploy site related business.<br>
     * 
     * @param siteModel Site need to deploy
     * @return Site deployed
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    public NbiSiteModel executeDeploy(NbiSiteModel siteModel) throws ServiceException {

        // Deploy Site Model
        siteSbiService.createSite(siteModel);

        // Deploy CloudCpe
        cloudCpeSbiService.createCloudCpe(siteModel.getCloudCpeModels().get(0));

        // Deploy LocalCpe
        localCpeSbiService.createLocalCpe(siteModel.getLocalCpeModels().get(0));

        // Deploy Vlan
        vlanSbiService.createVlan(siteModel.getVlans().get(0));

        // Deploy Subnet
        subnetSbiService.createSubnet(siteModel.getSubnets().get(0));

        return siteModel;
    }

    /**
     * UnDeploy site related business.<br>
     * 
     * @param siteModel Site need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public void executeUnDeploy(NbiSiteModel siteModel) throws ServiceException {

        // UnDeploy Subnet
        subnetSbiService.deleteSubnet(siteModel.getSubnets().get(0).getUuid());

        // UnDeploy Vlan
        vlanSbiService.deleteVlan(siteModel.getVlans().get(0).getUuid());

        // UnDeploy LocalCpe
        localCpeSbiService.deleteLocalCpe(siteModel.getLocalCpeModels().get(0).getUuid());

        // UnDeploy CloudCpe
        cloudCpeSbiService.deleteCloudCpe(siteModel.getCloudCpeModels().get(0).getUuid());

        // UnDeploy Site
        siteSbiService.deleteSite(siteModel.getUuid());
    }

}
