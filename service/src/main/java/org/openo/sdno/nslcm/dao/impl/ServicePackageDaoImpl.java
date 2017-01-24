/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.nslcm.dao.inf.IServicePackageDao;
import org.openo.sdno.nslcm.dao.multi.DatabaseSessionHandler;
import org.openo.sdno.nslcm.mapper.InvServicePackageMapper;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.exception.ApplicationException;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Database operation class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
@Repository
public class ServicePackageDaoImpl implements IServicePackageDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicePackageDaoImpl.class);

    /**
     * Session handler.
     */
    private DatabaseSessionHandler dbSessionHandler;

    /**
     * @return Returns the dbSessionHandler.
     */
    public DatabaseSessionHandler getDbSessionHandler() {
        return dbSessionHandler;
    }

    /**
     * @param dbSessionHandler The dbSessionHandler to set.
     */
    public void setDbSessionHandler(DatabaseSessionHandler dbSessionHandler) {
        this.dbSessionHandler = dbSessionHandler;
    }

    /**
     * Insert package mapping data.<br>
     * 
     * @param packageMapping service package mapping data
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    @Override
    public void insert(ServicePackageModel packageMapping) throws ApplicationException {
        try {
            // 1. Check data validation.
            if(null == packageMapping) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, Const.DATA_IS_WRONG);
            }

            // 2. Insert mapping relation between service instance and service package.
            InvServicePackageMapper packageMapper = getMapper(InvServicePackageMapper.class);
            packageMapper.insert(packageMapping);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert mapping relation. {}", exception);
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }

    /**
     * Delete relation instance by service ID.<br>
     * 
     * @param serviceId service ID
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    @Override
    public void delete(String serviceId) throws ApplicationException {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, Const.DATA_IS_WRONG);
            }

            // 2. Delete relation instance.
            InvServicePackageMapper packageMapper = getMapper(InvServicePackageMapper.class);
            packageMapper.delete(serviceId);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert mapping relation. {}", exception);
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }

    /**
     * Query service instance.<br>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @throws ApplicationException when database exception
     * @since SDNO 0.5
     */
    @Override
    public ServicePackageModel queryServiceById(String serviceId) throws ApplicationException {
        try {
            return getMapper(InvServicePackageMapper.class).queryServiceById(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query service instance. {}", e);
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }

    /**
     * Get sql mapper.<br>
     * 
     * @param type mapper class
     * @return mapper object
     * @since SDNO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        SqlSession session = dbSessionHandler.getSqlSession();
        return session.getMapper(type);
    }

}
