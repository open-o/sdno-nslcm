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

package org.openo.sdno.nslcm.util;

/**
 * Const Url of Sbi Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-25
 */
public class AdapterUrlConst {

    private AdapterUrlConst() {
    }

    /**
     * URL of Catalog Service
     */
    public static final String CATALOG_ADAPTER_URL = "/openoapi/catalog/v1/servicetemplates";

    /**
     * URL of L3Vpn Service
     */
    public static final String L3VPN_ADAPTER_URL = "/openoapi/sdnol3vpn/v1/l3vpns";

    /**
     * URL of L2Vpn Service
     */
    public static final String L2VPN_ADAPTER_URL = "/openoapi/sdnol2vpn/v1/l2vpns";

    /**
     * URL of OverlayVpn Service
     */
    public static final String VPN_ADAPTER_URL = "/openoapi/sdnooverlayvpn/v1/vpns";

    /**
     * URL of OverlayVpn Gateway
     */
    public static final String VPN_GATEWAY_ADAPTER_URL = "/openoapi/sdnooverlayvpn/v1/vpn-gateways";

    /**
     * URL of OverlayVpn Connection
     */
    public static final String VPN_CONNECTION_ADAPTER_URL = "/openoapi/sdnooverlayvpn/v1/vpn-connections";

    /**
     * URL of Site Service
     */
    public static final String SITE_ADAPTER_URL = "/openoapi/sdnolocalsite/v1/sites";

    /**
     * URL of Vlan Service
     */
    public static final String VLAN_ADAPTER_URL = "/openoapi/sdnolocalsite/v1/vlans";

    /**
     * URL of Subnet Service
     */
    public static final String SUBNET_ADAPTER_URL = "/openoapi/sdnolocalsite/v1/subnets";

    /**
     * URL of LocalCpe Service
     */
    public static final String LOCALCPE_ADAPTER_URL = "/openoapi/sdnolocalsite/v1/local-cpes";

    /**
     * URL of CloudCpe Service
     */
    public static final String CLOUDCPE_ADAPTER_URL = "/openoapi/sdnolocalsite/v1/cloud-cpes";

    /**
     * URL of ServiceChain Service
     */
    public static final String SERVICECHAIN_ADAPTER_URL = "/openoapi/sdnoservicechain/v1/paths";

    /**
     * URL of Vpc Service
     */
    public static final String VPC_ADAPTER_URL = "/openoapi/sdnovpc/v1/vpcs";

    /**
     * URL of Vpc Subnet Service
     */
    public static final String VPCSUBNET_ADAPTER_URL = "/openoapi/sdnovpc/v1/subnets";

}
