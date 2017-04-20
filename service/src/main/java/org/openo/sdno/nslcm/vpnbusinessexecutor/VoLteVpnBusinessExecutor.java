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

package org.openo.sdno.nslcm.vpnbusinessexecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.nslcm.model.VoLteBusinessModel;
import org.openo.sdno.nslcm.serviceexecutor.OverlayVpnBusinessExecutor;
import org.openo.sdno.nslcm.serviceexecutor.VpcBusinessExecutor;
import org.openo.sdno.nslcm.util.RecordProgress;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business executor of VoLTE OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component("VoLteVpnBusinessExecutor")
public class VoLteVpnBusinessExecutor implements VpnBusinessExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoLteVpnBusinessExecutor.class);

    @Autowired
    private VpcBusinessExecutor vpcBusinessExecutor;

    @Autowired
    private OverlayVpnBusinessExecutor vpnBusinessExecutor;

    @Override
    public NbiVpn executeDeploy(BusinessModel businessModel) throws ServiceException {

        if(!(businessModel instanceof VoLteBusinessModel)) {
            LOGGER.error("only VoLteBusinessModel object can be processed here");
            throw new ServiceException("only VoLteBusinessModel object can be processed here");
        }

        VoLteBusinessModel voLteBusinessModel = (VoLteBusinessModel)businessModel;
        String instanceId = voLteBusinessModel.getVpnModel().getId();

        if(null != voLteBusinessModel.getCoreVpcModel()) {
            vpcBusinessExecutor.executeDeploy(instanceId, voLteBusinessModel.getCoreVpcModel());
        }

        if(null != voLteBusinessModel.getEdgeVpc1Model()) {
            vpcBusinessExecutor.executeDeploy(instanceId, voLteBusinessModel.getEdgeVpc1Model());
        }

        if(null != voLteBusinessModel.getEdgeVpc2Model()) {
            vpcBusinessExecutor.executeDeploy(instanceId, voLteBusinessModel.getEdgeVpc2Model());
        }

        if(null != voLteBusinessModel.getVpnModel()) {
            return vpnBusinessExecutor.executeDeploy(instanceId, voLteBusinessModel.getVpnModel());
        }

        return null;
    }

    @Override
    public Map<String, String> executeUnDeploy(BusinessModel businessModel) throws ServiceException {

        if(!(businessModel instanceof VoLteBusinessModel)) {
            LOGGER.error("only VoLteBusinessModel object can be processed here");
            throw new ServiceException("only VoLteBusinessModel object can be processed here");
        }

        VoLteBusinessModel voLteBusinessModel = (VoLteBusinessModel)businessModel;
        String instanceId = voLteBusinessModel.getVpnModel().getId();

        if(null != voLteBusinessModel.getVpnModel()) {
            vpnBusinessExecutor.executeUnDeploy(instanceId, voLteBusinessModel.getVpnModel());
        }

        if(null != voLteBusinessModel.getCoreVpcModel()) {
            vpcBusinessExecutor.executeUnDeploy(instanceId, voLteBusinessModel.getCoreVpcModel());
        }

        if(null != voLteBusinessModel.getEdgeVpc1Model()) {
            vpcBusinessExecutor.executeUnDeploy(instanceId, voLteBusinessModel.getEdgeVpc1Model());
        }

        if(null != voLteBusinessModel.getEdgeVpc2Model()) {
            vpcBusinessExecutor.executeUnDeploy(instanceId, voLteBusinessModel.getEdgeVpc2Model());
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("errorCode", voLteBusinessModel.getVpnModel().getId());

        return resultMap;
    }

    @Override
    public VoLteBusinessModel executeQuery(String vpnUuid) throws ServiceException {

        VoLteBusinessModel businessModel = new VoLteBusinessModel();

        NbiVpn vpn = vpnBusinessExecutor.executeQuery(vpnUuid);
        businessModel.setVpnModel(vpn);

        Set<String> vpcUuidList = vpn.getVpcList();
        if(CollectionUtils.isEmpty(vpcUuidList)) {
            LOGGER.error("No vpc related to vpn");
            throw new ServiceException("No vpc related to vpn");
        }

        String coreVpcUuid = vpcUuidList.iterator().next();
        Vpc coreVpcModel = vpcBusinessExecutor.executeQuery(coreVpcUuid);
        businessModel.setCoreVpcModel(coreVpcModel);

        String edgeVpc1Uuid = vpcUuidList.iterator().next();
        Vpc edgeVpc1Model = vpcBusinessExecutor.executeQuery(edgeVpc1Uuid);
        businessModel.setEdgeVpc1Model(edgeVpc1Model);

        String edgeVpc2Uuid = vpcUuidList.iterator().next();
        Vpc edgeVpc2Model = vpcBusinessExecutor.executeQuery(edgeVpc2Uuid);
        businessModel.setEdgeVpc2Model(edgeVpc2Model);

        int total = 5;
        total += businessModel.getCoreVpcModel().getSubNetList().size()
                + businessModel.getEdgeVpc1Model().getSubNetList().size()
                + businessModel.getEdgeVpc2Model().getSubNetList().size()
                + businessModel.getVpnModel().getVpnGateways().size()
                + businessModel.getVpnModel().getVpnConnections().size();

        RecordProgress.setTotalSteps(vpnUuid, total);

        return businessModel;
    }

}
