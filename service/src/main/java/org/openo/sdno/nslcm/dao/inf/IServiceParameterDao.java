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

package org.openo.sdno.nslcm.dao.inf;

import java.util.List;

import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.nslcm.util.exception.ApplicationException;

/**
 * Operate database interface.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public interface IServiceParameterDao {

    /**
     * Insert service instance.<br>
     * 
     * @param serviceParameter service parameter data
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    void batchInsert(List<ServiceParameter> serviceParameter) throws ApplicationException;

    /**
     * Delete service instance by service ID.<br>
     * 
     * @param serviceId service ID
     * @throws ApplicationException when database exception or parameter is wrong
     * @since SDNO 0.5
     */
    void delete(String serviceId) throws ApplicationException;

    /**
     * Query some service instance.<br>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @throws ApplicationException when database exception
     * @since SDNO 0.5
     */
    List<ServiceParameter> queryServiceById(String serviceId) throws ApplicationException;
}
