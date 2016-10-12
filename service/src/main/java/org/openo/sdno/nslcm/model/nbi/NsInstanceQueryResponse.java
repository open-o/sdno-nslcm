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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SDN-O Service Instance Query Response, i.e., details of a service instance.<br>
 * 
 * @author
 * @version SDNO 0.5 September 8, 2016
 */
@ApiModel(description = "SDN-O Service Instance Query Response, i.e., details of a service instance")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")
public class NsInstanceQueryResponse {

    private String id = null;

    private String name = null;

    private String nsdId = null;

    private String description = null;

    private List<SdnoTemplateParameter> additionalParams = new ArrayList<SdnoTemplateParameter>();

    @ApiModelProperty(required = true, value = "ID of the SDN-O service instance")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(required = true, value = "name of the SDN-O service instance")
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(required = true, value = "ID of the template used to create this SDN-O service instance")
    @JsonProperty("nsdId")
    public String getNsdId() {
        return nsdId;
    }

    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }

    @ApiModelProperty(value = "description of the SDN-O service instance")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "parameters used to instantiate this SDN-O service instance")
    @JsonProperty("additionalParams")
    public List<SdnoTemplateParameter> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(List<SdnoTemplateParameter> additionalParams) {
        this.additionalParams = additionalParams;
    }

}
