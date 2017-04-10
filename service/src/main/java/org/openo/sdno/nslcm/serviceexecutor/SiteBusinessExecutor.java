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
import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.config.SiteParamConfigReader;
import org.openo.sdno.nslcm.sbi.site.CloudCpeSbiService;
import org.openo.sdno.nslcm.sbi.site.LocalCpeSbiService;
import org.openo.sdno.nslcm.sbi.site.SiteSbiService;
import org.openo.sdno.nslcm.sbi.site.SubnetSbiService;
import org.openo.sdno.nslcm.sbi.site.VlanSbiService;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteBusinessExecutor.class);

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

    @Autowired
    private SiteParamConfigReader siteParamConfigReader;

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

        // Wait for Cpe Online
        try {
            Thread.sleep(siteParamConfigReader.getCpeOneTime());
        } catch(InterruptedException e) {
            LOGGER.error("Wait for CpeOnline is interrupted, cpe device may not be online.");
            throw new ServiceException("Wait for CpeOnline is interrupted");
        }

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

        // UnDeploy Vlan
        vlanSbiService.deleteVlan(siteModel.getVlans().get(0).getUuid());

        // UnDeploy LocalCpe
        localCpeSbiService.deleteLocalCpe(siteModel.getLocalCpeModels().get(0).getUuid());

        // UnDeploy CloudCpe
        cloudCpeSbiService.deleteCloudCpe(siteModel.getCloudCpeModels().get(0).getUuid());

        // UnDeploy Site
        siteSbiService.deleteSite(siteModel.getUuid());
    }

    /**
     * UnDeploy subnet.<br>
     * 
     * @param siteModel Site need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public void executeUnDeploySubnet(NbiSiteModel siteModel) throws ServiceException {

        // UnDeploy Subnet
        subnetSbiService.deleteSubnet(siteModel.getSubnets().get(0).getUuid());
    }

    /**
     * Query SiteModel.<br>
     * 
     * @param siteUuid Site Uuid
     * @return Site queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiSiteModel executeQuery(String siteUuid) throws ServiceException {
        NbiSiteModel siteModel = siteSbiService.querySite(siteUuid);
        siteModel.setLocalCpeModels(convertLocalCpeNeToModel(siteModel.getLocalCpes()));
        siteModel.setCloudCpeModels(convertCloudCpeNeToModel(siteModel.getCloudCpes()));
        return siteModel;
    }

    private List<NbiCloudCpeModel> convertCloudCpeNeToModel(List<NetworkElementMO> neList) throws ServiceException {
        List<NbiCloudCpeModel> cloudCpeModelList = new ArrayList<>();
        for(NetworkElementMO curCpeNe : neList) {
            cloudCpeModelList.add(cloudCpeSbiService.queryCloudCpe(curCpeNe.getId()));
        }
        return cloudCpeModelList;
    }

    private List<NbiLocalCpeModel> convertLocalCpeNeToModel(List<NetworkElementMO> neList) {
        List<NbiLocalCpeModel> localCpeModelList = new ArrayList<>();
        for(NetworkElementMO curCpeNe : neList) {
            NbiLocalCpeModel localCpeModel = new NbiLocalCpeModel();
            localCpeModel.setUuid(curCpeNe.getId());
            localCpeModelList.add(localCpeModel);
        }
        return localCpeModelList;
    }

}
