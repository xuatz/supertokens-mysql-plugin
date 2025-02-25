/*
 *    Copyright (c) 2020, VRAI Labs and/or its affiliates. All rights reserved.
 *
 *    This software is licensed under the Apache License, Version 2.0 (the
 *    "License") as published by the Apache Software Foundation.
 *
 *    You may not use this file except in compliance with the License. You may
 *    obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 *
 */

package io.supertokens.storage.mysql.test;

import com.google.gson.JsonObject;
import io.supertokens.ProcessState;
import io.supertokens.pluginInterface.exceptions.StorageQueryException;
import io.supertokens.pluginInterface.exceptions.StorageTransactionLogicException;
import io.supertokens.session.Session;
import io.supertokens.session.info.SessionInformationHolder;
import io.supertokens.storageLayer.StorageLayer;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InMemoryDBTest {
    @Rule
    public TestRule watchman = Utils.getOnFailure();

    @AfterClass
    public static void afterTesting() {
        Utils.afterTesting();
    }

    @Before
    public void beforeEach() {
        Utils.reset();
    }

    @Test
    public void checkThatInMemDVWorksEvenIfWrongConfig()
            throws InterruptedException, StorageQueryException, NoSuchAlgorithmException, InvalidKeyException,
            SignatureException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException,
            IOException, InvalidKeySpecException, IllegalBlockSizeException, StorageTransactionLogicException {
        {
            Utils.commentConfigValue("mysql_user");
            Utils.commentConfigValue("mysql_password");

            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            String userId = "userId";
            JsonObject userDataInJWT = new JsonObject();
            userDataInJWT.addProperty("key", "value");
            JsonObject userDataInDatabase = new JsonObject();
            userDataInDatabase.addProperty("key", "value");

            SessionInformationHolder sessionInfo = Session.createNewSession(process.getProcess(), userId, userDataInJWT,
                    userDataInDatabase);

            assert sessionInfo.accessToken != null;
            assert sessionInfo.refreshToken != null;

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 1);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }

        {
            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 0);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }
    }

    @Test
    public void checkThatActualDBWorksIfCorrectConfigDev() throws InterruptedException, StorageQueryException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException,
            IllegalBlockSizeException, StorageTransactionLogicException {
        {
            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            String userId = "userId";
            JsonObject userDataInJWT = new JsonObject();
            userDataInJWT.addProperty("key", "value");
            JsonObject userDataInDatabase = new JsonObject();
            userDataInDatabase.addProperty("key", "value");

            SessionInformationHolder sessionInfo = Session.createNewSession(process.getProcess(), userId, userDataInJWT,
                    userDataInDatabase);

            assert sessionInfo.accessToken != null;
            assert sessionInfo.refreshToken != null;

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 1);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }
        {
            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 1);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }
    }

    @Test
    public void checkThatActualDBWorksIfCorrectConfigProduction() throws InterruptedException, StorageQueryException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException,
            IllegalBlockSizeException, StorageTransactionLogicException {
        {
            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            String userId = "userId";
            JsonObject userDataInJWT = new JsonObject();
            userDataInJWT.addProperty("key", "value");
            JsonObject userDataInDatabase = new JsonObject();
            userDataInDatabase.addProperty("key", "value");

            SessionInformationHolder sessionInfo = Session.createNewSession(process.getProcess(), userId, userDataInJWT,
                    userDataInDatabase);

            assert sessionInfo.accessToken != null;
            assert sessionInfo.refreshToken != null;

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 1);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }
        {
            String[] args = { "../" };
            TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STARTED));

            assertEquals(StorageLayer.getSessionStorage(process.getProcess()).getNumberOfSessions(), 1);

            process.kill();
            assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
        }
    }

    @Test
    public void checkThatErrorIsThrownIfIncorrectConfigInProduction() throws IOException, InterruptedException {
        String[] args = { "../" };

        Utils.commentConfigValue("mysql_user");

        TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);

        ProcessState.EventAndException e = process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.INIT_FAILURE, 15000);
        assertNotNull(e);
        TestCase.assertEquals(e.exception.getMessage(),
                "'mysql_user' and 'mysql_connection_uri' are not set. Please set at least one of " + "these values");

        process.kill();
        assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
    }

    @Test
    public void ifForceNoInMemoryThenDevShouldThrowError() throws IOException, InterruptedException {
        String[] args = { "../", "forceNoInMemDB=true" };

        Utils.commentConfigValue("mysql_user");

        TestingProcessManager.TestingProcess process = TestingProcessManager.start(args);

        ProcessState.EventAndException e = process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.INIT_FAILURE, 15000);
        assertNotNull(e);
        TestCase.assertEquals(e.exception.getMessage(),
                "'mysql_user' and 'mysql_connection_uri' are not set. Please set at least one of " + "these values");

        process.kill();
        assertNotNull(process.checkOrWaitForEvent(ProcessState.PROCESS_STATE.STOPPED));
    }
}
