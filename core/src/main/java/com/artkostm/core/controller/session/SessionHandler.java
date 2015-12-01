package com.artkostm.core.controller.session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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

//    public void process(Context ctx) {
//        List<Cookie> sessionCookieList = ctx.getRequest().getCookiesDownload().get("FUGASESSIONID");
//        Session session = null;
//        
//        boolean flag = false;
//        if (sessionCookieList != null) {
//            for (Cookie sessionCookie : sessionCookieList) {
//                if (sessionCookie != null && (session = sessions.get(UUID.fromString(sessionCookie.value()))) != null) {
//                    flag = true;
//                    break;
//                }
//            }
//        }
//
//        if (!flag) {
//            UUID sessionId = UUID.randomUUID();
//            session = new Session(sessionId);
//
//            sessions.put(sessionId, session);
//
//            DefaultCookie sessionCookie = new DefaultCookie("FUGASESSIONID", session.getSessionId().toString());
//            sessionCookie.setPath("/");
//            ctx.getRequest().setCookie(sessionCookie);
//        }
//        ctx.setSession(session);
//    }

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
