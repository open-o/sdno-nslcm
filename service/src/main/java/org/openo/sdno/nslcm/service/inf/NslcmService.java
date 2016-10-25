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

package org.openo.sdno.nslcm.service.inf;

import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IService;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;

/**
 * NSLCM service interface. <br>
 * 
 * @author
 * @version SDNO 0.5 June 16, 2016
 */
public interface NslcmService extends IService {

    /**
     * Query template information from catalog.<br>
     * 
     * @param nsdId ID of the template in catalog used to create the SDN-O service instance
     * @return The template information
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    Map<String, Object> queryServiceTemplate(String nsdId) throws ServiceException;

    /**
     * Delete overlay service instance.<br>
     * 
     * @param instanceId ID of the instance
     * @return The delete result
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    Map<String, String> deleteOverlay(String instanceId) throws ServiceException;

    /**
     * Create overlay service instance.<br>
     * 
     * @param siteToDcNbiMo The site2DcNbi model
     * @param instanceId ID of the instance
     * @return The create result
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    Map<String, String> createOverlay(SiteToDcNbi siteToDcNbiMo, String instanceId) throws ServiceException;

    /**
     * Query Vpn.<br>
     * 
     * @param instanceId ID of the instance
     * @return The NsInstance Query Response
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    NsInstanceQueryResponse queryVpn(String instanceId) throws ServiceException;

    /**
     * Create underlay service instance.<br>
     * 
     * @param vpnVo The vpnVo model
     * @param instanceId ID of the instance
     * @return The create result
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    Map<String, String> createUnderlay(VpnVo vpnVo, String instanceId) throws ServiceException;

    /**
     * Delete underlay service instance.<br>
     * 
     * @param instanceId ID of the instance
     * @param nsInstantiationInfo The nsInstantiation Info
     * @return The delete result
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    Map<String, String> deleteUnderlay(String instanceId, List<ServiceParameter> serviceParameterList)
            throws ServiceException;
}
