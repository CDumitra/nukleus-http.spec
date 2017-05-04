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

public class ConnectionManagementIT
{
    private final K3poRule k3po = new K3poRule()
            .addScriptRoot("streams", "org/reaktivity/specification/nukleus/http/streams/rfc7230/connection.management")
            .addScriptRoot("http", "org/kaazing/specification/http/rfc7230/connection.management");

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
//      "${http}/response.status.101.with.upgrade/request",
        "${streams}/response.status.101.with.upgrade/server/source",
        "${streams}/response.status.101.with.upgrade/server/nukleus",
        "${streams}/response.status.101.with.upgrade/server/target" })
    public void shouldSwitchProtocolAfterUpgrade() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/multiple.requests/server/source",
        "${streams}/multiple.requests/server/nukleus",
        "${streams}/multiple.requests/server/target" })
    public void shouldAcceptMultipleRequests() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/multiple.requests.same.connection/server/source",
        "${streams}/multiple.requests.same.connection/server/nukleus",
        "${streams}/multiple.requests.same.connection/server/target" })
    public void shouldAcceptMultipleRequestsOnSameConnectoin() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.finish();
    }

    @Test
    @Specification({
    // "${http}/response.status.101.with.upgrade/request",
    "${streams}/response.status.101.with.upgrade/client/source",
    "${streams}/response.status.101.with.upgrade/client/nukleus",
    "${streams}/response.status.101.with.upgrade/client/target" })
    public void shouldSwitchProtocolAfterUpgradeClient() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/multiple.requests/client/source",
        "${streams}/multiple.requests/client/nukleus",
        "${streams}/multiple.requests/client/target" })
    public void shouldIssueMultipleRequests() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }

    @Test
    @Specification({
        "${streams}/multiple.requests.same.connection/client/source",
        "${streams}/multiple.requests.same.connection/client/nukleus",
        "${streams}/multiple.requests.same.connection/client/target" })
    public void shouldIssueMultipleRequestsUsingConnectionPool() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_OUTPUT");
        k3po.notifyBarrier("ROUTED_INPUT");
        k3po.finish();
    }
}
