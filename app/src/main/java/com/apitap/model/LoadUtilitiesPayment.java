package com.apitap.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author Jose Viscaya
 *
 */
public class LoadUtilitiesPayment implements Serializable {

    private static final long serialVersionUID = -341264746820330585L;

    /**
     *
     * @return Key Store
     */
    public InputStream loadCertificate() {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is
        	= cl.getResourceAsStream("assets/client1.bks");
        return is;

    }

    /**
     *
     * @return Key store
     */
    public Properties loadProperties() {
        Properties propeties = new Properties();
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream is
            	= cl.getResourceAsStream("assets/keyStore.properties");
            propeties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            return propeties;
        }
        return propeties;
    }

}