package com.artkostm.core.web.controller.session;

import com.artkostm.core.service.Service;
import com.artkostm.core.service.ServiceManager;

public class SessionService implements Service
{
    private final SessionHandler sessionHandler;
    
    public SessionService() 
    {
        sessionHandler = new SessionHandler();
    }
    
    @Override
    public void run() 
    {
        sessionHandler.update();
    }

    @Override
    public String getName() 
    {
        return ServiceManager.SESSION_SERVICE;
    }

    public SessionHandler getSessionHandler() 
    {
        return sessionHandler;
    }
}
