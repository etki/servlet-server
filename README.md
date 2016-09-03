# Java 8'd embedded servlet servers

This project contains unified interface for embedded servlet server.
In a shiny, hipstatic CompletableFuture wrap.

A self-deploying HTTP application is always better than just 
HTTP-application. Instead of putting your application in a commonly used
server, you can put server into your application and bring them together
wherever you want, would it be a container or 12-factor app meetup. This
project aims to provide you with such superpower and let you switch your
server implementation whenever you feel to.

## How do i use it?

The project consists of core part (abstract classes and interface) and
implementations (Jetty, Tomcat, Glassfish). To use it, you'll need at
least core and one of implementations. After that, you'll need to create
concrete `AbstractServletServer` child and use it the way you want.

Currently i haven't pushed anything to public repos. However, you can
always build project yourself.

## Notable exceptions

This project doesn't support the natural way of servlet API. Following
rules apply:

- Servlets are always deployed with `/*` mapping specified. That's an 
official hardcode, bang.
- Servlets are identified by context paths they are deployed with, not 
by names. In fact, names are set to context paths to prevent collisions.
Honestly, that shouldn't bother you while you're using embedded servers.
- Filters are set on by-servlet basis. However, you can always put same 
`Filter` instance in several deployments.
- Currently, this library follows one-off principle: you can't reuse 
server after shutdown call. However, more permissive system may be 
implemented one day.
- You have to manage thread executors yourself; moreover, there is no 
way to notify the library that underlying thread executor has been shut 
down, so you have to stop it only after you've done working with 
library. That shouldn't be a big problem, though.
- You have to deploy a servlet or explicitly call `.start()` method to
create a listening socket.

Most of the wtf-from-servlet-API-perspective-things are implemented to
provide clear "operation impossible" method returns instead of broad set
of moody exceptions.

## WTFPL

Formally this code is protected by MIT, but be pleased to use it the way
you feel.