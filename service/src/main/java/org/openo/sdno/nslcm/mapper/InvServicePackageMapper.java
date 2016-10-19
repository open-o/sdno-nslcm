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

import org.apache.ibatis.annotations.Param;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;

/**
 * Mapping Class of inventory data operation.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 Oct 18, 2016
 */
public interface InvServicePackageMapper {

    /**
     * Insert relation instance.<br>
     * 
     * @param servicePackageMapping service package mapping data
     * @since SDNO 0.5
     */
    void insert(ServicePackageModel servicePackageMapping);

    /**
     * Delete relation instance by service ID.<br>
     * 
     * @param serviceId service ID
     * @since SDNO 0.5
     */
    void delete(@Param("serviceId") String serviceId);

    /**
     * Query service instance by service ID.<br>
     * 
     * @param serviceId service ID
     * @return service instance
     * @since SDNO 0.5
     */
    ServicePackageModel queryServiceById(@Param("serviceId") String serviceId);

}
