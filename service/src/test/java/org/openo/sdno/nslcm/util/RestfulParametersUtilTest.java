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

package org.openo.sdno.nslcm.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.sdno.overlayvpn.security.authentication.HttpContext;

public class RestfulParametersUtilTest {

    @Test
    public void getRestfulParametersWithBodyTest() {
        String testBody = "body";
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters(testBody);
        assertTrue("body".equals(restfulParameters.getRawData()));
        assertTrue(HttpContext.MEDIA_TYPE_JSON
                .equals(restfulParameters.getHttpContextHeader(HttpContext.CONTENT_TYPE_HEADER)));
    }

    @Test
    public void getRestfulParametersTest() {
        RestfulParametes restfulParameters = RestfulParametersUtil.getRestfulParameters();
        assertTrue(HttpContext.MEDIA_TYPE_JSON
                .equals(restfulParameters.getHttpContextHeader(HttpContext.CONTENT_TYPE_HEADER)));
    }

}
