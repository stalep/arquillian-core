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
package org.jboss.arquillian.container.test.impl.enricher.resource;

import javax.naming.InitialContext;

import org.jboss.arquillian.api.ArquillianResource;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

/**
 * InitialContextProvider
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class InitialContextProvider implements ResourceProvider
{
   @Inject
   private Instance<InitialContext> initialContext;
   
   /* (non-Javadoc)
    * @see org.jboss.arquillian.testenricher.arquillian.ResourceProvider#lookup(org.jboss.arquillian.api.ArquillianResource)
    */
   @Override
   public Object lookup(ArquillianResource resource)
   {
      return initialContext.get();
   }

}
