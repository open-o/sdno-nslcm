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

package org.openo.sdno.nslcm.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.nslcm.dao.inf.IServiceModelDao;
import org.openo.sdno.nslcm.dao.multi.DatabaseSessionHandler;
import org.openo.sdno.nslcm.mapper.InvServiceModelMapper;
import org.openo.sdno.nslcm.model.servicemo.InvServiceModel;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.exception.ApplicationException;
import org.openo.sdno.nslcm.util.operation.ValidateUtil;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database operation class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public class ServiceModelDaoImpl implements IServiceModelDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModelDaoImpl.class);

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
     * Insert service stances.<br>
     * 
     * @param serviceModel service instance
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    @Override
    public void insert(InvServiceModel serviceModel) throws ApplicationException {
        try {
            if(null == serviceModel) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, Const.DATA_IS_WRONG);
            }

            InvServiceModelMapper serviceModelMapper = getMapper(InvServiceModelMapper.class);
            serviceModelMapper.insert(serviceModel);

        } catch(Exception exception) {
            LOGGER.error("Fail to insert service instance. {}", exception);
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }

    /**
     * Delete service instance.<br>
     * 
     * @param serviceId service instance ID
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    @Override
    public void delete(String serviceId) throws ApplicationException {
        try {
            ValidateUtil.assertStringNotNull(serviceId);

            InvServiceModelMapper serviceModelMapper = getMapper(InvServiceModelMapper.class);
            serviceModelMapper.delete(serviceId);
        } catch(Exception exception) {
            LOGGER.error("Fail to delete service instance. {}", exception);
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
    public InvServiceModel queryServiceById(String serviceId) throws ApplicationException {
        try {
            return getMapper(InvServiceModelMapper.class).queryServiceById(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query service instance. {}", e);
            throw new ApplicationException(HttpCode.ERR_FAILED, Const.OPER_DB_FAIL);
        }
    }

    /**
     * Get mapper to operate database.<br>
     * 
     * @param type class type of Mapper
     * @return Mapper object
     * @since SDNO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        SqlSession session = dbSessionHandler.getSqlSession();
        return session.getMapper(type);
    }
}
