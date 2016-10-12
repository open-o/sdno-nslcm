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

package org.openo.sdno.nslcm.model.nbi;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SDN-O NS Package On-boarding Request.<br>
 * 
 * @author
 * @version SDNO 0.5 September 8, 2016
 */
@ApiModel(description = "SDN-O NS Package Onboarding Request")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")
public class PackageOnboardRequest {

    private UUID csarId = null;

    @ApiModelProperty(required = true, value = "UUID of the SDN-O NS package to be onboarded.")
    @JsonProperty("csarId")
    public UUID getCsarId() {
        return csarId;
    }

    public void setCsarId(UUID csarId) {
        this.csarId = csarId;
    }
}
