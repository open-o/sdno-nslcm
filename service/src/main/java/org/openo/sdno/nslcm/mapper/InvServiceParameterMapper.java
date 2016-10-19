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

package org.openo.sdno.nslcm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;

/**
 * Mapping Class of inventory data operation.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public interface InvServiceParameterMapper {

    /**
     * Insert service parameter in batch.<br>
     * 
     * @param serviceParams service parameters
     * @since SDNO 0.5
     */
    void batchInsert(@Param("serviceParams") List<ServiceParameter> serviceParams);

    /**
     * Delete service parameters by service ID.<br>
     * 
     * @param serviceId service instance ID
     * @since SDNO 0.5
     */
    void delete(@Param("serviceId") String serviceId);

    /**
     * Query service parameters by service ID.<br>
     * 
     * @return The list of service parameters
     * @since SDNO 0.5
     */
    List<ServiceParameter> queryAllService();
}
