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

package org.openo.sdno.nslcm.model;

import org.openo.sdno.overlayvpn.verify.annotation.AString;

/**
 * Model class of Volte Template.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
public class VoLteTemplateModel extends TemplateModel {

    /**
     * Core openstack name
     */
    @AString(require = true)
    private String coreOpenStackName;

    /**
     * Core vpc name
     */
    @AString(require = true)
    private String coreVpcName;

    /**
     * Core subnet information, the format is
     * "SubnetName,SubnetCidr,Vni|SubnetName,SubnetCidr,Vni|..."
     */
    @AString(require = true)
    private String coreSubnets;

    /**
     * Edge1 openstack name
     */
    @AString(require = true)
    private String edge1OpenStackName;

    /**
     * Edge1 vpc name
     */
    @AString(require = true)
    private String edge1VpcName;

    /**
     * Edge1 subnet information, the format is
     * "SubnetName,SubnetCidr,Vni|SubnetName,SubnetCidr,Vni|..."
     */
    @AString(require = true)
    private String edge1Subnets;

    /**
     * Edge2 openstack name
     */
    @AString(require = true)
    private String edge2OpenStackName;

    /**
     * Edge2 vpc name
     */
    @AString(require = true)
    private String edge2VpcName;

    /**
     * Edge2 subnet information, the format is
     * "SubnetName,SubnetCidr,Vni|SubnetName,SubnetCidr,Vni|..."
     */
    @AString(require = true)
    private String edge2Subnets;

    public String getCoreOpenStackName() {
        return coreOpenStackName;
    }

    public void setCoreOpenStackName(String coreOpenStackName) {
        this.coreOpenStackName = coreOpenStackName;
    }

    public String getCoreVpcName() {
        return coreVpcName;
    }

    public void setCoreVpcName(String coreVpcName) {
        this.coreVpcName = coreVpcName;
    }

    public String getCoreSubnets() {
        return coreSubnets;
    }

    public void setCoreSubnets(String coreSubnets) {
        this.coreSubnets = coreSubnets;
    }

    public String getEdge1OpenStackName() {
        return edge1OpenStackName;
    }

    public void setEdge1OpenStackName(String edge1OpenStackName) {
        this.edge1OpenStackName = edge1OpenStackName;
    }

    public String getEdge1VpcName() {
        return edge1VpcName;
    }

    public void setEdge1VpcName(String edge1VpcName) {
        this.edge1VpcName = edge1VpcName;
    }

    public String getEdge1Subnets() {
        return edge1Subnets;
    }

    public void setEdge1Subnets(String edge1Subnets) {
        this.edge1Subnets = edge1Subnets;
    }

    public String getEdge2OpenStackName() {
        return edge2OpenStackName;
    }

    public void setEdge2OpenStackName(String edge2OpenStackName) {
        this.edge2OpenStackName = edge2OpenStackName;
    }

    public String getEdge2VpcName() {
        return edge2VpcName;
    }

    public void setEdge2VpcName(String edge2VpcName) {
        this.edge2VpcName = edge2VpcName;
    }

    public String getEdge2Subnets() {
        return edge2Subnets;
    }

    public void setEdge2Subnets(String edge2Subnets) {
        this.edge2Subnets = edge2Subnets;
    }

}
