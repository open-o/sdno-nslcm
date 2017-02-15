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

package org.openo.sdno.nslcm.model.translator;

import java.util.ArrayList;
import java.util.Arrays;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.nslcm.config.OsDriverParamConfigReader;
import org.openo.sdno.nslcm.config.SiteParamConfigReader;
import org.openo.sdno.nslcm.dao.inf.IBaseResourceDao;
import org.openo.sdno.nslcm.model.template.OverlayTemplateModel;
import org.openo.sdno.nslcm.model.template.OverlayVpnBusinessModel;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.esr.invdao.VimInvDao;
import org.openo.sdno.overlayvpn.esr.model.Vim;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicechain.ServicePathHop;
import org.openo.sdno.overlayvpn.model.servicemodel.SubNet;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnConnection;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnGateway;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Model translator class of OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Component
public class OverlayVpnTranslator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverlayVpnTranslator.class);

    @Autowired
    private IBaseResourceDao baseResourceDao;

    @Autowired
    private SiteParamConfigReader siteParamConfigReader;

    @Autowired
    private OsDriverParamConfigReader osDriverParamConfigReader;

    @Autowired
    private VimInvDao vimInvDao;

    /**
     * Translate OverlayVpn model.<br>
     * 
     * @param templateModel OverlayTemplateModel object
     * @param instanceId Nslcm Instance Id
     * @return OverlayVpnBusinessModel translated
     * @throws ServiceException when translate failed
     * @since SDNO 0.5
     */
    public OverlayVpnBusinessModel translateOverlayVpnModel(OverlayTemplateModel templateModel, String instanceId)
            throws ServiceException {

        OverlayVpnBusinessModel businessModel = new OverlayVpnBusinessModel();

        businessModel.setSiteModel(translateSiteModel(templateModel));
        businessModel.setServiceChainPathModel(translateServiceChainPath(templateModel, instanceId));
        businessModel.setVpcModel(translateVpc(templateModel));
        businessModel.setVpnModel(translateVpn(templateModel, instanceId, businessModel.getVpcModel().getUuid()));

        return businessModel;
    }

    private NbiSiteModel translateSiteModel(OverlayTemplateModel templateModel) throws ServiceException {

        SiteMO brsSiteMO = baseResourceDao.querySiteByName(templateModel.getSiteName());
        NetworkElementMO localCpeNe = baseResourceDao.querySiteCpeByType(brsSiteMO.getId(), CpeRoleType.THIN_CPE);
        NetworkElementMO cloudCpeNe = baseResourceDao.querySiteCpeByType(brsSiteMO.getId(), CpeRoleType.CLOUD_CPE);

        // Create SiteModel
        NbiSiteModel siteModel = new NbiSiteModel();
        siteModel.setUuid(brsSiteMO.getId());
        siteModel.initBasicInfo(brsSiteMO.getName(), brsSiteMO.getTenantID(), null, brsSiteMO.getDescription());
        siteModel.setLocalCpeType(localCpeNe.getProductName());
        siteModel.setReliability(siteParamConfigReader.getSiteReliability());
        siteModel.setSiteDescriptor(siteParamConfigReader.getSiteDescriptor());
        siteModel.setIsEncrypt(siteParamConfigReader.getSiteIsEncrypt());

        // Create LocalCpe
        NbiLocalCpeModel localCpeModel = new NbiLocalCpeModel();
        localCpeModel.setUuid(localCpeNe.getId());
        localCpeModel.setName(localCpeNe.getName());
        localCpeModel.setTenantId(brsSiteMO.getTenantID());
        localCpeModel.setDescription(localCpeNe.getDescription());
        localCpeModel.setSiteId(brsSiteMO.getId());
        localCpeModel.setEsn(localCpeNe.getSerialNumber());
        localCpeModel.setControllerId(localCpeNe.getControllerID().get(0));
        localCpeModel.setLocalCpeType(localCpeNe.getProductName());

        siteModel.setLocalCpeModels(Arrays.asList(localCpeModel));

        // Create CloudCpe
        NbiCloudCpeModel cloudCpeModel = new NbiCloudCpeModel();
        cloudCpeModel.setUuid(cloudCpeNe.getId());
        cloudCpeModel.setName(cloudCpeNe.getName());
        cloudCpeModel.setTenantId(brsSiteMO.getTenantID());
        cloudCpeModel.setDescription(cloudCpeNe.getDescription());
        cloudCpeModel.setSiteId(brsSiteMO.getId());
        cloudCpeModel.setEsn(cloudCpeNe.getSerialNumber());
        cloudCpeModel.setControllerId(cloudCpeNe.getControllerID().get(0));

        siteModel.setCloudCpeModels(Arrays.asList(cloudCpeModel));

        // Create VlanModel
        NbiVlanModel vlanModel = new NbiVlanModel();
        vlanModel.setUuid(UuidUtils.createUuid());
        vlanModel.setName("Vlan_" + templateModel.getVpnName());
        vlanModel.setTenantId(brsSiteMO.getTenantID());
        vlanModel.setDescription(templateModel.getVpnDescription());
        vlanModel.setSiteId(brsSiteMO.getId());
        vlanModel.setVlanId(templateModel.getSubnetVlan());

        siteModel.setVlans(Arrays.asList(vlanModel));

        // Create SubnetModel
        NbiSubnetModel subnetModel = new NbiSubnetModel();
        subnetModel.setUuid(UuidUtils.createUuid());
        subnetModel.setName("Subnet_" + templateModel.getVpnName());
        subnetModel.setTenantId(brsSiteMO.getTenantID());
        subnetModel.setDescription(templateModel.getVpnDescription());
        subnetModel.setSiteId(brsSiteMO.getId());
        subnetModel.setCidrBlock(templateModel.getSiteCidr());
        subnetModel.setVni(templateModel.getSiteVni().toString());

        siteModel.setSubnets(Arrays.asList(subnetModel));

        return siteModel;
    }

    private NbiVpn translateVpn(OverlayTemplateModel templateModel, String instanceId, String vpcId)
            throws ServiceException {

        SiteMO brsSiteMO = baseResourceDao.querySiteByName(templateModel.getSiteName());

        // Create Vpn
        NbiVpn vpn = new NbiVpn();
        vpn.setUuid(instanceId);
        vpn.setName(templateModel.getVpnName());
        vpn.setTenantId(brsSiteMO.getTenantID());
        vpn.setDescription(templateModel.getVpnDescription());
        vpn.setVpnDescriptor(siteParamConfigReader.getSiteDescriptor());

        // Create Site Gateway
        NbiVpnGateway siteGateway = new NbiVpnGateway();
        siteGateway.setUuid(UuidUtils.createUuid());
        siteGateway.setName("SiteGateway_" + templateModel.getVpnName());
        siteGateway.setTenantId(brsSiteMO.getTenantID());
        siteGateway.setDescription(templateModel.getVpnDescription());
        siteGateway.setSiteId(brsSiteMO.getId());
        siteGateway.setVpnId(vpn.getUuid());

        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());
        vpn.getVpnGateways().add(siteGateway);

        // Create Vpc Gateway
        NbiVpnGateway vpcGateway = new NbiVpnGateway();
        vpcGateway.setUuid(UuidUtils.createUuid());
        siteGateway.setName("VpcGateway_" + templateModel.getVpnName());
        siteGateway.setTenantId(brsSiteMO.getTenantID());
        siteGateway.setDescription(templateModel.getVpnDescription());
        siteGateway.setSiteId(brsSiteMO.getId());
        siteGateway.setVpcId(vpcId);

        vpn.getVpnGateways().add(vpcGateway);

        // Create Vpn Connection
        NbiVpnConnection vpnConnection = new NbiVpnConnection();
        vpnConnection.setUuid(UuidUtils.createUuid());
        vpnConnection.setName("VpnConnection_" + templateModel.getVpnName());
        vpnConnection.setTenantId(brsSiteMO.getTenantID());
        vpnConnection.setDescription(templateModel.getVpnDescription());
        vpnConnection.setaEndVpnGatewayId(siteGateway.getUuid());
        vpnConnection.setzEndVpnGatewayId(vpcGateway.getUuid());
        vpnConnection.setVpnId(vpn.getUuid());

        vpn.setVpnConnections(Arrays.asList(vpnConnection));

        return vpn;
    }

    private ServiceChainPath translateServiceChainPath(OverlayTemplateModel templateModel, String instanceId)
            throws ServiceException {

        ServiceChainPath sfpPath = new ServiceChainPath();
        sfpPath.setUuid(instanceId);
        sfpPath.setName("Sfp_" + templateModel.getVpnName());
        sfpPath.setDescription(templateModel.getVpnDescription());
        NetworkElementMO gwNe = baseResourceDao.queryNeByIpAddress(templateModel.getDcGwIp());
        sfpPath.setScfNeId(gwNe.getId());

        NetworkElementMO fwNe = baseResourceDao.queryNeByIpAddress(templateModel.getDcFwIp());
        ServicePathHop fwPathHop = new ServicePathHop();
        fwPathHop.setHopNumber(1);
        fwPathHop.setSfiId(fwNe.getId());
        sfpPath.setServicePathHops(new ArrayList<ServicePathHop>());
        sfpPath.getServicePathHops().add(fwPathHop);

        NetworkElementMO lbNe = baseResourceDao.queryNeByIpAddress(templateModel.getDcLbIp());
        ServicePathHop lbPathHop = new ServicePathHop();
        lbPathHop.setHopNumber(2);
        lbPathHop.setSfiId(lbNe.getId());
        sfpPath.getServicePathHops().add(lbPathHop);

        return sfpPath;
    }

    private Vpc translateVpc(OverlayTemplateModel templateModel) throws ServiceException {
        Vpc vpc = new Vpc();
        vpc.setUuid(UuidUtils.createUuid());
        vpc.setName(templateModel.getVpcName());
        vpc.setDescription(templateModel.getVpnDescription());

        vpc.setOsControllerId(queryOsControllerId());

        vpc.setSubnet(new SubNet());

        vpc.getSubnet().setUuid(UuidUtils.createUuid());
        vpc.getSubnet().setName(templateModel.getVpcSubnetName());
        vpc.getSubnet().setDescription(templateModel.getVpnDescription());
        vpc.getSubnet().setVpcId(vpc.getUuid());
        vpc.getSubnet().setCidr(templateModel.getVpcSubnetCidr());
        vpc.getSubnet().setVni(templateModel.getVpcVni());

        return vpc;
    }

    private String queryOsControllerId() throws ServiceException {
        String vimName = osDriverParamConfigReader.getVimName();
        Vim osVim = vimInvDao.queryVimByName(vimName);
        if(null == osVim) {
            LOGGER.error("This openstack controller does not exist");
            throw new ServiceException("This openstack controller does not exist");
        }
        return osVim.getVimId();
    }

}
