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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.sbi.vpc.VpcSbiService;
import org.openo.sdno.nslcm.sbi.vpc.VpcSubnetSbiService;
import org.openo.sdno.overlayvpn.model.servicemodel.SubNet;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Executor class of vpc Business.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
@Component
public class VpcBusinessExecutor {

    @Autowired
    private VpcSbiService vpcSbiService;

    @Autowired
    private VpcSubnetSbiService vpcSubnetSbiService;

    /**
     * Deploy Vpc business.<br>
     * 
     * @param vpcModel Vpc need to deploy
     * @return Vpc deployed
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    public Vpc executeDeploy(Vpc vpcModel) throws ServiceException {
        vpcSbiService.createVpc(vpcModel);
        vpcSubnetSbiService.createVpcSubnet(vpcModel.getSubnet());
        return vpcModel;
    }

    /**
     * UnDeploy Vpc business.<br>
     * 
     * @param vpcModel Vpc need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    public void executeUnDeploy(Vpc vpcModel) throws ServiceException {
        vpcSubnetSbiService.deleteVpcSubnet(vpcModel.getSubnet().getUuid());
        vpcSbiService.deleteVpc(vpcModel.getUuid());
    }

    /**
     * Query Vpc.<br>
     * 
     * @param vpcUuid Vpc Uuid
     * @return Vpc queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public Vpc executeQuery(String vpcUuid) throws ServiceException {
        Vpc vpc = vpcSbiService.queryVpc(vpcUuid);
        SubNet vpcSubnet = vpcSubnetSbiService.queryVpcSubnet(vpcUuid);
        vpc.setSubnet(vpcSubnet);
        return vpc;
    }

}
