/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.test.impl.execution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.api.RunAsClient;
import org.jboss.arquillian.container.test.impl.execution.ClientTestExecuter;
import org.jboss.arquillian.container.test.impl.execution.event.LocalExecutionEvent;
import org.jboss.arquillian.container.test.impl.execution.event.RemoteExecutionEvent;
import org.jboss.arquillian.container.test.test.AbstractContainerTestTestBase;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.spi.TestMethodExecutor;
import org.jboss.arquillian.spi.client.deployment.DeploymentDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * ClientTestExecuterTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientTestExecuterTestCase extends AbstractContainerTestTestBase
{
   @Mock
   private DeploymentDescription deployment;
   

   /* (non-Javadoc)
    * @see org.jboss.arquillian.core.test.AbstractManagerTestBase#addExtensions(java.util.List)
    */
   @Override
   protected void addExtensions(List<Class<?>> extensions)
   {
      extensions.add(ClientTestExecuter.class);
   }
   
   @Before
   public void bindDeployment()
   {
      bind(ApplicationScoped.class, DeploymentDescription.class, deployment);      
   }
   
   @Test
   public void shouldExecuteRemoteIfDeploymentIsTestable() throws Exception
   {
      when(deployment.testable()).thenReturn(true);
      
      fire(test("methodLevelRunModeDefault", new ClassLevelRunModeDefault()));
      
      assertEventFired(RemoteExecutionEvent.class, 1);
   }

   @Test
   public void shouldExecuteLocalIfDeploymentIsNotTestable() throws Exception
   {
      when(deployment.testable()).thenReturn(false);
      
      fire(test("methodLevelRunModeDefault", new ClassLevelRunModeDefault()));
      
      assertEventFired(LocalExecutionEvent.class, 1);
   }

   @Test
   public void shouldExecuteLocalIfDeploymentIsTestableAndClassRunModeAsClient() throws Exception
   {
      when(deployment.testable()).thenReturn(true);

      fire(test("methodLevelRunModeDefault", new ClassLevelRunModeAsClient()));
      
      assertEventFired(LocalExecutionEvent.class, 1);
   }

   @Test
   public void shouldExecuteLocalIfDeploymentIsTestableAndMethodRunModeAsClient() throws Exception
   {
      when(deployment.testable()).thenReturn(true);
      
      fire(test("methodLevelRunModeAsClient", new ClassLevelRunModeDefault()));
      
      assertEventFired(LocalExecutionEvent.class, 1);
   }

   private org.jboss.arquillian.spi.event.suite.Test test(String testMethodName, Object obj) throws Exception
   {
      TestMethodExecutor executor = mock(TestMethodExecutor.class);
      when(executor.getInstance()).thenReturn(obj);
      when(executor.getMethod()).thenReturn(method(testMethodName));

      return new org.jboss.arquillian.spi.event.suite.Test(
            executor
      );
   }
   
   private Method method(String name) throws Exception
   {
      return this.getClass().getDeclaredMethod(name);
   }
   
   private static class ClassLevelRunModeDefault { }

   @RunAsClient
   private static class ClassLevelRunModeAsClient { }
   
   @SuppressWarnings("unused")
   private void methodLevelRunModeDefault() {}

   @SuppressWarnings("unused")
   private void methodLevelRunModeInContainer() {}

   @SuppressWarnings("unused")
   @RunAsClient
   private void methodLevelRunModeAsClient() {}
}
