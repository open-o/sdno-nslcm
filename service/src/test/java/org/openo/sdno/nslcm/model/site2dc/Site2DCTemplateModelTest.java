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

package org.openo.sdno.nslcm.model.site2dc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.sdno.nslcm.model.Site2DCTemplateModel;

public class Site2DCTemplateModelTest extends Site2DCTemplateModel {

    @Test
    public void setGetDcFwIpTest() {
        this.setDcFwIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(this.getDcFwIp()));
    }

    @Test
    public void setGetDcGwIpTest() {
        this.setDcGwIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(this.getDcGwIp()));
    }

    @Test
    public void setGetDcLbIpTest() {
        this.setDcLbIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(this.getDcLbIp()));
    }

    @Test
    public void setGetSiteCidrTest() {
        this.setSiteCidr("10.100.100.0/24");
        assertTrue("10.100.100.0/24".equals(this.getSiteCidr()));
    }

    @Test
    public void setGetSiteNameTest() {
        this.setSiteName("Site1");
        assertTrue("Site1".equals(this.getSiteName()));
    }

    @Test
    public void setGetSiteVniTest() {
        this.setSiteVni(13);
        assertTrue(13 == this.getSiteVni());
    }

    @Test
    public void setGetVpcNameTest() {
        this.setVpcName("Vpc1");
        assertTrue("Vpc1".equals(this.getVpcName()));
    }

    @Test
    public void setGetVpcSubnetCidrTest() {
        this.setVpcSubnetCidr("10.10.10.0/24");
        assertTrue("10.10.10.0/24".equals(this.getVpcSubnetCidr()));
    }

    @Test
    public void setGetSubnetVlanTest() {
        this.setSubnetVlan(24);
        assertTrue(24 == this.getSubnetVlan());
    }

    @Test
    public void setGetSubnetNameTest() {
        this.setVpcSubnetName("VpcSubnet1");
        assertTrue("VpcSubnet1".equals(this.getVpcSubnetName()));
    }

    @Test
    public void setGetVpcVniTest() {
        this.setVpcVni(24);
        assertTrue(24 == this.getVpcVni());
    }

    @Test
    public void setGetVpnDescriptionTest() {
        this.setVpnDescription("Test Vpn");
        assertTrue("Test Vpn".equals(this.getVpnDescription()));
    }

    @Test
    public void setGetVpnNameTest() {
        this.setVpnName("Vpn1");
        assertTrue("Vpn1".equals(this.getVpnName()));
    }

    @Test
    public void setGetVpnTypeTest() {
        this.setVpnType("IpSec");
        assertTrue("IpSec".equals(this.getVpnType()));
    }

}
