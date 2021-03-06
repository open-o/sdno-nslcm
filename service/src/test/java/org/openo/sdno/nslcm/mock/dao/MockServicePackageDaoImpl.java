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

package org.openo.sdno.nslcm.mock.dao;

import org.openo.sdno.nslcm.dao.impl.ServicePackageDaoImpl;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;
import org.openo.sdno.nslcm.util.exception.ApplicationException;

import mockit.Mock;
import mockit.MockUp;

public class MockServicePackageDaoImpl extends MockUp<ServicePackageDaoImpl> {

    @Mock
    public void insert(ServicePackageModel packageMapping) throws ApplicationException {
        return;
    }

    @Mock
    public void delete(String serviceId) throws ApplicationException {
        return;
    }

    @Mock
    public ServicePackageModel queryServiceById(String serviceId) throws ApplicationException {
        ServicePackageModel servicePackageModel = new ServicePackageModel();
        servicePackageModel.setTemplateId("nsdIdtest");
        servicePackageModel.setTemplateName("enterprise2DC");
        return servicePackageModel;
    }

}
