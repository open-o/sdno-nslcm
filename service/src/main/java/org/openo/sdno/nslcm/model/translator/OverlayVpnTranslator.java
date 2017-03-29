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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.nslcm.config.OsDriverParamConfigReader;
import org.openo.sdno.nslcm.config.SiteParamConfigReader;
import org.openo.sdno.nslcm.dao.inf.IBaseResourceDao;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.nslcm.model.Site2DCBusinessModel;
import org.openo.sdno.nslcm.model.Site2DCTemplateModel;
import org.openo.sdno.nslcm.model.TemplateModel;
import org.openo.sdno.nslcm.model.VoLteBusinessModel;
import org.openo.sdno.nslcm.model.VoLteTemplateModel;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
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
import org.openo.sdno.overlayvpn.model.v2.overlay.Site2DCVpnType;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
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

    @Autowired
    private LogicalTernminationPointInvDao ltpInvDao;

    /**
     * Translate OverlayVpn model.<br>
     * 
     * @param templateModel template parameter
     * @param instanceId Nslcm Instance Id
     * @param templateName OverlayVpn template name
     * @return BusinessModel translated
     * @throws ServiceException when translate failed
     * @since SDNO 0.5
     */
    public BusinessModel translateVpnModel(Map<String, Object> templateParameter, String instanceId,
            String templateName) throws ServiceException {

        // Create Vpn template model and validate
        TemplateModel vpnTemplateModel = null;
        if(Const.SITE2DC_TEMPLATE_NAME.equals(templateName)) {
            vpnTemplateModel = JsonUtil.fromJson(JsonUtil.toJson(templateParameter), Site2DCTemplateModel.class);
        } else {
            vpnTemplateModel = JsonUtil.fromJson(JsonUtil.toJson(templateParameter), VoLteTemplateModel.class);
        }

        ValidationUtil.validateModel(vpnTemplateModel);

        if(Const.SITE2DC_TEMPLATE_NAME.equals(templateName)) {
            return translateSite2DCModel((Site2DCTemplateModel)vpnTemplateModel, instanceId);
        } else {
            return translateVoLTEModel((VoLteTemplateModel)vpnTemplateModel, instanceId);
        }
    }

    private Site2DCBusinessModel translateSite2DCModel(Site2DCTemplateModel templateModel, String instanceId)
            throws ServiceException {
        Site2DCBusinessModel businessModel = new Site2DCBusinessModel();

        businessModel.setSiteModel(translateSiteModel(templateModel));
        businessModel.setServiceChainPathModel(translateServiceChainPath(templateModel, instanceId));
        String openStackName = osDriverParamConfigReader.getVimName();
        businessModel.setVpcModel(translateVpc(templateModel.getVpcName(), templateModel.getVpcSubnetName(),
                templateModel.getVpcSubnetCidr(), templateModel.getVpcVni(), openStackName));
        businessModel
                .setVpnModel(translateSite2DCVpn(templateModel, instanceId, businessModel.getVpcModel().getUuid()));

        return businessModel;
    }

    private VoLteBusinessModel translateVoLTEModel(VoLteTemplateModel templateModel, String instanceId)
            throws ServiceException {
        VoLteBusinessModel businessModel = new VoLteBusinessModel();

        businessModel.setCoreVpcModel(translateVpc(templateModel.getCoreVpcName(), templateModel.getCoreVpcSubnetName(),
                templateModel.getCoreVpcSubnetCidr(), templateModel.getCoreVpcVni(),
                templateModel.getCoreVpcOpenStackName()));
        businessModel.setEdgeVpc1Model(translateVpc(templateModel.getEdgeVpc1Name(),
                templateModel.getEdgeVpc1SubnetName(), templateModel.getEdgeVpc1SubnetCidr(),
                templateModel.getEdgeVpc1Vni(), templateModel.getEdgeVpc1OpenStackName()));
        businessModel.setEdgeVpc2Model(translateVpc(templateModel.getEdgeVpc2Name(),
                templateModel.getEdgeVpc2SubnetName(), templateModel.getEdgeVpc2SubnetCidr(),
                templateModel.getEdgeVpc2Vni(), templateModel.getEdgeVpc2OpenStackName()));

        businessModel.setVpnModel(translateVoLteVpn(templateModel, instanceId, businessModel));

        return businessModel;
    }

    private NbiSiteModel translateSiteModel(Site2DCTemplateModel templateModel) throws ServiceException {

        SiteMO brsSiteMO = baseResourceDao.querySiteByName(templateModel.getSiteName());
        NetworkElementMO localCpeNe = baseResourceDao.querySiteCpeByType(brsSiteMO.getId(), CpeRoleType.THIN_CPE);
        NetworkElementMO cloudCpeNe = baseResourceDao.querySiteCpeByType(brsSiteMO.getId(), CpeRoleType.CLOUD_CPE);

        // Create SiteModel
        NbiSiteModel siteModel = new NbiSiteModel();
        siteModel.setUuid(brsSiteMO.getId());
        siteModel.initBasicInfo(brsSiteMO.getName(), brsSiteMO.getTenantID(), null, brsSiteMO.getDescription());
        siteModel.setLocalCpeType(localCpeNe.getProductName());
        siteModel.setReliability(siteParamConfigReader.getSiteReliability());
        siteModel.setSiteDescriptor(templateModel.getVpnType());
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

    private NbiVpn translateSite2DCVpn(Site2DCTemplateModel templateModel, String instanceId, String vpcId)
            throws ServiceException {

        SiteMO brsSiteMO = baseResourceDao.querySiteByName(templateModel.getSiteName());

        // Create Vpn
        NbiVpn vpn = new NbiVpn();
        vpn.setId(instanceId);
        vpn.setName(templateModel.getVpnName());
        vpn.setTenantId(brsSiteMO.getTenantID());
        vpn.setDescription(templateModel.getVpnDescription());
        vpn.setVpnDescriptor(templateModel.getVpnType());

        // Create Site Gateway
        NbiVpnGateway siteGateway = new NbiVpnGateway();
        siteGateway.setId(UuidUtils.createUuid());
        siteGateway.setName("SiteGateway_" + templateModel.getVpnName());
        siteGateway.setTenantId(brsSiteMO.getTenantID());
        siteGateway.setDescription(templateModel.getVpnDescription());
        siteGateway.setSiteId(brsSiteMO.getId());
        siteGateway.setVpnId(vpn.getId());
        siteGateway.setPorts(queryPorts(templateModel).get("portId"));
        siteGateway.setPortNames(queryPorts(templateModel).get("portName"));

        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());
        vpn.getVpnGateways().add(siteGateway);

        // Create Vpc Gateway
        NbiVpnGateway vpcGateway = new NbiVpnGateway();
        vpcGateway.setId(UuidUtils.createUuid());
        vpcGateway.setName("VpcGateway_" + templateModel.getVpnName());
        vpcGateway.setTenantId(brsSiteMO.getTenantID());
        vpcGateway.setDescription(templateModel.getVpnDescription());
        vpcGateway.setVpcId(vpcId);
        vpcGateway.setVpnId(vpn.getId());

        vpn.getVpnGateways().add(vpcGateway);

        // Create Vpn Connection
        NbiVpnConnection vpnConnection = new NbiVpnConnection();
        vpnConnection.setId(UuidUtils.createUuid());
        vpnConnection.setName("VpnConnection_" + templateModel.getVpnName());
        vpnConnection.setTenantId(brsSiteMO.getTenantID());
        vpnConnection.setDescription(templateModel.getVpnDescription());
        vpnConnection.setaEndVpnGatewayId(siteGateway.getId());
        vpnConnection.setzEndVpnGatewayId(vpcGateway.getId());
        vpnConnection.setVpnId(vpn.getId());
        vpnConnection.setVni(templateModel.getSiteVni().toString());
        vpnConnection.setDeployStatus("deploy");

        vpn.setVpnConnections(Arrays.asList(vpnConnection));

        return vpn;
    }

    private NbiVpn translateVoLteVpn(VoLteTemplateModel templateModel, String instanceId,
            VoLteBusinessModel businesssModel) throws ServiceException {

        // Create Vpn
        NbiVpn vpn = new NbiVpn();
        vpn.setId(instanceId);
        vpn.setName(templateModel.getVpnName());
        vpn.setDescription(templateModel.getVpnDescription());
        vpn.setVpnDescriptor(Site2DCVpnType.IPSEC.getName());

        vpn.setVpnGateways(new ArrayList<NbiVpnGateway>());

        // Create CoreVpc Gateway
        NbiVpnGateway coreVpcGateway = new NbiVpnGateway();
        coreVpcGateway.setId(UuidUtils.createUuid());
        coreVpcGateway.setName("CoreVpcGw_" + templateModel.getVpnName());
        coreVpcGateway.setDescription(templateModel.getVpnDescription());
        coreVpcGateway.setVpcId(businesssModel.getCoreVpcModel().getUuid());
        coreVpcGateway.setVpnId(vpn.getId());

        vpn.getVpnGateways().add(coreVpcGateway);

        // Create EdgeVpc1 Gateway
        NbiVpnGateway edgeVpc1Gateway = new NbiVpnGateway();
        edgeVpc1Gateway.setId(UuidUtils.createUuid());
        edgeVpc1Gateway.setName("EdgeVpc1Gw_" + templateModel.getVpnName());
        edgeVpc1Gateway.setDescription(templateModel.getVpnDescription());
        edgeVpc1Gateway.setVpcId(businesssModel.getEdgeVpc1Model().getUuid());
        edgeVpc1Gateway.setVpnId(vpn.getId());

        vpn.getVpnGateways().add(edgeVpc1Gateway);

        // Create EdgeVpc2 Gateway
        NbiVpnGateway edgeVpc2Gateway = new NbiVpnGateway();
        edgeVpc2Gateway.setId(UuidUtils.createUuid());
        edgeVpc2Gateway.setName("EdgeVpc2Gw_" + templateModel.getVpnName());
        edgeVpc2Gateway.setDescription(templateModel.getVpnDescription());
        edgeVpc2Gateway.setVpcId(businesssModel.getEdgeVpc2Model().getUuid());
        edgeVpc2Gateway.setVpnId(vpn.getId());

        vpn.getVpnGateways().add(edgeVpc2Gateway);

        vpn.setVpnConnections(new ArrayList<NbiVpnConnection>());

        // Create CoreVpcToEdgeVpc1 Connection
        NbiVpnConnection coreToEdge1VpnConnection = new NbiVpnConnection();
        coreToEdge1VpnConnection.setId(UuidUtils.createUuid());
        coreToEdge1VpnConnection.setName("CoreToEdge1_" + templateModel.getVpnName());
        coreToEdge1VpnConnection.setDescription(templateModel.getVpnDescription());
        coreToEdge1VpnConnection.setaEndVpnGatewayId(coreVpcGateway.getId());
        coreToEdge1VpnConnection.setzEndVpnGatewayId(edgeVpc1Gateway.getId());
        coreToEdge1VpnConnection.setVpnId(vpn.getId());
        coreToEdge1VpnConnection.setDeployStatus("deploy");

        vpn.getVpnConnections().add(coreToEdge1VpnConnection);

        // Create CoreVpcToEdgeVpc2 Connection
        NbiVpnConnection coreToEdge2VpnConnection = new NbiVpnConnection();
        coreToEdge2VpnConnection.setId(UuidUtils.createUuid());
        coreToEdge2VpnConnection.setName("CoreToEdge2_" + templateModel.getVpnName());
        coreToEdge2VpnConnection.setDescription(templateModel.getVpnDescription());
        coreToEdge2VpnConnection.setaEndVpnGatewayId(coreVpcGateway.getId());
        coreToEdge2VpnConnection.setzEndVpnGatewayId(edgeVpc2Gateway.getId());
        coreToEdge2VpnConnection.setVpnId(vpn.getId());
        coreToEdge2VpnConnection.setDeployStatus("deploy");

        vpn.getVpnConnections().add(coreToEdge2VpnConnection);

        // Create Vpn Connection
        NbiVpnConnection edge1ToEdge2VpnConnection = new NbiVpnConnection();
        edge1ToEdge2VpnConnection.setId(UuidUtils.createUuid());
        edge1ToEdge2VpnConnection.setName("Edge1ToEdge2_" + templateModel.getVpnName());
        edge1ToEdge2VpnConnection.setDescription(templateModel.getVpnDescription());
        edge1ToEdge2VpnConnection.setaEndVpnGatewayId(edgeVpc1Gateway.getId());
        edge1ToEdge2VpnConnection.setzEndVpnGatewayId(edgeVpc2Gateway.getId());
        edge1ToEdge2VpnConnection.setVpnId(vpn.getId());
        edge1ToEdge2VpnConnection.setDeployStatus("deploy");

        vpn.getVpnConnections().add(edge1ToEdge2VpnConnection);

        return vpn;
    }

    private ServiceChainPath translateServiceChainPath(Site2DCTemplateModel templateModel, String instanceId)
            throws ServiceException {

        String dcGwIp = templateModel.getDcGwIp();
        String dcFwIp = templateModel.getDcFwIp();
        String dcLbIp = templateModel.getDcLbIp();

        if(StringUtils.isEmpty(dcGwIp) || StringUtils.isEmpty(dcFwIp) || StringUtils.isEmpty(dcLbIp)) {
            LOGGER.info("ServiceChain Ne IpAddress is empty, Service Chain will not be created.");
            return null;
        }

        ServiceChainPath sfpPath = new ServiceChainPath();
        sfpPath.setUuid(instanceId);
        sfpPath.setName("Sfp_" + templateModel.getVpnName());
        sfpPath.setDescription(templateModel.getVpnDescription());
        NetworkElementMO gwNe = baseResourceDao.queryNeByIpAddress(dcGwIp);
        sfpPath.setScfNeId(gwNe.getId());

        NetworkElementMO fwNe = baseResourceDao.queryNeByIpAddress(dcFwIp);
        ServicePathHop fwPathHop = new ServicePathHop();
        fwPathHop.setHopNumber(1);
        fwPathHop.setSfiId(fwNe.getId());
        sfpPath.setServicePathHops(new ArrayList<ServicePathHop>());
        sfpPath.getServicePathHops().add(fwPathHop);

        NetworkElementMO lbNe = baseResourceDao.queryNeByIpAddress(dcLbIp);
        ServicePathHop lbPathHop = new ServicePathHop();
        lbPathHop.setHopNumber(2);
        lbPathHop.setSfiId(lbNe.getId());
        sfpPath.getServicePathHops().add(lbPathHop);

        return sfpPath;
    }

    private Vpc translateVpc(String vpcName, String vpcSubnetName, String vpcSubnetCidr, Integer vpcVni,
            String openStackName) throws ServiceException {
        Vpc vpc = new Vpc();

        vpc.setUuid(UuidUtils.createUuid());
        vpc.setName(vpcName);
        vpc.setOsControllerId(queryOsControllerId(openStackName));
        vpc.setSubnet(new SubNet());
        vpc.getSubnet().setUuid(UuidUtils.createUuid());
        vpc.getSubnet().setName(vpcSubnetName);
        vpc.getSubnet().setVpcId(vpc.getUuid());
        vpc.getSubnet().setCidr(vpcSubnetCidr);
        vpc.getSubnet().setVni(vpcVni);

        return vpc;
    }

    private String queryOsControllerId(String openStackName) throws ServiceException {

        Vim osVim = vimInvDao.queryVimByName(openStackName);
        if(null == osVim) {
            LOGGER.error("This openstack controller does not exist");
            throw new ServiceException("This openstack controller does not exist");
        }

        return osVim.getVimId();
    }

    private Map<String, List<String>> queryPorts(Site2DCTemplateModel templateModel) throws ServiceException {
        SiteMO brsSiteMO = baseResourceDao.querySiteByName(templateModel.getSiteName());
        NetworkElementMO localCpeNe = baseResourceDao.querySiteCpeByType(brsSiteMO.getId(), CpeRoleType.THIN_CPE);
        Map<String, List<String>> portInfo = new HashMap<>();

        List<String> ltpIdList = new ArrayList<>();
        List<String> ltpNameList = new ArrayList<>();

        Map<String, String> condition = new HashMap<String, String>();
        condition.put("meID", localCpeNe.getId());
        List<LogicalTernminationPointMO> localLtpMOList = ltpInvDao.query(condition);
        for(LogicalTernminationPointMO ltpMo : localLtpMOList) {
            ltpIdList.add(ltpMo.getId());
            ltpNameList.add(ltpMo.getName());
        }

        portInfo.put("portId", ltpIdList);
        portInfo.put("portName", ltpNameList);

        return portInfo;
    }

}
