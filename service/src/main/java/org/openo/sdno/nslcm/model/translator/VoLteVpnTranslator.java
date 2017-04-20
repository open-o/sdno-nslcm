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
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.nslcm.model.SubnetModel;
import org.openo.sdno.nslcm.model.VoLteBusinessModel;
import org.openo.sdno.nslcm.model.VoLteTemplateModel;
import org.openo.sdno.nslcm.util.RecordProgress;
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

    private static final int SUBNET_PARAMETER_NUMBER = 3;

    private static final int SUBNET_PARAMETER_NAME = 0;

    private static final int SUBNET_PARAMETER_CIDR = 1;

    private static final int SUBNET_PARAMETER_VNI = 2;

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

        businessModel.setCoreVpcModel(translateVpc(templateModel.getCoreVpcName(), templateModel.getCoreSubnets(),
                templateModel.getCoreOpenStackName()));
        businessModel.setEdgeVpc1Model(translateVpc(templateModel.getEdge1VpcName(), templateModel.getEdge1Subnets(),
                templateModel.getEdge1OpenStackName()));
        businessModel.setEdgeVpc2Model(translateVpc(templateModel.getEdge2VpcName(), templateModel.getEdge2Subnets(),
                templateModel.getEdge2OpenStackName()));

        businessModel.setVpnModel(translateVpnModel(templateModel, instanceId, businessModel));

        int total = 5;
        total += businessModel.getCoreVpcModel().getSubNetList().size()
                + businessModel.getEdgeVpc1Model().getSubNetList().size()
                + businessModel.getEdgeVpc2Model().getSubNetList().size()
                + businessModel.getVpnModel().getVpnGateways().size()
                + businessModel.getVpnModel().getVpnConnections().size();

        RecordProgress.setTotalSteps(instanceId, total);

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
        List<NbiVpnGateway> coreNbiVpcGateways = translateVpnGateways(vpn.getVpnGateways(), templateModel,
                businesssModel.getCoreVpcModel().getSubNetList(), "CoreVpcGw_",
                businesssModel.getCoreVpcModel().getUuid(), vpn.getId());

        // Create EdgeVpc1 Gateway
        List<NbiVpnGateway> edge1NbiVpcGateways = translateVpnGateways(vpn.getVpnGateways(), templateModel,
                businesssModel.getEdgeVpc1Model().getSubNetList(), "EdgeVpc1Gw_",
                businesssModel.getEdgeVpc1Model().getUuid(), vpn.getId());

        // Create EdgeVpc2 Gateway
        List<NbiVpnGateway> edge2NbiVpcGateways = translateVpnGateways(vpn.getVpnGateways(), templateModel,
                businesssModel.getEdgeVpc2Model().getSubNetList(), "EdgeVpc2Gw_",
                businesssModel.getEdgeVpc2Model().getUuid(), vpn.getId());

        vpn.setVpnConnections(new ArrayList<NbiVpnConnection>());

        // Create CoreVpcToEdgeVpc1 Connection
        translateVpnConnections(vpn.getVpnConnections(), templateModel, coreNbiVpcGateways, edge1NbiVpcGateways,
                "CoreToEdge1_", vpn.getId());

        // Create CoreVpcToEdgeVpc2 Connection
        translateVpnConnections(vpn.getVpnConnections(), templateModel, coreNbiVpcGateways, edge2NbiVpcGateways,
                "CoreToEdge2_", vpn.getId());

        // Create EdgeVpc1ToEdgeVpc2 Connection
        translateVpnConnections(vpn.getVpnConnections(), templateModel, edge1NbiVpcGateways, edge2NbiVpcGateways,
                "Edge1ToEdge2_", vpn.getId());

        return vpn;
    }

    private Vpc translateVpc(String vpcName, String subnetInfo, String openStackName) throws ServiceException {
        Vpc vpc = new Vpc();

        vpc.setUuid(UuidUtils.createUuid());
        vpc.setName(vpcName);
        vpc.setOsControllerId(queryOsControllerId(openStackName));
        vpc.getSubNetList().clear();

        List<SubnetModel> subnetModelList = parseSubnets(subnetInfo);
        for(SubnetModel subnetModel : subnetModelList) {
            SubNet subNet = new SubNet();
            subNet.setUuid(UuidUtils.createUuid());
            subNet.setVpcId(vpc.getUuid());
            subNet.setName(subnetModel.getSubnetName());
            subNet.setCidr(subnetModel.getSubnetCidr());
            subNet.setVni(subnetModel.getVni());
            vpc.getSubNetList().add(subNet);
        }

        return vpc;
    }

    private String queryOsControllerId(String openStackName) throws ServiceException {

        Vim osVim = vimInvDao.queryVimByName(openStackName);
        if(null == osVim) {
            LOGGER.error("This openstack controller does not exist");
            throw new ParameterServiceException("This openstack controller does not exist");
        }

        return osVim.getVimId();
    }

    private List<SubnetModel> parseSubnets(String subnetInfo) throws ServiceException {
        List<SubnetModel> subnetModelList = new ArrayList<>();

        String[] subnetInfoList = subnetInfo.split("\\|");

        for(String tempSubnetInfo : subnetInfoList) {
            String[] subnetModelParas = tempSubnetInfo.split(",");
            if(SUBNET_PARAMETER_NUMBER != subnetModelParas.length) {
                LOGGER.error("The format of subnet info is wrong, each subnet info only can have 3 parameters");
                throw new ParameterServiceException(
                        "The format of subnet info is wrong, each subnet info only can have 3 parameters");
            }

            String subnetName = subnetModelParas[SUBNET_PARAMETER_NAME];
            String subnetCidr = subnetModelParas[SUBNET_PARAMETER_CIDR];
            String vni = subnetModelParas[SUBNET_PARAMETER_VNI];

            SubnetModel subnetModel = new SubnetModel();
            subnetModel.setSubnetName(subnetName);
            subnetModel.setSubnetCidr(subnetCidr);
            subnetModel.setVni(Integer.valueOf(vni));

            ValidationUtil.validateModel(subnetModel);
            subnetModelList.add(subnetModel);
        }

        return subnetModelList;
    }

    private List<NbiVpnGateway> translateVpnGateways(List<NbiVpnGateway> nbiVpnGatewayList,
            VoLteTemplateModel templateModel, List<SubNet> subNetList, String namePrefix, String vpcId, String vpnId) {
        int index = 1;

        List<NbiVpnGateway> nbiVpnGateways = new ArrayList<>();

        for(SubNet subNet : subNetList) {
            NbiVpnGateway nbiVpnGateway = new NbiVpnGateway();
            nbiVpnGateway.setId(UuidUtils.createUuid());
            nbiVpnGateway.setName(namePrefix + templateModel.getVpnName() + "_" + index);
            nbiVpnGateway.setDescription(templateModel.getVpnDescription());
            nbiVpnGateway.setVpcId(vpcId);
            nbiVpnGateway.setVpnId(vpnId);

            nbiVpnGateway.setSubnets(new ArrayList<String>());
            nbiVpnGateway.getSubnets().add(subNet.getUuid());

            nbiVpnGatewayList.add(nbiVpnGateway);
            nbiVpnGateways.add(nbiVpnGateway);
            index++;
        }

        return nbiVpnGateways;
    }

    private void translateVpnConnections(List<NbiVpnConnection> vpnConnectionList, VoLteTemplateModel templateModel,
            List<NbiVpnGateway> nbiVpcGateways1, List<NbiVpnGateway> nbiVpcGateways2, String namePrefix, String vpnId) {
        int index = 1;

        for(NbiVpnGateway nbiVpnGateway1 : nbiVpcGateways1) {
            for(NbiVpnGateway nbiVpnGateway2 : nbiVpcGateways2) {
                NbiVpnConnection nbiVpnConnection = new NbiVpnConnection();
                nbiVpnConnection.setId(UuidUtils.createUuid());
                nbiVpnConnection.setName(namePrefix + templateModel.getVpnName() + "_" + index);
                nbiVpnConnection.setDescription(templateModel.getVpnDescription());
                nbiVpnConnection.setaEndVpnGatewayId(nbiVpnGateway1.getId());
                nbiVpnConnection.setzEndVpnGatewayId(nbiVpnGateway2.getId());
                nbiVpnConnection.setVpnId(vpnId);
                nbiVpnConnection.setDeployStatus("deploy");

                vpnConnectionList.add(nbiVpnConnection);
                index++;
            }
        }
    }

}
