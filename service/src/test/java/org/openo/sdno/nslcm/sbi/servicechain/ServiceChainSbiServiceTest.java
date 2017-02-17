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

package org.openo.sdno.nslcm.sbi.servicechain;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.nslcm.mock.restfulproxy.MockFailRestfulProxy;
import org.openo.sdno.nslcm.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.servicechain.ServiceChainPath;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceChainSbiServiceTest extends SpringTest {

    @Autowired
    private ServiceChainSbiService serviceChainSbiService;

    @Test(expected = ParameterServiceException.class)
    public void emptyUuidQueryTest() throws ServiceException {
        serviceChainSbiService.queryServiceChain(null);
    }

    @Test(expected = ParameterServiceException.class)
    public void emptyUuidCreateTest() throws ServiceException {
        serviceChainSbiService.createServiceChain(null);
    }

    @Test(expected = ParameterServiceException.class)
    public void emptyUuidDeleteTest() throws ServiceException {
        serviceChainSbiService.deleteServiceChain(null);
    }

    @Test(expected = ServiceException.class)
    public void adapterErrorQueryTest() throws ServiceException {
        new MockFailRestfulProxy();
        serviceChainSbiService.queryServiceChain("serviceChainUuid");
    }

    @Test(expected = ServiceException.class)
    public void adapterErrorCreateTest() throws ServiceException {
        new MockFailRestfulProxy();
        ServiceChainPath sfp = new ServiceChainPath();
        serviceChainSbiService.createServiceChain(sfp);
    }

    @Test(expected = ServiceException.class)
    public void adapterErrorDeleteTest() throws ServiceException {
        new MockFailRestfulProxy();
        serviceChainSbiService.deleteServiceChain("serviceChainUuid");
    }

}
