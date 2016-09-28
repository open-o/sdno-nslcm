/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.sdno.nslcm.sbi.inf;

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IService;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;

/**
 * DC gateway controller south branch interface.<br>
 * 
 * @author
 * @version SDNO 0.5 Sep 8, 2016
 */
public interface UnderlaySbiService extends IService {

    /**
     * Delete underlay.<br>
     * 
     * @param vpnUuid The vpn uuid
     * @param serviceType The service type
     * @return The delete result
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    Map<String, String> deleteUnderlay(String vpnUuid, String serviceType) throws ServiceException;

    /**
     * Create underlay.<br>
     * 
     * @param vpnVo The vpnVo object
     * @return The create result
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    Map<String, String> createUnderlay(VpnVo vpnVo) throws ServiceException;
}