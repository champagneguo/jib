/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.jib.gradle;

import com.google.cloud.tools.jib.plugins.common.AuthProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/** Test for {@link GradleRawConfiguration}. */
public class GradleRawConfigurationTest {

  @Test
  public void testGetters() {
    JibExtension jibExtension = Mockito.mock(JibExtension.class);

    AuthParameters authParameters = Mockito.mock(AuthParameters.class);
    BaseImageParameters baseImageParameters = Mockito.mock(BaseImageParameters.class);
    ContainerParameters containerParameters = Mockito.mock(ContainerParameters.class);

    Mockito.when(authParameters.getUsername()).thenReturn("user");
    Mockito.when(authParameters.getPassword()).thenReturn("password");
    Mockito.when(authParameters.getPropertyDescriptor()).thenReturn("auth source");
    Mockito.when(authParameters.getUsernamePropertyDescriptor()).thenReturn("from.auth.username");
    Mockito.when(authParameters.getPasswordPropertyDescriptor()).thenReturn("<to><auth><password>");

    Mockito.when(jibExtension.getFrom()).thenReturn(baseImageParameters);
    Mockito.when(jibExtension.getContainer()).thenReturn(containerParameters);

    Mockito.when(baseImageParameters.getCredHelper()).thenReturn("gcr");
    Mockito.when(baseImageParameters.getImage()).thenReturn("openjdk:15");
    Mockito.when(baseImageParameters.getAuth()).thenReturn(authParameters);

    Mockito.when(containerParameters.getAppRoot()).thenReturn("/app/root");
    Mockito.when(containerParameters.getEntrypoint()).thenReturn(Arrays.asList("java", "Main"));
    Mockito.when(containerParameters.getEnvironment())
        .thenReturn(new HashMap<>(ImmutableMap.of("currency", "dollar")));
    Mockito.when(containerParameters.getJvmFlags()).thenReturn(Arrays.asList("-cp", "."));
    Mockito.when(containerParameters.getLabels())
        .thenReturn(new HashMap<>(ImmutableMap.of("unit", "cm")));
    Mockito.when(containerParameters.getMainClass()).thenReturn("com.example.Main");
    Mockito.when(containerParameters.getPorts()).thenReturn(Arrays.asList("80/tcp", "0"));
    Mockito.when(containerParameters.getArgs()).thenReturn(Arrays.asList("--log", "info"));
    Mockito.when(containerParameters.getUseCurrentTimestamp()).thenReturn(true);
    Mockito.when(containerParameters.getUser()).thenReturn("admin:wheel");

    GradleRawConfiguration rawConfiguration = new GradleRawConfiguration(jibExtension);

    AuthProperty fromAuth = rawConfiguration.getFromAuth();
    Assert.assertEquals("user", fromAuth.getUsername());
    Assert.assertEquals("password", fromAuth.getPassword());
    Assert.assertEquals("auth source", fromAuth.getPropertyDescriptor());
    Assert.assertEquals("from.auth.username", fromAuth.getUsernamePropertyDescriptor());
    Assert.assertEquals("<to><auth><password>", fromAuth.getPasswordPropertyDescriptor());

    Assert.assertEquals(Arrays.asList("java", "Main"), rawConfiguration.getEntrypoint());
    Assert.assertEquals(
        new HashMap<>(ImmutableMap.of("currency", "dollar")), rawConfiguration.getEnvironment());
    Assert.assertEquals("/app/root", rawConfiguration.getAppRoot());
    Assert.assertEquals("gcr", rawConfiguration.getFromCredHelper());
    Assert.assertEquals("openjdk:15", rawConfiguration.getFromImage());
    Assert.assertEquals(Arrays.asList("-cp", "."), rawConfiguration.getJvmFlags());
    Assert.assertEquals(new HashMap<>(ImmutableMap.of("unit", "cm")), rawConfiguration.getLabels());
    Assert.assertEquals("com.example.Main", rawConfiguration.getMainClass());
    Assert.assertEquals(Arrays.asList("80/tcp", "0"), rawConfiguration.getPorts());
    Assert.assertEquals(Arrays.asList("--log", "info"), rawConfiguration.getProgramArguments());
    Assert.assertTrue(rawConfiguration.getUseCurrentTimestamp());
    Assert.assertEquals("admin:wheel", rawConfiguration.getUser());
  }
}
