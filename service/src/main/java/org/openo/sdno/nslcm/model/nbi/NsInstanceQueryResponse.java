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

package org.openo.sdno.nslcm.model.nbi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "SDN-O Service Instance Query Response, i.e., details of a service instance")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

/**
 * SDN-O Service Instance Query Response, i.e., details of a service instance. <br/>
 * 
 * @author
 * @version SDNO 0.5 Aug 31, 2016
 */
public class NsInstanceQueryResponse {

    private String id = null;

    private String name = null;

    private String nsdId = null;

    private String description = null;

    private List<SdnoTemplateParameter> additionalParams = new ArrayList<SdnoTemplateParameter>();

    public NsInstanceQueryResponse id(String id) {
        this.id = id;
        return this;
    }

    @ApiModelProperty(required = true, value = "ID of the SDN-O service instance")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NsInstanceQueryResponse name(String name) {
        this.name = name;
        return this;
    }

    @ApiModelProperty(required = true, value = "name of the SDN-O service instance")
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NsInstanceQueryResponse nsdId(String nsdId) {
        this.nsdId = nsdId;
        return this;
    }

    @ApiModelProperty(required = true, value = "ID of the template used to create this SDN-O service instance")
    @JsonProperty("nsdId")
    public String getNsdId() {
        return nsdId;
    }

    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }

    public NsInstanceQueryResponse description(String description) {
        this.description = description;
        return this;
    }

    @ApiModelProperty(value = "description of the SDN-O service instance")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NsInstanceQueryResponse additionalParams(List<SdnoTemplateParameter> additionalParams) {
        this.additionalParams = additionalParams;
        return this;
    }

    @ApiModelProperty(value = "parameters used to instantiate this SDN-O service instance")
    @JsonProperty("additionalParams")
    public List<SdnoTemplateParameter> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(List<SdnoTemplateParameter> additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        NsInstanceQueryResponse nsInstanceQueryResponse = (NsInstanceQueryResponse)o;
        return Objects.equals(id, nsInstanceQueryResponse.id) && Objects.equals(name, nsInstanceQueryResponse.name)
                && Objects.equals(nsdId, nsInstanceQueryResponse.nsdId)
                && Objects.equals(description, nsInstanceQueryResponse.description)
                && Objects.equals(additionalParams, nsInstanceQueryResponse.additionalParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nsdId, description, additionalParams);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NsInstanceQueryResponse {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    nsdId: ").append(toIndentedString(nsdId)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    additionalParams: ").append(toIndentedString(additionalParams)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if(o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
