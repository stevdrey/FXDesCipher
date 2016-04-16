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

import fxdescipher.DialogUtil;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Esta clase contien 2 metodos estaticos que se encarga de realizar el "parseo" un archivo con formato 
 * XML con una estructura especifica a un Objeto Plano de Java.
 *
 * @author srey
 */
public final class ManageXml {
    
    /**
     * Este metodo se encarga de transformar y guardar un objeto plano de java en un archivo xml, 
     * en la ruta especificada por el usuario.
     * 
     * @param cipher
     *                  Esta instancia contiene los datos que se plasmaran en un archivo de tipo xml.
     * 
     * @param target 
     *                  Esta instancia contiene la ruta donde se desea guardar el archivo.
     */
    public static void saveFile(CipherXml cipher, Path target) {
        try {
            JAXBContext ctx= JAXBContext.newInstance(CipherXml.class);
            Marshaller marshaller= ctx.createMarshaller();
            
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            marshaller.marshal(cipher, target.toFile());
            
            DialogUtil.showInfoMessage("Operacion Exitosa", "El archivo fue guardado de forma exitosa!");
        } catch (JAXBException ex) {
            Logger.getLogger(ManageXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo se encarga de leer un archivo de tipo xml y crear una instancia de tipo {@link CipherXml}
     * 
     * @param source
     *                  Instancia que contiene la ruta donde se encentra el archivo xml a leer.
     * 
     * @return 
     *      Retorna una instancia de tipo {@link CipherXml} con los datos guardados anteriormente por un usuario.
     */
    public static CipherXml loadFile(Path source) {
        CipherXml cipher= null;
        
        try {
             JAXBContext ctx= JAXBContext.newInstance(CipherXml.class);
             Unmarshaller unmarshaller= ctx.createUnmarshaller();
             
             cipher= (CipherXml) unmarshaller.unmarshal(source.toFile());
        } catch (JAXBException ex) {
            Logger.getLogger(ManageXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cipher;
    }
}