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

import java.util.Arrays;
import java.util.List;

import org.openo.sdno.nslcm.dao.impl.ServiceParametersDaoImpl;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.nslcm.util.exception.ApplicationException;

import mockit.Mock;
import mockit.MockUp;

public class MockServiceParametersDaoImpl extends MockUp<ServiceParametersDaoImpl> {

    @Mock
    public List<ServiceParameter> queryServiceById(String serviceId) throws ApplicationException {
        return Arrays.asList(new ServiceParameter());
    }

    @Mock
    public void batchInsert(List<ServiceParameter> serviceParameter) throws ApplicationException {
        return;
    }

    @Mock
    public void delete(String serviceId) throws ApplicationException {
        return;
    }
}
