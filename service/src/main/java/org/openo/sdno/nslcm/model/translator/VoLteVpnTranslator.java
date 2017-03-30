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
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.nslcm.model.VoLteBusinessModel;
import org.openo.sdno.nslcm.model.VoLteTemplateModel;
import org.openo.sdno.overlayvpn.esr.invdao.VimInvDao;
import org.openo.sdno.overlayvpn.esr.model.Vim;
import org.openo.sdno.overlayvpn.model.servicemodel.SubNet;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnConnection;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpnGateway;
import org.openo.sdno.overlayvpn.model.v2.overlay.Site2DCVpnType;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Model translator class of VoLte OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
@Component("VoLteVpnTranslator")
public class VoLteVpnTranslator implements VpnTranslator {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoLteVpnTranslator.class);

    @Autowired
    private VimInvDao vimInvDao;

    @Override
    public BusinessModel translateVpnModel(Map<String, Object> templateParameter, String instanceId)
            throws ServiceException {

        // Create Vpn template model and validate
        VoLteTemplateModel vpnTemplateModel =
                JsonUtil.fromJson(JsonUtil.toJson(templateParameter), VoLteTemplateModel.class);

        // Validate model data
        ValidationUtil.validateModel(vpnTemplateModel);

        // Translate VoLte Model
        return translateVoLTEVpnModel(vpnTemplateModel, instanceId);
    }

    private VoLteBusinessModel translateVoLTEVpnModel(VoLteTemplateModel templateModel, String instanceId)
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

        businessModel.setVpnModel(translateVpnModel(templateModel, instanceId, businessModel));

        return businessModel;
    }

    private NbiVpn translateVpnModel(VoLteTemplateModel templateModel, String instanceId,
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

        // Create EdgeVpc1ToEdgeVpc2 Connection
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

}
