/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.picketlink.idm.impl.fedid;

import org.picketlink.idm.impl.fedid.UUIDPseudonymGenerator;
import org.picketlink.idm.spi.fedid.PseudonymGenerator;

import junit.framework.TestCase;

/**
 * Unit Test the pseudonym generator
 * @author Anil.Saldhana@redhat.com
 * @since Sep 11, 2008
 */
public class PseudonymGeneratorUnitTestCase extends TestCase
{
   public void testUUID()
   {
     PseudonymGenerator pseudoGen = new UUIDPseudonymGenerator();
     String first = pseudoGen.generatePseudonym();
     String second = pseudoGen.generatePseudonym();
     String third = pseudoGen.generatePseudonym();

     assertFalse(first.equals(second));
     assertFalse(third.equals(second));
     assertFalse(first.equals(third));
   }
}