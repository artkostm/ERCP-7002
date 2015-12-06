package com.artkostm.core.controller.session;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.artkostm.core.controller.Context;

public class SessionHandler 
{
    private final Map<UUID, Session> sessions = new HashMap<UUID, Session>();

    public Session getSession(final UUID sessionId) 
    {
        return sessions.get(sessionId);
    }

    public Session getSession(final String sessionId) 
    {
        return sessions.get(UUID.fromString(sessionId));
    }

    //TODO: change logic
    public void process(final Context ctx) 
    {
        final List<Cookie> sessionCookieList = ctx.getCookies().get("MYSESSIONID");
        Session session = null;
        
        boolean flag = false;
        if (sessionCookieList != null) 
        {
            for (Cookie sessionCookie : sessionCookieList) 
            {
                if (sessionCookie != null && (session = sessions.get(UUID.fromString(sessionCookie.value()))) != null) 
                {
                    flag = true;
                    break;//looks strange
                }
            }
        }

        if (!flag) 
        {
            final UUID sessionId = UUID.randomUUID();
            session = new Session(sessionId);

            sessions.put(sessionId, session);

            final Cookie sessionCookie = new DefaultCookie("MYSESSIONID", session.getSessionId().toString());
            sessionCookie.setPath("/");
            ctx.getCookies().put(sessionCookie.name(), Arrays.asList(sessionCookie));
        }
        ctx.setSession(session);
    }

    public void update() 
    {
        for (Entry<UUID, Session> entry : sessions.entrySet())
        {
            final Session session = entry.getValue();
            long currentTime = new Date().getTime();
            if (currentTime - session.getUpdateTime() >= session.getTimeout() * 60000) 
            {
                sessions.remove(entry.getKey());
            }
        }
    }
}
