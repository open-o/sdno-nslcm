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

package org.openo.sdno.nslcm.model.translator;

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.nslcm.model.BusinessModel;

/**
 * Interface class of Vpn Translator.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-3-29
 */
public interface VpnTranslator {

    /**
     * Translate OverlayVpn model.<br>
     * 
     * @param templateParameter template parameter
     * @param instanceId Nslcm Instance Id
     * @return BusinessModel translated
     * @throws ServiceException when translate failed
     * @since SDNO 0.5
     */
    BusinessModel translateVpnModel(Map<String, Object> templateParameter, String instanceId) throws ServiceException;

}
