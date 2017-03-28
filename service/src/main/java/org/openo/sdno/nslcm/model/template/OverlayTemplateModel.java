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

package org.openo.sdno.nslcm.model.template;

import org.openo.sdno.overlayvpn.verify.annotation.AInt;
import org.openo.sdno.overlayvpn.verify.annotation.AIp;
import org.openo.sdno.overlayvpn.verify.annotation.AIpMask;
import org.openo.sdno.overlayvpn.verify.annotation.AString;

/**
 * Model class of OverlayVpn Template.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
public class OverlayTemplateModel {

    /**
     * OverlayVpn name
     */
    @AString(require = true)
    private String vpnName;

    /**
     * OverlayVpn description
     */
    @AString(require = true)
    private String vpnDescription;

    /**
     * Vpn Type, only support "IpSec", "VxLAN" and "MPLS_VPN"
     */
    @AString(require = true, scope = "IpSec,VxLAN,MPLS_VPN")
    private String vpnType;

    /**
     * Site name
     */
    @AString(require = true)
    private String siteName;

    /**
     * Site cidr
     */
    @AIpMask(require = true)
    private String siteCidr;

    /**
     * Subnet vlan
     */
    @AInt(min = 2, max = 4095, require = true)
    private Integer subnetVlan;

    /**
     * Site vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer siteVni;

    /**
     * Vpc name
     */
    @AString(require = true)
    private String vpcName;

    /**
     * Vpc subnet name
     */
    @AString(require = true)
    private String vpcSubnetName;

    /**
     * Vpc Subnet cidr
     */
    @AIpMask(require = true)
    private String vpcSubnetCidr;

    /**
     * Vpc vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer vpcVni;

    /**
     * DC Gateway IpAddress
     */
    @AIp
    private String dcGwIp;

    /**
     * DC FireWall IpAddress
     */
    @AIp
    private String dcFwIp;

    /**
     * DC LoadBalance IpAddress
     */
    @AIp
    private String dcLbIp;

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getVpnDescription() {
        return vpnDescription;
    }

    public void setVpnDescription(String vpnDescription) {
        this.vpnDescription = vpnDescription;
    }

    public String getVpnType() {
        return vpnType;
    }

    public void setVpnType(String vpnType) {
        this.vpnType = vpnType;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCidr() {
        return siteCidr;
    }

    public void setSiteCidr(String siteCidr) {
        this.siteCidr = siteCidr;
    }

    public Integer getSubnetVlan() {
        return subnetVlan;
    }

    public void setSubnetVlan(Integer subnetVlan) {
        this.subnetVlan = subnetVlan;
    }

    public Integer getSiteVni() {
        return siteVni;
    }

    public void setSiteVni(Integer siteVni) {
        this.siteVni = siteVni;
    }

    public String getVpcName() {
        return vpcName;
    }

    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
    }

    public String getVpcSubnetName() {
        return vpcSubnetName;
    }

    public void setVpcSubnetName(String vpcSubnetName) {
        this.vpcSubnetName = vpcSubnetName;
    }

    public String getVpcSubnetCidr() {
        return vpcSubnetCidr;
    }

    public void setVpcSubnetCidr(String vpcSubnetCidr) {
        this.vpcSubnetCidr = vpcSubnetCidr;
    }

    public Integer getVpcVni() {
        return vpcVni;
    }

    public void setVpcVni(Integer vpcVni) {
        this.vpcVni = vpcVni;
    }

    public String getDcGwIp() {
        return dcGwIp;
    }

    public void setDcGwIp(String dcGwIp) {
        this.dcGwIp = dcGwIp;
    }

    public String getDcFwIp() {
        return dcFwIp;
    }

    public void setDcFwIp(String dcFwIp) {
        this.dcFwIp = dcFwIp;
    }

    public String getDcLbIp() {
        return dcLbIp;
    }

    public void setDcLbIp(String dcLbIp) {
        this.dcLbIp = dcLbIp;
    }
}
