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

package org.openo.sdno.nslcm.dao.inf;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;

/**
 * Operate Base Resource Interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-6
 */
public interface IBaseResourceDao {

    /**
     * Query site by name.<br>
     * 
     * @param siteName Site name
     * @return SiteMO queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    SiteMO querySiteByName(String siteName) throws ServiceException;

    /**
     * Query site cpe by type.<br>
     * 
     * @param siteId Site Id
     * @param cpeRoleType Cpe role type
     * @return Cpe NetworkElement queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    NetworkElementMO querySiteCpeByType(String siteId, CpeRoleType cpeRoleType) throws ServiceException;

    /**
     * Query Network Element by IpAddress.<br>
     * 
     * @param ipAddress Ip Address
     * @return NetworkElementMO queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    NetworkElementMO queryNeByIpAddress(String ipAddress) throws ServiceException;

    /**
     * Query port NativeID by name.<br>
     * 
     * @param portName port name
     * @return port NativeID
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    String queryPortNativeID(String portName) throws ServiceException;
}
