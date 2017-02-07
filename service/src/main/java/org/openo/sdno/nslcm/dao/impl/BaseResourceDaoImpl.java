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

package org.openo.sdno.nslcm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.dao.inf.IBaseResourceDao;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * Implementation class of Base Resource Dao.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-2-6
 */
@Repository
public class BaseResourceDaoImpl implements IBaseResourceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseResourceDaoImpl.class);

    @Autowired
    private SiteInvDao siteInvDao;

    @Autowired
    private NetworkElementInvDao neInvDao;

    @Autowired
    private LogicalTernminationPointInvDao ltpInvDao;

    @Override
    public SiteMO querySiteByName(String siteName) throws ServiceException {
        Map<String, String> filterMap = new HashMap<String, String>();
        filterMap.put("name", siteName);

        List<SiteMO> siteList = siteInvDao.query(filterMap);
        if(CollectionUtils.isEmpty(siteList)) {
            LOGGER.error("No sites queried out");
            throw new ServiceException("No sites queried out");
        }

        return siteList.get(0);
    }

    @Override
    public NetworkElementMO querySiteCpeByType(String siteId, CpeRoleType cpeRoleType) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("siteID", siteId);
        conditionMap.put("neRole", cpeRoleType.getName());

        List<NetworkElementMO> neList = neInvDao.query(conditionMap);
        if(CollectionUtils.isEmpty(neList)) {
            LOGGER.error("No cpes queried out");
            throw new ServiceException("No cpes queried out");
        }

        return neList.get(0);
    }

    @Override
    public NetworkElementMO queryNeByIpAddress(String ipAddress) throws ServiceException {

        Map<String, String> condition = new HashMap<String, String>();
        condition.put("ipAddress", ipAddress);
        List<NetworkElementMO> neList = neInvDao.query(condition);
        if(CollectionUtils.isEmpty(neList)) {
            LOGGER.error("No network element queried out");
            throw new ServiceException("No network element queried out");
        }

        return neList.get(0);
    }

    @Override
    public String queryPortNativeID(String portName) throws ServiceException {
        Map<String, String> condition = new HashMap<String, String>();
        condition.put("name", portName);

        List<LogicalTernminationPointMO> ltpMOList = ltpInvDao.query(condition);
        if(CollectionUtils.isEmpty(ltpMOList)) {
            LOGGER.error("No ltps queried out");
            throw new ServiceException("No ltps queried out");
        }

        return ltpMOList.get(0).getNativeID();
    }

}
