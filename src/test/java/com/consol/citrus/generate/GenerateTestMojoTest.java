/*
 * Copyright 2006-2018 the original author or authors.
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

package com.consol.citrus.generate;

import com.consol.citrus.GenerateTestMojo;
import com.consol.citrus.config.tests.SwaggerConfiguration;
import com.consol.citrus.config.tests.TestConfiguration;
import com.consol.citrus.config.tests.WsdlConfiguration;
import com.consol.citrus.config.tests.XsdConfiguration;
import com.consol.citrus.generate.UnitFramework;
import com.consol.citrus.generate.javadsl.JavaDslTestGenerator;
import com.consol.citrus.generate.javadsl.SwaggerJavaTestGenerator;
import com.consol.citrus.generate.javadsl.WsdlJavaTestGenerator;
import com.consol.citrus.generate.javadsl.XsdJavaTestGenerator;
import com.consol.citrus.generate.xml.SwaggerXmlTestGenerator;
import com.consol.citrus.generate.xml.WsdlXmlTestGenerator;
import com.consol.citrus.generate.xml.XmlTestGenerator;
import com.consol.citrus.generate.xml.XsdXmlTestGenerator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @author Christoph Deppisch
 */
public class GenerateTestMojoTest {

    private XmlTestGenerator xmlTestGenerator = Mockito.mock(XmlTestGenerator.class);
    private XsdXmlTestGenerator xsdXmlTestGenerator = Mockito.mock(XsdXmlTestGenerator.class);
    private WsdlXmlTestGenerator wsdlXmlTestGenerator = Mockito.mock(WsdlXmlTestGenerator.class);
    private SwaggerXmlTestGenerator swaggerXmlTestGenerator = Mockito.mock(SwaggerXmlTestGenerator.class);

    private JavaDslTestGenerator javaTestGenerator = Mockito.mock(JavaDslTestGenerator.class);
    private XsdJavaTestGenerator xsdJavaTestGenerator = Mockito.mock(XsdJavaTestGenerator.class);
    private WsdlJavaTestGenerator wsdlJavaTestGenerator = Mockito.mock(WsdlJavaTestGenerator.class);
    private SwaggerJavaTestGenerator swaggerJavaTestGenerator = Mockito.mock(SwaggerJavaTestGenerator.class);

    private GenerateTestMojo mojo;
    
    @BeforeMethod
    public void setup() {
        mojo = new GenerateTestMojo(xmlTestGenerator,
                                    xsdXmlTestGenerator,
                                    wsdlXmlTestGenerator,
                                    swaggerXmlTestGenerator,
                                    javaTestGenerator,
                                    xsdJavaTestGenerator,
                                    wsdlJavaTestGenerator,
                                    swaggerJavaTestGenerator);

        mojo.setType("xml");
    }
    
    @Test
    public void testCreate() throws PrompterException, MojoExecutionException, MojoFailureException {
        reset(xmlTestGenerator);

        TestConfiguration configuration = new TestConfiguration();
        configuration.setName("FooTest");
        configuration.setAuthor("UnknownAuthor");
        configuration.setDescription("TODO");
        configuration.setPackageName("com.consol.citrus.foo");

        when(xmlTestGenerator.withFramework(UnitFramework.TESTNG)).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.withDisabled(false)).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.withAuthor("UnknownAuthor")).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.withDescription("TODO")).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.usePackage("com.consol.citrus.foo")).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.withName("FooTest")).thenReturn(xmlTestGenerator);
        when(xmlTestGenerator.useSrcDirectory("target/generated/citrus")).thenReturn(xmlTestGenerator);

        mojo.setTests(Collections.singletonList(configuration));

        mojo.execute();

        verify(xmlTestGenerator).create();
    }

    @Test
    public void testSuiteFromXsd() throws MojoExecutionException, PrompterException, MojoFailureException {
        reset(xsdXmlTestGenerator);

        TestConfiguration configuration = new TestConfiguration();
        configuration.setName("BookStore");
        configuration.setAuthor("UnknownAuthor");
        configuration.setDescription("TODO");
        configuration.setPackageName("com.consol.citrus.xsd");

        XsdConfiguration xsdConfiguration = new XsdConfiguration();
        xsdConfiguration.setFile("classpath:xsd/BookStore.xsd");
        xsdConfiguration.setRequest("BookRequest");
        xsdConfiguration.setResponse("BookResponse");
        configuration.setXsd(xsdConfiguration);

        when(xsdXmlTestGenerator.withFramework(UnitFramework.TESTNG)).thenReturn(xsdXmlTestGenerator);
        when(xsdXmlTestGenerator.withDisabled(false)).thenReturn(xsdXmlTestGenerator);
        when(xsdXmlTestGenerator.withAuthor("UnknownAuthor")).thenReturn(xsdXmlTestGenerator);
        when(xsdXmlTestGenerator.withDescription("TODO")).thenReturn(xsdXmlTestGenerator);
        when(xsdXmlTestGenerator.usePackage("com.consol.citrus.xsd")).thenReturn(xsdXmlTestGenerator);

        when(xsdXmlTestGenerator.withXsd("classpath:xsd/BookStore.xsd")).thenReturn(xsdXmlTestGenerator);

        when(xsdXmlTestGenerator.withName("BookStore")).thenReturn(xsdXmlTestGenerator);
        when(xsdXmlTestGenerator.useSrcDirectory("target/generated/citrus")).thenReturn(xsdXmlTestGenerator);

        mojo.setTests(Collections.singletonList(configuration));

        mojo.execute();

        verify(xsdXmlTestGenerator).create();
        verify(xsdXmlTestGenerator).withXsd("classpath:xsd/BookStore.xsd");
        verify(xsdXmlTestGenerator).withRequestMessage("BookRequest");
        verify(xsdXmlTestGenerator).withResponseMessage("BookResponse");
    }

    @Test
    public void testSuiteFromWsdl() throws MojoExecutionException, PrompterException, MojoFailureException {
        reset(wsdlXmlTestGenerator);

        TestConfiguration configuration = new TestConfiguration();
        configuration.setName("BookStore");
        configuration.setAuthor("UnknownAuthor");
        configuration.setDescription("TODO");
        configuration.setPackageName("com.consol.citrus.wsdl");
        configuration.setSuffix("_Test");

        WsdlConfiguration wsdlConfiguration = new WsdlConfiguration();
        wsdlConfiguration.setFile("classpath:wsdl/BookStore.wsdl");
        configuration.setWsdl(wsdlConfiguration);

        when(wsdlXmlTestGenerator.withFramework(UnitFramework.TESTNG)).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.withDisabled(false)).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.withAuthor("UnknownAuthor")).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.withDescription("TODO")).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.usePackage("com.consol.citrus.wsdl")).thenReturn(wsdlXmlTestGenerator);

        when(wsdlXmlTestGenerator.withWsdl("classpath:wsdl/BookStore.wsdl")).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.withNameSuffix("_Test")).thenReturn(wsdlXmlTestGenerator);

        when(wsdlXmlTestGenerator.withName("BookStore")).thenReturn(wsdlXmlTestGenerator);
        when(wsdlXmlTestGenerator.useSrcDirectory("target/generated/citrus")).thenReturn(wsdlXmlTestGenerator);

        mojo.setTests(Collections.singletonList(configuration));

        mojo.execute();

        verify(wsdlXmlTestGenerator).create();
        verify(wsdlXmlTestGenerator).withWsdl("classpath:wsdl/BookStore.wsdl");
        verify(wsdlXmlTestGenerator).withNameSuffix("_Test");
    }
    
    @Test
    public void testSuiteFromSwagger() throws MojoExecutionException, PrompterException, MojoFailureException {
        reset(swaggerXmlTestGenerator);

        TestConfiguration configuration = new TestConfiguration();
        configuration.setName("UserLoginService");
        configuration.setAuthor("UnknownAuthor");
        configuration.setDescription("TODO");
        configuration.setPackageName("com.consol.citrus.swagger");
        configuration.setSuffix("_IT");

        SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();
        swaggerConfiguration.setFile("classpath:swagger/user-login-api.json");
        configuration.setSwagger(swaggerConfiguration);

        when(swaggerXmlTestGenerator.withFramework(UnitFramework.TESTNG)).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.withDisabled(false)).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.withAuthor("UnknownAuthor")).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.withDescription("TODO")).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.usePackage("com.consol.citrus.swagger")).thenReturn(swaggerXmlTestGenerator);

        when(swaggerXmlTestGenerator.withSpec("classpath:swagger/user-login-api.json")).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.withNameSuffix("_Test")).thenReturn(swaggerXmlTestGenerator);

        when(swaggerXmlTestGenerator.withName("UserLoginService")).thenReturn(swaggerXmlTestGenerator);
        when(swaggerXmlTestGenerator.useSrcDirectory("target/generated/citrus")).thenReturn(swaggerXmlTestGenerator);

        mojo.setTests(Collections.singletonList(configuration));

        mojo.execute();

        verify(swaggerXmlTestGenerator).create();
        verify(swaggerXmlTestGenerator).withSpec("classpath:swagger/user-login-api.json");
        verify(swaggerXmlTestGenerator).withNameSuffix("_IT");
    }
}
