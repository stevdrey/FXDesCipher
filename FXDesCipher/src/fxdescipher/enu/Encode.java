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
package fxdescipher.enu;

import java.util.stream.Stream;

/**
 *
 * @author srey
 */
public enum Encode {
    BASE64("base64"), BINARY("binary"), HEXADECIMAL("hex"), NULL("null");
    
    private final String encode;

    private Encode(String encode) {
        this.encode = encode;
    }

    public String getEncode() {
        return encode;
    }
 
    public static Encode fromName(String encode) {
        return Stream.of(Encode.values()).
                filter(e -> e.encode.equals(encode)).
                findFirst().
                orElse(NULL);
    }
}