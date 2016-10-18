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

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SDN-O Service Instance Creation Request.<br>
 * 
 * @author
 * @version SDNO 0.5 September 8, 2016
 */
@ApiModel(description = "SDN-O Service Instance Creation Request")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

public class NsCreationRequest {

    private String nsdId = null;

    private String nsName = null;

    private String description = null;

    private String serviceType = null;

    @ApiModelProperty(required = true, value = "ID of the template in catalog used to create the SDN-O service instance")
    @JsonProperty("nsdId")
    public String getNsdId() {
        return nsdId;
    }

    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }

    @ApiModelProperty(required = true, value = "name of the SDN-O service instance to be created")
    @JsonProperty("nsName")
    public String getNsName() {
        return nsName;
    }

    public void setNsName(String nsName) {
        this.nsName = nsName;
    }

    @ApiModelProperty(value = "description of the SDN-O service instance to be created")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "serviceType of the SDN-O service instance to be created")
    @JsonProperty("serviceType")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
