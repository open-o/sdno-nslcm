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

@ApiModel(description = "SDN-O Service Instance Instantiation Request")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

/**
 * SDN-O Service Instance Instantiation Request. <br/>
 * 
 * @author
 * @version SDNO 0.5 Aug 31, 2016
 */
public class NsInstantiationRequest {

    private String nsInstanceId = null;

    private List<SdnoTemplateParameter> additionalParamForNS = new ArrayList<SdnoTemplateParameter>();

    public NsInstantiationRequest nsInstanceId(String nsInstanceId) {
        this.nsInstanceId = nsInstanceId;
        return this;
    }

    @ApiModelProperty(required = true, value = "ID of the SDN-O service instance to be instantiated")
    @JsonProperty("nsInstanceId")
    public String getNsInstanceId() {
        return nsInstanceId;
    }

    public void setNsInstanceId(String nsInstanceId) {
        this.nsInstanceId = nsInstanceId;
    }

    public NsInstantiationRequest additionalParamForNS(List<SdnoTemplateParameter> additionalParamForNS) {
        this.additionalParamForNS = additionalParamForNS;
        return this;
    }

    @ApiModelProperty(value = "parameters used to instantiate this SDN-O service instance")
    @JsonProperty("additionalParamForNS")
    public List<SdnoTemplateParameter> getAdditionalParamForNS() {
        return additionalParamForNS;
    }

    public void setAdditionalParamForNS(List<SdnoTemplateParameter> additionalParamForNS) {
        this.additionalParamForNS = additionalParamForNS;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        NsInstantiationRequest nsInstantiationRequest = (NsInstantiationRequest)o;
        return Objects.equals(nsInstanceId, nsInstantiationRequest.nsInstanceId)
                && Objects.equals(additionalParamForNS, nsInstantiationRequest.additionalParamForNS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nsInstanceId, additionalParamForNS);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NsInstantiationRequest {\n");

        sb.append("    nsInstanceId: ").append(toIndentedString(nsInstanceId)).append("\n");
        sb.append("    additionalParamForNS: ").append(toIndentedString(additionalParamForNS)).append("\n");
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
