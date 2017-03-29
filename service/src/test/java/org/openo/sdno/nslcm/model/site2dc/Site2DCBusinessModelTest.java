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
import org.openo.sdno.nslcm.model.Site2DCBusinessModel;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;

public class Site2DCBusinessModelTest extends Site2DCBusinessModel {

    @Test
    public void setGetServiceChainPathModelTest() {
        ServiceChainPath sfpModel = new ServiceChainPath();
        this.setServiceChainPathModel(sfpModel);
        assertTrue(sfpModel.equals(this.getServiceChainPathModel()));
    }

    @Test
    public void setGetSiteModelTest() {
        NbiSiteModel siteModel = new NbiSiteModel();
        this.setSiteModel(siteModel);
        assertTrue(siteModel.equals(this.getSiteModel()));
    }

    @Test
    public void setGetVpcModelTest() {
        Vpc vpcModel = new Vpc();
        this.setVpcModel(vpcModel);
        assertTrue(vpcModel.equals(this.getVpcModel()));
    }

    @Test
    public void setGetVpnModelTest() {
        NbiVpn vpnModel = new NbiVpn();
        this.setVpnModel(vpnModel);
        assertTrue(vpnModel.equals(this.getVpnModel()));
    }

}
