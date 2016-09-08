/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

/**
 * DC gateway controller south branch interface. <br/>
 * 
 * @author
 * @version SDNO 0.5 Jun 22, 2016
 */
public interface CatalogSbiService extends IService {

    /**
     * Query template information from catalog. <br/>
     * 
     * @param nsdId ID of the template in catalog used to create the SDN-O service instance
     * @return The template information
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    Map<String, String> queryServiceTemplate(String nsdId) throws ServiceException;
}
