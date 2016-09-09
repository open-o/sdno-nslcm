
package org.openo.sdno.nslcm.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.nslcm.mocoserver.MocoUnderlaySuccessServer;
import org.openo.sdno.nslcm.model.nbi.NsCreationResponse;
import org.openo.sdno.nslcm.util.HttpRest;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;

public class ITNsdIdExistedFailed extends TestManager {

    private static MocoUnderlaySuccessServer sbiAdapterServer = new MocoUnderlaySuccessServer();

    private static final String CREATE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmsuccess.json";

    private static final String CREATE_NSLCM_EXISTED_FAILED_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmexistedfailed.json";

    private static final String DELETE_NSLCM_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase/deletenslcmsuccess.json";

    @BeforeClass
    public static void setup() throws ServiceException {
        sbiAdapterServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        sbiAdapterServer.stop();
    }

    @Test
    public void testNsdIdExistedFailed() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_NSLCM_SUCCESS_TESTCASE);
        HttpRequest request = httpCreateObject.getRequest();
        RestfulResponse responseResult = HttpRest.doSend(request);
        HttpResponse response = HttpModelUtils.convertResponse(responseResult);
        NsCreationResponse nsCreationResponse = JsonUtil.fromJson(response.getData(), NsCreationResponse.class);
        String instanceId = nsCreationResponse.getNsInstanceId();

        testCreateNslcm(CREATE_NSLCM_EXISTED_FAILED_TESTCASE);

        testOperation(DELETE_NSLCM_SUCCESS_TESTCASE, instanceId);
    }

    private void testCreateNslcm(String createNslcmPath) throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(createNslcmPath);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new FailedChecker());
    }

    private void testOperation(String queryPath, String instanceId) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponseFromFile(queryPath);
        HttpRequest hrttpRequest = httpObject.getRequest();

        hrttpRequest.setUri(PathReplace.replaceUuid("instanceid", hrttpRequest.getUri(), instanceId));
        execTestCase(hrttpRequest, new SuccessChecker());
    }

    private class SuccessChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(HttpCode.isSucess(response.getStatus())) {
                return true;
            }
            return false;
        }

    }

    private class FailedChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(HttpCode.isSucess(response.getStatus())) {
                return false;
            }
            return true;
        }

    }

}
