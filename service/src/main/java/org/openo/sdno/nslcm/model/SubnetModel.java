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
 * Model class of Subnet Template.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-4-12
 */
public class SubnetModel {

    /**
     * subnet name
     */
    @AString(require = true)
    private String subnetName;

    /**
     * subnet cidr
     */
    @AIpMask(require = true)
    private String subnetCidr;

    /**
     * vni
     */
    @AInt(min = 2, max = 16777215, require = true)
    private Integer vni;

    public String getSubnetName() {
        return subnetName;
    }

    public void setSubnetName(String subnetName) {
        this.subnetName = subnetName;
    }

    public String getSubnetCidr() {
        return subnetCidr;
    }

    public void setSubnetCidr(String subnetCidr) {
        this.subnetCidr = subnetCidr;
    }

    public Integer getVni() {
        return vni;
    }

    public void setVni(Integer vni) {
        this.vni = vni;
    }

}
