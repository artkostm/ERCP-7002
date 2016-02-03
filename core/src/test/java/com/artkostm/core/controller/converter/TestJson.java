package com.artkostm.core.controller.converter;

import org.junit.Assert;
import org.junit.Test;

import com.artkostm.core.controller.TestObject;
import com.artkostm.core.web.controller.converter.Json;

public class TestJson 
{
    @Test
    public void teastBasic()
    {
        TestObject obj = new TestObject();
        obj.setMessage("testmsg");
        Assert.assertEquals("{\"message\":\"testmsg\",\"num\":0}", Json.toJson(obj).toString());
    }
}
