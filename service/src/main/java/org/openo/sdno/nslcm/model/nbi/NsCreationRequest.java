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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "SDN-O Service Instance Creation Request")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

/**
 * SDN-O Service Instance Creation Request. <br/>
 * 
 * @author
 * @version SDNO 0.5 Aug 31, 2016
 */
public class NsCreationRequest {

    private String nsdId = null;

    private String nsName = null;

    private String description = null;

    public NsCreationRequest nsdId(String nsdId) {
        this.nsdId = nsdId;
        return this;
    }

    @ApiModelProperty(required = true, value = "ID of the template in catalog used to create the SDN-O service instance")
    @JsonProperty("nsdId")
    public String getNsdId() {
        return nsdId;
    }

    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }

    public NsCreationRequest nsName(String nsName) {
        this.nsName = nsName;
        return this;
    }

    @ApiModelProperty(required = true, value = "name of the SDN-O service instance to be created")
    @JsonProperty("nsName")
    public String getNsName() {
        return nsName;
    }

    public void setNsName(String nsName) {
        this.nsName = nsName;
    }

    public NsCreationRequest description(String description) {
        this.description = description;
        return this;
    }

    @ApiModelProperty(value = "description of the SDN-O service instance to be created")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        NsCreationRequest nsCreationRequest = (NsCreationRequest)o;
        return Objects.equals(nsdId, nsCreationRequest.nsdId) && Objects.equals(nsName, nsCreationRequest.nsName)
                && Objects.equals(description, nsCreationRequest.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nsdId, nsName, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NsCreationRequest {\n");

        sb.append("    nsdId: ").append(toIndentedString(nsdId)).append("\n");
        sb.append("    nsName: ").append(toIndentedString(nsName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
