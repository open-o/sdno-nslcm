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

import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.openo.sdno.overlayvpn.model.servicemodel.Vpc;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;

/**
 * Business model of OverlayVpn.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-7
 */
public class OverlayVpnBusinessModel {

    private NbiSiteModel siteModel;

    private NbiVpn vpnModel;

    private ServiceChainPath serviceChainPathModel;

    private Vpc vpcModel;

    public NbiSiteModel getSiteModel() {
        return siteModel;
    }

    public void setSiteModel(NbiSiteModel siteModel) {
        this.siteModel = siteModel;
    }

    public NbiVpn getVpnModel() {
        return vpnModel;
    }

    public void setVpnModel(NbiVpn vpnModel) {
        this.vpnModel = vpnModel;
    }

    public ServiceChainPath getServiceChainPathModel() {
        return serviceChainPathModel;
    }

    public void setServiceChainPathModel(ServiceChainPath serviceChainPathModel) {
        this.serviceChainPathModel = serviceChainPathModel;
    }

    public Vpc getVpcModel() {
        return vpcModel;
    }

    public void setVpcModel(Vpc vpcModel) {
        this.vpcModel = vpcModel;
    }

}
