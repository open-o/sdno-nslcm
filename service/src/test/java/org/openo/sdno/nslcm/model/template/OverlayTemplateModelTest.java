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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OverlayTemplateModelTest {

    private OverlayTemplateModel overlayTemplateModel = new OverlayTemplateModel();

    @Test
    public void setGetDcFwIpTest() {
        overlayTemplateModel.setDcFwIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(overlayTemplateModel.getDcFwIp()));
    }

    @Test
    public void setGetDcGwIpTest() {
        overlayTemplateModel.setDcGwIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(overlayTemplateModel.getDcGwIp()));
    }

    @Test
    public void setGetDcLbIpTest() {
        overlayTemplateModel.setDcLbIp("192.168.1.0");
        assertTrue("192.168.1.0".equals(overlayTemplateModel.getDcLbIp()));
    }

    @Test
    public void setGetSiteCidrTest() {
        overlayTemplateModel.setSiteCidr("10.100.100.0/24");
        assertTrue("10.100.100.0/24".equals(overlayTemplateModel.getSiteCidr()));
    }

    @Test
    public void setGetSiteNameTest() {
        overlayTemplateModel.setSiteName("Site1");
        assertTrue("Site1".equals(overlayTemplateModel.getSiteName()));
    }

    @Test
    public void setGetSiteVniTest() {
        overlayTemplateModel.setSiteVni(13);
        assertTrue(13 == overlayTemplateModel.getSiteVni());
    }

    @Test
    public void setGetVpcNameTest() {
        overlayTemplateModel.setVpcName("Vpc1");
        assertTrue("Vpc1".equals(overlayTemplateModel.getVpcName()));
    }

    @Test
    public void setGetVpcSubnetCidrTest() {
        overlayTemplateModel.setVpcSubnetCidr("10.10.10.0/24");
        assertTrue("10.10.10.0/24".equals(overlayTemplateModel.getVpcSubnetCidr()));
    }

    @Test
    public void setGetSubnetVlanTest() {
        overlayTemplateModel.setSubnetVlan(24);
        assertTrue(24 == overlayTemplateModel.getSubnetVlan());
    }

    @Test
    public void setGetSubnetNameTest() {
        overlayTemplateModel.setVpcSubnetName("VpcSubnet1");
        assertTrue("VpcSubnet1".equals(overlayTemplateModel.getVpcSubnetName()));
    }

    @Test
    public void setGetVpcVniTest() {
        overlayTemplateModel.setVpcVni(24);
        assertTrue(24 == overlayTemplateModel.getVpcVni());
    }

    @Test
    public void setGetVpnDescriptionTest() {
        overlayTemplateModel.setVpnDescription("Test Vpn");
        assertTrue("Test Vpn".equals(overlayTemplateModel.getVpnDescription()));
    }

    @Test
    public void setGetVpnNameTest() {
        overlayTemplateModel.setVpnName("Vpn1");
        assertTrue("Vpn1".equals(overlayTemplateModel.getVpnName()));
    }

    @Test
    public void setGetVpnTypeTest() {
        overlayTemplateModel.setVpnType("IpSec");
        assertTrue("IpSec".equals(overlayTemplateModel.getVpnType()));
    }

}
