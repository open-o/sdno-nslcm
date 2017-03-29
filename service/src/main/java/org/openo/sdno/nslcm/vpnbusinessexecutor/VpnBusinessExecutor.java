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

package org.openo.sdno.nslcm.vpnbusinessexecutor;

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.model.BusinessModel;
import org.openo.sdno.overlayvpn.model.v2.overlay.NbiVpn;

/**
 * Business Executor Interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-3-28
 */
public interface VpnBusinessExecutor {

    /**
     * Deploy OverlayVpn business.<br>
     * 
     * @param businessModel BusinessModel need to deploy
     * @throws ServiceException when deploy failed
     * @since SDNO 0.5
     */
    NbiVpn executeDeploy(BusinessModel businessModel) throws ServiceException;

    /**
     * Query OverlayVpn business.<br>
     * 
     * @param vpnUuid Vpn Uuid
     * @return OverlayVpn business queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    BusinessModel executeQuery(String vpnUuid) throws ServiceException;

    /**
     * UnDeploy OverlayVpn business.<br>
     * 
     * @param businessModel BusinessModel need to undeploy
     * @throws ServiceException when undeploy failed
     * @since SDNO 0.5
     */
    Map<String, String> executeUnDeploy(BusinessModel businessModel) throws ServiceException;
}
