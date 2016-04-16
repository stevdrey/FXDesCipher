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
@XmlRootElement(name = "cipher-xml")
public class CipherXml {
    private CipherParamsXml params;
    private String text;

    public CipherXml(CipherParamsXml params, String text) {
        super();
        
        this.params = params;
        this.text = text;
    }

    public CipherXml() {
        this(null, "");
    }

    public CipherParamsXml getParams() {
        return params;
    }

    @XmlElement
    public void setParams(CipherParamsXml params) {
        this.params = params;
    }

    public String getText() {
        return text;
    }

    @XmlElement
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CipherXml) {
            CipherXml cx= (CipherXml) obj;
            
            return cx.params.equals(this.params) && cx.text.equals(this.text);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.params);
        hash = 89 * hash + Objects.hashCode(this.text);
        return hash;
    }

}