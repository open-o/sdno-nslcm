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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "SDN-O Service Instance Termination Request.")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-08-31T18:01:03.332+08:00")

/**
 * SDN-O Service Instance Termination Request. <br>
 * 
 * @author
 * @version SDNO 0.5 Aug 31, 2016
 */
public class NsTerminationRequest {

    private String nsInstanceId = null;

    private String terminationType = null;

    private String gracefulTerminationTimeout = null;

    public NsTerminationRequest nsInstanceId(String nsInstanceId) {
        this.nsInstanceId = nsInstanceId;
        return this;
    }

    @ApiModelProperty(required = true, value = "id of the SDN-O service instance to be terminated")
    @JsonProperty("nsInstanceId")
    public String getNsInstanceId() {
        return nsInstanceId;
    }

    public void setNsInstanceId(String nsInstanceId) {
        this.nsInstanceId = nsInstanceId;
    }

    public NsTerminationRequest terminationType(String terminationType) {
        this.terminationType = terminationType;
        return this;
    }

    @ApiModelProperty(required = true, value = "type of termination. graceful or not, etc.")
    @JsonProperty("terminationType")
    public String getTerminationType() {
        return terminationType;
    }

    public void setTerminationType(String terminationType) {
        this.terminationType = terminationType;
    }

    public NsTerminationRequest gracefulTerminationTimeout(String gracefulTerminationTimeout) {
        this.gracefulTerminationTimeout = gracefulTerminationTimeout;
        return this;
    }

    @ApiModelProperty(value = "timeout value for a graceful timeout")
    @JsonProperty("gracefulTerminationTimeout")
    public String getGracefulTerminationTimeout() {
        return gracefulTerminationTimeout;
    }

    public void setGracefulTerminationTimeout(String gracefulTerminationTimeout) {
        this.gracefulTerminationTimeout = gracefulTerminationTimeout;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        NsTerminationRequest nsTerminationRequest = (NsTerminationRequest)o;
        return Objects.equals(nsInstanceId, nsTerminationRequest.nsInstanceId)
                && Objects.equals(terminationType, nsTerminationRequest.terminationType)
                && Objects.equals(gracefulTerminationTimeout, nsTerminationRequest.gracefulTerminationTimeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nsInstanceId, terminationType, gracefulTerminationTimeout);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NsTerminationRequest {\n");

        sb.append("    nsInstanceId: ").append(toIndentedString(nsInstanceId)).append("\n");
        sb.append("    terminationType: ").append(toIndentedString(terminationType)).append("\n");
        sb.append("    gracefulTerminationTimeout: ").append(toIndentedString(gracefulTerminationTimeout)).append("\n");
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
