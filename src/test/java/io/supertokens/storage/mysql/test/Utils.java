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

import io.supertokens.Main;
import io.supertokens.pluginInterface.PluginInterfaceTesting;
import io.supertokens.storage.mysql.Start;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;

abstract class Utils extends Mockito {

    private static ByteArrayOutputStream byteArrayOutputStream;

    static void afterTesting() {
        String installDir = "../";
        try {
            // we remove the license key file
            ProcessBuilder pb = new ProcessBuilder("rm", "licenseKey");
            pb.directory(new File(installDir));
            Process process = pb.start();
            process.waitFor();

            // remove config.yaml file
            pb = new ProcessBuilder("rm", "config.yaml");
            pb.directory(new File(installDir));
            process = pb.start();
            process.waitFor();

            // remove webserver-temp folders created by tomcat
            final File webserverTemp = new File(installDir + "webserver-temp");
            try {
                FileUtils.deleteDirectory(webserverTemp);
            } catch (Exception ignored) {
            }

            // remove .started folder created by processes
            final File dotStartedFolder = new File(installDir + ".started");
            try {
                FileUtils.deleteDirectory(dotStartedFolder);
            } catch (Exception ignored) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void reset() {
        Main.isTesting = true;
        PluginInterfaceTesting.isTesting = true;
        Start.isTesting = true;
        Main.makeConsolePrintSilent = true;
        String installDir = "../";
        try {
            // move from temp folder to installDir
            ProcessBuilder pb = new ProcessBuilder("cp", "temp/licenseKey", "./licenseKey");
            pb.directory(new File(installDir));
            Process process = pb.start();
            process.waitFor();

            pb = new ProcessBuilder("cp", "temp/config.yaml", "./config.yaml");
            pb.directory(new File(installDir));
            process = pb.start();
            process.waitFor();

            TestingProcessManager.killAll();
            TestingProcessManager.deleteAllInformation();
            TestingProcessManager.killAll();

            byteArrayOutputStream = new ByteArrayOutputStream();
            System.setErr(new PrintStream(byteArrayOutputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setValueInConfig(String key, String value) throws FileNotFoundException, IOException {

        String oldStr = "((#\\s)?)" + key + "(:|((:\\s).+))\n";
        String newStr = key + ": " + value + "\n";
        StringBuilder originalFileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("../config.yaml"))) {
            String currentReadingLine = reader.readLine();
            while (currentReadingLine != null) {
                originalFileContent.append(currentReadingLine).append(System.lineSeparator());
                currentReadingLine = reader.readLine();
            }
            String modifiedFileContent = originalFileContent.toString().replaceAll(oldStr, newStr);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("../config.yaml"))) {
                writer.write(modifiedFileContent);
            }
        }
    }

    public static void commentConfigValue(String key) throws IOException {
        String oldStr = "((#\\s)?)" + key + "(:|((:\\s).+))\n";
        String newStr = "# " + key + ":";

        StringBuilder originalFileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("../config.yaml"))) {
            String currentReadingLine = reader.readLine();
            while (currentReadingLine != null) {
                originalFileContent.append(currentReadingLine).append(System.lineSeparator());
                currentReadingLine = reader.readLine();
            }
            String modifiedFileContent = originalFileContent.toString().replaceAll(oldStr, newStr);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("../config.yaml"))) {
                writer.write(modifiedFileContent);
            }
        }

    }

    static TestRule getOnFailure() {
        return new TestWatcher() {
            @Override
            protected void failed(Throwable e, Description description) {
                System.out.println(byteArrayOutputStream.toString(StandardCharsets.UTF_8));
            }
        };
    }

}
