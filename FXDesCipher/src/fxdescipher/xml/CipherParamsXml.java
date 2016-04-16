/*
 * Copyright (C) 2016 srey
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package fxdescipher.xml;

import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author srey
 */
@XmlRootElement(name = "params")
public class CipherParamsXml {
    private String algorithm;
    private String key;
    private String mode;

    public CipherParamsXml(String algorithm, String key, String mode) {
        super();
        
        this.algorithm = algorithm;
        this.key = key;
        this.mode = mode;
    }

    public CipherParamsXml() {
        this("", "", "");
    }

    public String getAlgorithm() {
        return algorithm;
    }

    @XmlElement
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getKey() {
        return key;
    }

    @XmlElement
    public void setKey(String key) {
        this.key = key;
    }

    public String getMode() {
        return mode;
    }

    @XmlElement
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CipherParamsXml) {
            CipherParamsXml cpx= (CipherParamsXml) obj;
            
            return cpx.key.equals(this.key) && cpx.algorithm.equals(this.algorithm) && cpx.mode.equals(this.mode);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.algorithm);
        hash = 79 * hash + Objects.hashCode(this.key);
        hash = 79 * hash + Objects.hashCode(this.mode);
        return hash;
    }
    
}