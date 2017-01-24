/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.model.servicemodel.vpn.VpnVo;
import org.openo.sdno.nslcm.dao.inf.IServiceModelDao;
import org.openo.sdno.nslcm.dao.inf.IServicePackageDao;
import org.openo.sdno.nslcm.dao.inf.IServiceParameterDao;
import org.openo.sdno.nslcm.model.nbi.JobQueryResponse;
import org.openo.sdno.nslcm.model.nbi.JobResponseDescriptor;
import org.openo.sdno.nslcm.model.nbi.LongOperationResponse;
import org.openo.sdno.nslcm.model.nbi.NsCreationRequest;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstanceQueryResponse;
import org.openo.sdno.nslcm.model.nbi.NsInstantiationRequest;
import org.openo.sdno.nslcm.model.nbi.NsTerminationRequest;
import org.openo.sdno.nslcm.model.nbi.PackageManagementResponse;
import org.openo.sdno.nslcm.model.nbi.PackageOnboardRequest;
import org.openo.sdno.nslcm.model.servicemo.InvServiceModel;
import org.openo.sdno.nslcm.model.servicemo.ServicePackageModel;
import org.openo.sdno.nslcm.model.servicemo.ServiceParameter;
import org.openo.sdno.nslcm.model.translator.Translator;
import org.openo.sdno.nslcm.service.inf.NslcmService;
import org.openo.sdno.nslcm.util.Const;
import org.openo.sdno.nslcm.util.exception.ThrowException;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.servicemodel.SiteToDcNbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * The rest interface of NSLCM.<br>
 * 
 * @author
 * @version SDNO 0.5 September 8, 2016
 */
@Controller("nslcmSvcRoaResource")
@Path("/sdnonslcm/v1")
public class NslcmSvcRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NslcmSvcRoaResource.class);

    @Autowired
    private NslcmService nslcmService;

    @Autowired
    private IServiceModelDao iServiceModelDao;

    @Autowired
    private IServicePackageDao iServicePackageDao;

    @Autowired
    private IServiceParameterDao iServiceParameterDao;

    /**
     * Create SDN-O service instance based on a template.<br>
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
        LOGGER.info("NsCreationPost enter");

        long infterEnterTime = System.currentTimeMillis();

        // check the creation is existed or not, and forbid to create if the data is existed
        String nsdId = nsRequest.getNsdId();

        // query service template information from Catalog
        Map<String, Object> templateInfo = nslcmService.queryServiceTemplate(nsdId);
        if(null == templateInfo) {
            throw new ServiceException("nsdId[" + nsdId + "] is invalid, query from catalog failure");
        }

        String templateName = (String)templateInfo.get(Const.TEMPLATE_NAME);
        String serviceDefId = (String)templateInfo.get(Const.CSAR_ID);
        if(null == templateName || (!templateName.equals(Const.UNDERLAYVPN_TEMPLATE_NAME)
                && !templateName.equals(Const.OVERLAYVPN_TEMPLATE_NAME))) {
            ThrowException.throwParameterInvalid("templateName[" + templateName + "] is invalid");
        }

        String serviceId = UuidUtils.createUuid();

        InvServiceModel serviceModel = convert2ServiceModel(nsRequest, serviceId);
        ServicePackageModel servicePackageModel =
                convert2ServicePackageModel(nsdId, templateName, serviceDefId, serviceId);

        iServiceModelDao.insert(serviceModel);
        iServicePackageDao.insert(servicePackageModel);

        LOGGER.info("Exit nsCreationPost method. cost time = " + (System.currentTimeMillis() - infterEnterTime));

        NsCreationResponse nsCreationResponse = new NsCreationResponse();
        nsCreationResponse.setNsInstanceId(serviceModel.getServiceId());

        return nsCreationResponse;
    }

    /**
     * Create SDN-O service instance with parameters.<br>
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
        LOGGER.info("NsInstantiationPost enter");

        String nsInstanceId = nsInstantiationRequest.getNsInstanceId();
        if(!nsInstanceId.equals(instanceId)) {
            ThrowException.throwParameterInvalid("instanceId[" + instanceId + "] is invalid");
        }

        List<ServiceParameter> serviceParameterList = new ArrayList<ServiceParameter>();
        insertServiceParameter(nsInstantiationRequest, nsInstanceId, serviceParameterList);

        Map<String, String> response = null;
        try {
            String templateName = queryTemplateName(instanceId);
            if(Const.OVERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                SiteToDcNbi siteToDcNbiMo = Translator.translateList2Overlay(serviceParameterList, instanceId);
                response = nslcmService.createOverlay(siteToDcNbiMo, instanceId);
            } else {
                VpnVo vpnVo = Translator.translateList2Underlay(serviceParameterList, instanceId);
                response = nslcmService.createUnderlay(vpnVo, instanceId);
            }
        } catch(ServiceException e) {
            LOGGER.error("NsInstantiationPost failed", e);
            throw e;
        }

        LongOperationResponse longOperationResponse = new LongOperationResponse();
        longOperationResponse.setJobId(response.get("vpnId"));

        LOGGER.info("NsInstantiationPost success, vpnid: " + longOperationResponse.getJobId());

        return longOperationResponse;
    }

    /**
     * Terminate a SDN-O service instance.<br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be terminated
     * @param nsTerminationRequest The request used to terminate a SDN-O service instance
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
        LOGGER.info("NsTerminationPost enter");

        List<ServiceParameter> serviceParameterList = queryServiceParameter(instanceId);
        Map<String, String> response = null;

        try {
            String templateName = queryTemplateName(instanceId);

            if(Const.OVERLAYVPN_TEMPLATE_NAME.equals(templateName)) {
                response = nslcmService.deleteOverlay(instanceId);
            } else {
                response = nslcmService.deleteUnderlay(instanceId, serviceParameterList);
            }
        } catch(ServiceException e) {
            LOGGER.error("NsTerminationPost failed", e);
            throw e;
        }

        LongOperationResponse longOperationResponse = new LongOperationResponse();
        longOperationResponse.setJobId(response.get("errorCode"));

        LOGGER.info("NsTerminationPost exit");

        return longOperationResponse;
    }

    /**
     * Query one SDN-O service instance.<br>
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
     * Delete a SDN-O service instance.<br>
     * 
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param instanceId ID of the SDN-O service instance to be deleted
     * @throws ServiceException When delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/ns/{instanceid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void nsDeletionDelete(@Context HttpServletRequest req, @Context HttpServletResponse resp,
            @PathParam("instanceid") String instanceId) throws ServiceException {
        LOGGER.info("NsDeletionDelete enter");

        iServiceParameterDao.delete(instanceId);
        iServicePackageDao.delete(instanceId);
        iServiceModelDao.delete(instanceId);

        LOGGER.info("NsDeletionDelete exit");
    }

    /**
     * Query one job that terminates or instantiates one SDN-O service.<br>
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
        LOGGER.info("JobQueryGet enter");

        JobQueryResponse jobQueryResponse = new JobQueryResponse();
        jobQueryResponse.setJobId(jobId);
        JobResponseDescriptor jobResponseDescriptor = new JobResponseDescriptor();
        jobResponseDescriptor.setProgress("100");
        jobResponseDescriptor.setStatus("finished");
        jobQueryResponse.setResponseDescriptor(jobResponseDescriptor);

        LOGGER.info("JobQueryGet exit");

        return jobQueryResponse;
    }

    /**
     * On-boarding a NS package.<br>
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
        LOGGER.info("PackageOnboardingPost enter");

        PackageManagementResponse packageManagementResponse = new PackageManagementResponse();
        packageManagementResponse.setErrorCode(ErrorCode.OVERLAYVPN_SUCCESS);
        packageManagementResponse.setStatus(String.valueOf(HttpCode.RESPOND_OK));
        packageManagementResponse.setStatusDescription("description of operation status");

        LOGGER.info("PackageOnboardingPost exit");

        return packageManagementResponse;
    }

    /**
     * Delete a NS package from SDN-O.<br>
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
        LOGGER.info("PackageDeletionDelete enter");

        PackageManagementResponse packageManagementResponse = new PackageManagementResponse();
        packageManagementResponse.setErrorCode(ErrorCode.OVERLAYVPN_SUCCESS);
        packageManagementResponse.setStatus(String.valueOf(HttpCode.RESPOND_OK));
        packageManagementResponse.setStatusDescription("description of operation status");

        LOGGER.info("PackageDeletionDelete exit");

        return packageManagementResponse;
    }

    private void insertServiceParameter(NsInstantiationRequest nsInstantiationRequest, String nsInstanceId,
            List<ServiceParameter> serviceParameterList) throws ServiceException {
        Map<String, Object> sdnoTemplateParameter = nsInstantiationRequest.getAdditionalParamForNs();
        for(String inputKey : sdnoTemplateParameter.keySet()) {
            ServiceParameter serviceParameter = new ServiceParameter();
            serviceParameter.setServiceId(nsInstanceId);
            serviceParameter.setInputKey(inputKey);
            serviceParameter.setInputValue((String)sdnoTemplateParameter.get(inputKey));
            serviceParameterList.add(serviceParameter);
        }
        iServiceParameterDao.batchInsert(serviceParameterList);
    }

    private List<ServiceParameter> queryServiceParameter(String instanceId) throws ServiceException {
        return iServiceParameterDao.queryServiceById(instanceId);
    }

    private String queryTemplateName(String instanceId) throws ServiceException {
        return iServicePackageDao.queryServiceById(instanceId).getTemplateName();
    }

    private InvServiceModel convert2ServiceModel(NsCreationRequest nsRequest, String serviceId) {
        InvServiceModel serviceModel = new InvServiceModel();
        serviceModel.setServiceId(serviceId);
        serviceModel.setServiceName(nsRequest.getNsName());
        serviceModel.setServiceType("SDNO");
        serviceModel.setDescription(nsRequest.getDescription());
        serviceModel.setActiveStatus(Const.ACTIVE);
        serviceModel.setCreator(Const.DEFAULT_STRING);
        serviceModel.setCreateTime(System.currentTimeMillis());
        serviceModel.setStatus(Const.CREATE_SUCCESS);
        return serviceModel;
    }

    private ServicePackageModel convert2ServicePackageModel(String nsdId, String templateName, String serviceDefId,
            String serviceId) {
        ServicePackageModel servicePackageMo = new ServicePackageModel();
        servicePackageMo.setTemplateName(templateName);
        servicePackageMo.setServiceId(serviceId);
        servicePackageMo.setTemplateId(nsdId);
        servicePackageMo.setServiceDefId(serviceDefId);
        return servicePackageMo;
    }
}
