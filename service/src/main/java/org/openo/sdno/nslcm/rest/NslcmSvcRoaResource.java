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

package org.openo.sdno.nslcm.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.model.db.NsCreationInfo;
import org.openo.sdno.nslcm.model.db.NsInstantiationInfo;
import org.openo.sdno.nslcm.model.nbi.JobQueryResponse;
import org.openo.sdno.nslcm.model.nbi.LongOperationResponse;
import org.openo.sdno.nslcm.model.nbi.NsCreationRequest;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.nslcm.model.nbi.NsTerminationRequest;
import org.openo.sdno.nslcm.model.nbi.PackageManagementResponse;
import org.openo.sdno.nslcm.model.nbi.PackageOnboardRequest;
import org.openo.sdno.nslcm.model.nbi.SdnoTemplateParameter;
import org.openo.sdno.nslcm.model.translator.Translator;
import org.openo.sdno.nslcm.service.inf.NslcmService;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.db.DbOper;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.common.enums.ActionStatus;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The rest interface of nslcm. <br>
 * 
 * @author
 * @version SDNO 0.5 Aug 30, 2016
 */
@Service
@Path("/sdnonslcm/v1")
@SuppressWarnings({"unchecked", "rawtypes"})
public class NslcmSvcRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NslcmSvcRoaResource.class);

    @Resource
    private DbOper dbOper;

    @Resource
    private NslcmService nslcmService;

    public void setDbOper(DbOper dbOper) {
        this.dbOper = dbOper;
    }

    public void setNslcmService(NslcmService nslcmService) {
        this.nslcmService = nslcmService;
    }

    /**
     * Create SDN-O service instance based on a template. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param nsRequest The request used to create a SDN-O service instance
     * @return The object of NsCreationResponse
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/ns")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NsCreationResponse nsCreationPost(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            NsCreationRequest nsRequest) throws ServiceException {

        long infterEnterTime = System.currentTimeMillis();

        // check the creation is existed or not, and forbid to create if the data is existed
        String nsdId = nsRequest.getNsdId();
        if(dbOper.checkRecordIsExisted(NsCreationInfo.class, Const.NSD_ID, nsdId)) {
            ThrowException.throwDataIsExisted(nsdId);
        }

        // query service template information from Catalog
        Map<String, String> templateInfo = nslcmService.queryServiceTemplate(nsdId);
        if(null == templateInfo) {
            ThrowException.throwParameterInvalid("nsdId[" + nsdId + "] is invalid, query from catalog failure");
        }

        String templateName = templateInfo.get(Const.TEMPLATE_NAME);
        if(null == templateName || (!templateName.equals(Const.UNDERLAYVPN_TEMPLATE_NAME)
                && !templateName.equals(Const.OVERLAYVPN_TEMPLATE_NAME))) {
            ThrowException.throwParameterInvalid("templateName[" + templateName + "] is invalid");
        }

        // save the request data
        List<NsCreationInfo> nsCreationInfoList = new ArrayList<NsCreationInfo>();
        NsCreationInfo nsCreationInfo = new NsCreationInfo(nsdId, nsRequest.getNsName(), templateName,
                nsRequest.getDescription(), ActionStatus.NORMAL.getName());
        nsCreationInfo.allocateUuid();
        nsCreationInfoList.add(nsCreationInfo);
        dbOper.insert(nsCreationInfoList);

        // call the service method to perform create operation
        /*
         * try {
         * nslcmService.nsCreationPost(req, resp, nsCreationInfo);
         * } catch(ServiceException e) {
         * LOGGER.info("nsCreationPost exception: " + e);
         * nsCreationInfoList.get(0).setActionState(ActionStatus.CREATE_EXCEPTION.getName());
         * dbOper.update(NsCreationInfo.class, nsCreationInfoList, Const.ACTION_STATE);
         * throw e;
         * }
         */

        // well all-is-well, set the response status as success and return result
        resp.setStatus(HttpCode.CREATE_OK);

        LOGGER.info("Exit nsCreationPost method. cost time = " + (System.currentTimeMillis() - infterEnterTime));

        NsCreationResponse nsCreationResponse = new NsCreationResponse();
        nsCreationResponse.setNsInstanceId(nsCreationInfo.getUuid());

        return nsCreationResponse;
    }

    /**
     * Create SDN-O service instance with parameters. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be instantiated
     * @param nsInstantiationRequest The request used to instantiate a SDN-O service instance
     * @return The object of LongOperationResponse
     * @throws ServiceException When create failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/ns/{instanceid}/instantiate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LongOperationResponse nsInstantiationPost(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("instanceid") String instanceId, NsInstantiationRequest nsInstantiationRequest)
            throws ServiceException {
        String nsInstanceId = nsInstantiationRequest.getNsInstanceId();
        if(!nsInstanceId.equals(instanceId)) {
            ThrowException.throwParameterInvalid("instanceId[" + instanceId + "] is invalid");
        }

        List<NsInstantiationInfo> nsInstantiationInfoList = new ArrayList<NsInstantiationInfo>();
        insertNsInstrantiationInfo(nsInstantiationRequest, nsInstanceId, nsInstantiationInfoList);

        Map<String, String> response = null;
        try {
            String templateName = queryTemplateName(instanceId);
            if(Const.OVERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                SiteToDcNbi siteToDcNbiMo = Translator.translateList2Overlay(nsInstantiationInfoList);
                response = nslcmService.createOverlay(siteToDcNbiMo, instanceId);
            } else if(Const.UNDERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                VpnVo vpnVo = Translator.translateList2Underlay(nsInstantiationInfoList);
                response = nslcmService.createUnderlay(vpnVo, instanceId);
            } else {
                ThrowException.throwParameterInvalid("templateName[" + templateName + "] is invalid");
            }
        } catch(ServiceException e) {
            LOGGER.error("create business failed", e);
            updateNsInstantiationInfoStatus(nsInstantiationInfoList, ActionStatus.CREATE_EXCEPTION.getName());
            throw e;
        }

        updateNsInstantiationInfoStatus(nsInstantiationInfoList, ActionStatus.NORMAL.getName());

        LongOperationResponse longOperationResponse = new LongOperationResponse();
        longOperationResponse.setJobId(response.get("vpnId"));
        return longOperationResponse;
    }

    /**
     * Terminate a SDN-O service instance. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be terminated
     * @param nsRequest The request used to terminate a SDN-O service instance
     * @return The object of LongOperationResponse
     * @throws ServiceException When terminate failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/ns/{instanceid}/terminate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LongOperationResponse nsTerminationPost(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("instanceid") String instanceId, NsTerminationRequest nsTerminationRequest)
            throws ServiceException {
        ResultRsp<List<NsInstantiationInfo>> nsInstantiationInfoRsp = queryNsInstantiationInfo(instanceId);
        updateNsInstantiationInfoStatus(nsInstantiationInfoRsp.getData(), ActionStatus.DELETING.getName());
        Map<String, String> response = null;

        try {
            String templateName = queryTemplateName(instanceId);

            if(Const.OVERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                response = nslcmService.deleteOverlay(instanceId);
            } else if(Const.UNDERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                response = nslcmService.deleteUnderlay(instanceId, nsInstantiationInfoRsp.getData());
            } else {
                ThrowException.throwParameterInvalid("templateName[" + templateName + "] is invalid");
            }
        } catch(ServiceException e) {
            LOGGER.error("create business failed", e);

            updateNsInstantiationInfoStatus(nsInstantiationInfoRsp.getData(), ActionStatus.DELETE_EXCEPTION.getName());
            throw e;
        }

        updateNsInstantiationInfoStatus(nsInstantiationInfoRsp.getData(), ActionStatus.NORMAL.getName());

        LongOperationResponse longOperationResponse = new LongOperationResponse();
        longOperationResponse.setJobId(response.get("errorCode"));
        return longOperationResponse;
    }

    /**
     * Query one SDN-O service instance. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be queried
     * @return The object of NsInstanceQueryResponse
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/ns/{instanceid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NsInstanceQueryResponse nsQueryGet(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("instanceid") String instanceId) throws ServiceException {
        return nslcmService.queryVpn(instanceId);
    }

    /**
     * Delete a SDN-O service instance. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be deleted
     * @return The object of nsDeletionDelete
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/ns/{instanceid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void nsDeletionDelete(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("instanceid") String instanceId) throws ServiceException {
        if(dbOper.checkRecordIsExisted(NsCreationInfo.class, Const.UUID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsCreationInfo>> nsCreationInfoRsp = dbOper.query(NsCreationInfo.class, Const.UUID, instanceId);

        updateNsCreationInfoStatus(nsCreationInfoRsp.getData(), ActionStatus.DELETING.getName());

        ResultRsp<List<NsInstantiationInfo>> nsInstantiationInfoRsp = queryNsInstantiationInfo(instanceId);
        updateNsInstantiationInfoStatus(nsInstantiationInfoRsp.getData(), ActionStatus.DELETING.getName());

        try {
            dbOper.delete(NsInstantiationInfo.class, instanceId);
        } catch(ServiceException e) {
            LOGGER.error("delete business failed", e);
            updateNsInstantiationInfoStatus(nsInstantiationInfoRsp.getData(), ActionStatus.DELETE_EXCEPTION.getName());
            throw e;
        }

        try {
            dbOper.delete(NsCreationInfo.class, instanceId);
        } catch(ServiceException e) {
            LOGGER.error("delete business failed", e);
            updateNsCreationInfoStatus(nsCreationInfoRsp.getData(), ActionStatus.DELETE_EXCEPTION.getName());
            throw e;
        }

    }

    /**
     * Query one job that terminates or instantiates one SDN-O service. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param jobId ID of the job to be queried
     * @return The object of JobQueryResponse
     * @throws ServiceException When query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/jobs/{jobid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JobQueryResponse jobQueryGet(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("jobid") String jobId) throws ServiceException {

        return null;
    }

    /**
     * Onboarding a NS package. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param ssRequest The request used to onboard a NS package to SDN-O
     * @return The object of PackageManagementResponse
     * @since SDNO 0.5
     */
    @POST
    @Path("/nspackages")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PackageManagementResponse packageOnboardingPost(@Context HttpServletRequest req,
            @Context HttpServletResponse resp, PackageOnboardRequest ssRequest) {

        return null;
    }

    /**
     * Delete a NS package from SDN-O. <br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param csarId ID of the NS package to be deleted from SDN-O
     * @return The object of PackageManagementResponse
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/nspackages/{csarid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PackageManagementResponse packageDeletionDelete(@Context HttpServletRequest req,
            @Context HttpServletResponse resp, @PathParam("csarid") String csarId) {

        return null;
    }

    private void updateNsInstantiationInfoStatus(List<NsInstantiationInfo> nsInstantiationInfoList, String actionStatus)
            throws ServiceException {
        for(NsInstantiationInfo nsInstantiationInfoMo : nsInstantiationInfoList) {
            nsInstantiationInfoMo.setActionState(actionStatus);
        }
        dbOper.update(NsInstantiationInfo.class, nsInstantiationInfoList, Const.ACTION_STATE);
    }

    private void updateNsCreationInfoStatus(List<NsCreationInfo> nsCreationInfoList, String actionStatus)
            throws ServiceException {
        for(NsCreationInfo nsCreationInfoMo : nsCreationInfoList) {
            nsCreationInfoMo.setActionState(actionStatus);
        }
        dbOper.update(NsCreationInfo.class, nsCreationInfoList, Const.ACTION_STATE);
    }

    private void insertNsInstrantiationInfo(NsInstantiationRequest nsInstantiationRequest, String nsInstanceId,
            List<NsInstantiationInfo> nsInstantiationInfoList) throws ServiceException {
        for(SdnoTemplateParameter sdnoTemplateParameter : nsInstantiationRequest.getAdditionalParamForNS()) {
            NsInstantiationInfo nsInstantiationInfo = new NsInstantiationInfo();
            nsInstantiationInfo.setInstanceId(nsInstanceId);
            nsInstantiationInfo.setName(sdnoTemplateParameter.getName());
            nsInstantiationInfo.setValue(sdnoTemplateParameter.getValue());
            nsInstantiationInfo.setActionState(ActionStatus.CREATING.getName());
            nsInstantiationInfo.allocateUuid();
            nsInstantiationInfoList.add(nsInstantiationInfo);
        }
        dbOper.insert(nsInstantiationInfoList);
    }

    private ResultRsp<List<NsInstantiationInfo>> queryNsInstantiationInfo(String instanceId) throws ServiceException {
        if(dbOper.checkRecordIsExisted(NsInstantiationInfo.class, Const.INSTANCE_ID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsInstantiationInfo>> nsInstantiationInfoRsp =
                dbOper.query(NsInstantiationInfo.class, Const.INSTANCE_ID, instanceId);
        return nsInstantiationInfoRsp;
    }

    private String queryTemplateName(String instanceId) throws ServiceException {
        if(dbOper.checkRecordIsExisted(NsCreationInfo.class, Const.UUID, instanceId)) {
            ThrowException.throwDataIsExisted(instanceId);
        }
        ResultRsp<List<NsCreationInfo>> rsp = dbOper.query(NsCreationInfo.class, Const.UUID, instanceId);

        String templateName = rsp.getData().get(0).getTemplateName();
        return templateName;
    }
}
