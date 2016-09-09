
package org.openo.sdno.nslcm.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.nslcm.mocoserver.MocoTemplateNameWrong;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class ITTemplateNameWrongFailed extends TestManager {

    private static MocoTemplateNameWrong sbiAdapterServer = new MocoTemplateNameWrong();

    private static final String CREATE_NSLCM_EXISTED_FAILED_TESTCASE =
            "src/integration-test/resources/testcase/createnslcmfailed.json";

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
        testCreateNslcm(CREATE_NSLCM_EXISTED_FAILED_TESTCASE);
    }

    private void testCreateNslcm(String createNslcmPath) throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(createNslcmPath);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new FailedChecker());
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
