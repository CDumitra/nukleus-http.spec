Tests in this directory correspond to those in the [k3po http specification](https://github.com/k3po/k3po/tree/develop/specification/http/src/main/scripts/org/kaazing/specification/http/rfc7230/) 
as follows, grouped by subfolder:

### architecture

nukleus-http.spec              | k3po
------------------------------ | ----
request.and.response           | client.must.send.host.identifier
request.header.host.missing    | inbound.must.reject.requests.missing.host.identifier 
request.uri.with.user.info     | inbound.must.reject.requests.with.user.info.on.uri 
request.version.1.2+           | inbound.must.reply.with.version.one.dot.one.when.received.higher.minor.version 
request.version.missing        | inbound.must.send.version
request.uri.with.percent.chars | inbound.should.allow.requests.with.percent.chars.in.uri
request.version.not.1.x        | origin.server.should.send.505.on.major.version.not.equal.to.one
request.version.invalid        | response.must.be.400.on.invalid.version
TODO: response.version.invalid |
response.version.missing       | outbound.must.send.version
TODO: response.version.not.http.1.x

### connection.management

nukleus-http.spec                             | k3po
---------------------------------             | ----
request.with.connection.close                 | client.must.close.connection.after.request.with.connection.close
Covered in request.with.connection.close      | server.must.close.its.half.of.connection.after.sending.response.if.it.receives.a.close
response.with.connection.close                | server.must.close.connection.after.response.with.connection.close
multiple.requests.same.connection             | connections.should.persist.by.default
multiple.requests.pipelined                   | server.should.accept.http.pipelining
multiple.requests.pipelined.with.retry        | client.with.pipelining.must.not.retry.pipelining.immediately.after.failure
first.pipelined.response.has.connection.close | client.must.not.reuse.tcp.connection.when.receives.connection.close
upgrade.request.and.response                  | server.getting.upgrade.request.must.respond.with.upgrade.header
upgrade.request.and.response.with.data        | server.that.is.upgrading.must.send.a.101.response
request.and.upgrade.required.response         | server.that.sends.upgrade.required.must.include.upgrade.header
TODO: proxy.must.not.forward.connection.header| intermediary.must.remove.connection.header.on.forward.request
TBD                                           | proxy.must.not.retry.non.idempotent.requests
TBD                                           | reverse.proxy.connection.established
                                 
### message.format

nukleus-http.spec                       | k3po
--------------------------------------- | ----
request.with.start.line.too.long        | server.should.send.414.to.request.with.too.long.a.request[URI]
request.with.content.length             | inbound.should.process.request.with.content.length
request.with.headers                    | inbound.should.accept.headers
response.with.content.length            | outbound.should.process.response.with.content.length
response.with.headers                   | outbound.should.accept.headers
invalid.request.whitespace.after.start.line | inbound.should.reject.request.with.whitespace.between.start.line.and.first.header
invalid.request.not.http                | non.http.request.to.http.server.should.be.responded.to.with.400
request.with.unimplemented.method       | server.should.send.501.to.unimplemented.methods
request.with.extra.CRLF.after.request.line | robust.server.should.allow.extra.CRLF.after.request.line
request.with.obsolete.line.folding      | server.should.reject.obs.in.header.value
request.with.unknown.transfer.encoding  | server.should.send.501.to.unknown.transfer.encoding
post.request.with.no.content            | client.should.send.content.length.header.in.post.even.if.no.content
head.request.and.response               | head.response.must.not.have.content
head.request.and.response.with.content.length | head.response.must.not.have.content.though.may.have.content.length
request.with.header.value.too.long      | inbound.on.receiving.field.with.length.larger.than.wanting.to.process.must.reply.with.4xx
TBD                                     | inbound.should.reject.invalid.request.line
TBD                                     | server.must.reject.header.with.space.between.header.name.and.colon
TBD                                     | server.must.reject.request.with.multiple.different.content.length
TBD                                     | on.response.proxy.must.remove.space.in.header.with.space.between.header.name.and.colon
TBD                                     | gateway.must.reject.request.with.multiple.different.content.length
TBD                                     | proxy.or.gateway.must.reject.obs.in.header.value
TBD                                     | proxy.should.preserve.unrecongnized.headers
none (covered by architecture/request.and.response) | outbound.should.accept.no.headers
none (covered by architecture/request.and.response) | request.must.start.with.request.line
none (covered by architecture/request.and.response) | server.should.send.status.line.in.start.line

### flow.control

nukleus-http.spec                       | k3po
--------------------------------------- | ----
request.headers.too.long                | server.should.send.414.to.request.with.too.long.a.request[URI]


#### Migration plan for Agrona-based flow control scripts

- multiple.requests.pipelined:
  - removed, covered in connection.management/multiple.requests.pipelined
- multiple.requests.pipelined.fragmented: 
  - same, low level done
- multiple.requests.with.content.length.pipelined.fragmented:

- multiple.requests.with.response.flow.control
                                        | 
- request.flow.controlled:
  - network server script forces client to fragment, application level scripts same as architecture/request.and.response
- request.with.content.flow.controlled:
  - network server script forces client to fragment data, application level server script forces client to fragment data
- request.fragmented: same, done
- request.fragmented.with.content.length: same, low level done
- request.with.content.length.and.end.late.target.window: same, done
request.with.fragmented.content.flow.controlled
request.with.upgrade.and.data
response.first.fragment.maximum.headers
response.flow.controlled
response.fragmented
response.fragmented.with.content.length
response.headers.too.long
response.with.content.flow.controlled
response.with.content.length.and.end.late.target.window
response.with.fragmented.content.flow.controlled
response.with.upgrade.and.data

