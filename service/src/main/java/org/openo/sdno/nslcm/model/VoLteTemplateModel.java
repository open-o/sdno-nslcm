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

import org.openo.sdno.overlayvpn.verify.annotation.AInt;
import org.openo.sdno.overlayvpn.verify.annotation.AIpMask;
import org.openo.sdno.overlayvpn.verify.annotation.AString;

/**
 * Model class of Site OverlayVpn Template.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
public class VoLteTemplateModel extends TemplateModel {

    /**
     * Core vpc openstack name
     */
    @AString(require = true)
    private String coreVpcOpenStackName;

    /**
     * Core vpc name
     */
    @AString(require = true)
    private String coreVpcName;

    /**
     * Core vpc subnet name
     */
    @AString(require = true)
    private String coreVpcSubnetName;

    /**
     * Core vpc subnet cidr
     */
    @AIpMask(require = true)
    private String coreVpcSubnetCidr;

    /**
     * core vpc vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer coreVpcVni;

    /**
     * Core vpc openstack name
     */
    @AString(require = true)
    private String edgeVpc1OpenStackName;

    /**
     * Edge vpc1 name
     */
    @AString(require = true)
    private String edgeVpc1Name;

    /**
     * Edge vpc1 subnet name
     */
    @AString(require = true)
    private String edgeVpc1SubnetName;

    /**
     * Edge vpc1 subnet cidr
     */
    @AIpMask(require = true)
    private String edgeVpc1SubnetCidr;

    /**
     * Edge vpc1 vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer edgeVpc1Vni;

    /**
     * Edge vpc2 openstack name
     */
    @AString(require = true)
    private String edgeVpc2OpenStackName;

    /**
     * Edge vpc2 name
     */
    @AString(require = true)
    private String edgeVpc2Name;

    /**
     * Edge vpc2 subnet name
     */
    @AString(require = true)
    private String edgeVpc2SubnetName;

    /**
     * Edge vpc2 subnet cidr
     */
    @AIpMask(require = true)
    private String edgeVpc2SubnetCidr;

    /**
     * Edge vpc2 vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer edgeVpc2Vni;

    public String getCoreVpcOpenStackName() {
        return coreVpcOpenStackName;
    }

    public void setCoreVpcOpenStackName(String coreVpcOpenStackName) {
        this.coreVpcOpenStackName = coreVpcOpenStackName;
    }

    public String getCoreVpcName() {
        return coreVpcName;
    }

    public void setCoreVpcName(String coreVpcName) {
        this.coreVpcName = coreVpcName;
    }

    public String getCoreVpcSubnetName() {
        return coreVpcSubnetName;
    }

    public void setCoreVpcSubnetName(String coreVpcSubnetName) {
        this.coreVpcSubnetName = coreVpcSubnetName;
    }

    public String getCoreVpcSubnetCidr() {
        return coreVpcSubnetCidr;
    }

    public void setCoreVpcSubnetCidr(String coreVpcSubnetCidr) {
        this.coreVpcSubnetCidr = coreVpcSubnetCidr;
    }

    public Integer getCoreVpcVni() {
        return coreVpcVni;
    }

    public void setCoreVpcVni(Integer coreVpcVni) {
        this.coreVpcVni = coreVpcVni;
    }

    public String getEdgeVpc1OpenStackName() {
        return edgeVpc1OpenStackName;
    }

    public void setEdgeVpc1OpenStackName(String edgeVpc1OpenStackName) {
        this.edgeVpc1OpenStackName = edgeVpc1OpenStackName;
    }

    public String getEdgeVpc1Name() {
        return edgeVpc1Name;
    }

    public void setEdgeVpc1Name(String edgeVpc1Name) {
        this.edgeVpc1Name = edgeVpc1Name;
    }

    public String getEdgeVpc1SubnetName() {
        return edgeVpc1SubnetName;
    }

    public void setEdgeVpc1SubnetName(String edgeVpc1SubnetName) {
        this.edgeVpc1SubnetName = edgeVpc1SubnetName;
    }

    public String getEdgeVpc1SubnetCidr() {
        return edgeVpc1SubnetCidr;
    }

    public void setEdgeVpc1SubnetCidr(String edgeVpc1SubnetCidr) {
        this.edgeVpc1SubnetCidr = edgeVpc1SubnetCidr;
    }

    public Integer getEdgeVpc1Vni() {
        return edgeVpc1Vni;
    }

    public void setEdgeVpc1Vni(Integer edgeVpc1Vni) {
        this.edgeVpc1Vni = edgeVpc1Vni;
    }

    public String getEdgeVpc2OpenStackName() {
        return edgeVpc2OpenStackName;
    }

    public void setEdgeVpc2OpenStackName(String edgeVpc2OpenStackName) {
        this.edgeVpc2OpenStackName = edgeVpc2OpenStackName;
    }

    public String getEdgeVpc2Name() {
        return edgeVpc2Name;
    }

    public void setEdgeVpc2Name(String edgeVpc2Name) {
        this.edgeVpc2Name = edgeVpc2Name;
    }

    public String getEdgeVpc2SubnetName() {
        return edgeVpc2SubnetName;
    }

    public void setEdgeVpc2SubnetName(String edgeVpc2SubnetName) {
        this.edgeVpc2SubnetName = edgeVpc2SubnetName;
    }

    public String getEdgeVpc2SubnetCidr() {
        return edgeVpc2SubnetCidr;
    }

    public void setEdgeVpc2SubnetCidr(String edgeVpc2SubnetCidr) {
        this.edgeVpc2SubnetCidr = edgeVpc2SubnetCidr;
    }

    public Integer getEdgeVpc2Vni() {
        return edgeVpc2Vni;
    }

    public void setEdgeVpc2Vni(Integer edgeVpc2Vni) {
        this.edgeVpc2Vni = edgeVpc2Vni;
    }

}
