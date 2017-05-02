/**
 * Copyright 2016-2017 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.specification.nukleus.http.streams.rfc7230;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.reaktivity.specification.nukleus.NukleusRule;

public class MessageFormatIT
{
    private final K3poRule k3po = new K3poRule()
            .addScriptRoot("streams", "org/reaktivity/specification/nukleus/http/streams/rfc7230/message.format")
            .addScriptRoot("http", "org/kaazing/specification/http/rfc7230/message.format");

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    private final NukleusRule nukleus = new NukleusRule()
        .directory("target/nukleus-itests")
        .streams("http", "source")
        .streams("source", "http#source")
        .streams("target", "http#source")
        .streams("http", "target")
        .streams("source", "http#target");

    @Rule
    public final TestRule chain = outerRule(nukleus).around(k3po).around(timeout);

    @Test
    @Specification({
        "${streams}/request.with.content.length/server/source",
        "${streams}/request.with.content.length/server/nukleus",
        "${streams}/request.with.content.length/server/target" })
    public void shouldAcceptRequestWithContentLength() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/request.with.headers/server/source",
        "${streams}/request.with.headers/server/nukleus",
        "${streams}/request.with.headers/server/target" })
    public void shouldAcceptRequestWithHeaders() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/response.with.content.length/server/source",
        "${streams}/response.with.content.length/server/nukleus",
        "${streams}/response.with.content.length/server/target" })
    public void shouldWriteResponseWithContentLength() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/response.with.content.length/client/source",
        "${streams}/response.with.content.length/client/nukleus",
        "${streams}/response.with.content.length/client/target" })
    public void shouldAcceptResponseWithContentLength() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/response.with.headers/client/source",
        "${streams}/response.with.headers/client/nukleus",
        "${streams}/response.with.headers/client/target" })
    public void shouldAcceptResponseWithHeaders() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/request.with.headers/client/source",
        "${streams}/request.with.headers/client/nukleus",
        "${streams}/request.with.headers/client/target" })
    public void shouldWriteRequestWithHeaders() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/request.with.content.length/client/source",
        "${streams}/request.with.content.length/client/nukleus",
        "${streams}/request.with.content.length/client/target" })
    public void shouldWriteRequestWithContentLength() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }
}
