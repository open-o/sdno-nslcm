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
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;

public class OverlayVpnBusinessModelTest {

    private OverlayVpnBusinessModel overlayVpnBusinessModel = new OverlayVpnBusinessModel();

    @Test
    public void setGetServiceChainPathModelTest() {
        ServiceChainPath sfpModel = new ServiceChainPath();
        overlayVpnBusinessModel.setServiceChainPathModel(sfpModel);
        assertTrue(sfpModel.equals(overlayVpnBusinessModel.getServiceChainPathModel()));
    }

    @Test
    public void setGetSiteModelTest() {
        NbiSiteModel siteModel = new NbiSiteModel();
        overlayVpnBusinessModel.setSiteModel(siteModel);
        assertTrue(siteModel.equals(overlayVpnBusinessModel.getSiteModel()));
    }

    @Test
    public void setGetVpcModelTest() {
        Vpc vpcModel = new Vpc();
        overlayVpnBusinessModel.setVpcModel(vpcModel);
        assertTrue(vpcModel.equals(overlayVpnBusinessModel.getVpcModel()));
    }

    @Test
    public void setGetVpnModelTest() {
        NbiVpn vpnModel = new NbiVpn();
        overlayVpnBusinessModel.setVpnModel(vpnModel);
        assertTrue(vpnModel.equals(overlayVpnBusinessModel.getVpnModel()));
    }

}
