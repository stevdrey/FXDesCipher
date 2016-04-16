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
package fxdescipher;

import fxdescipher.enu.Algorithm;
import fxdescipher.enu.Encode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author srey
 */
public class DesCipher {
    private Algorithm algorithm;
    private String mode;
    private boolean padding;
    
    private final byte[] IV = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x1E, 0x72, 0x6F, 0x5B };

    public DesCipher(Algorithm algorithm, String mode, boolean padding) {
        super();
        
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }

    public DesCipher(Algorithm algorithm, String mode) {
        this(algorithm, mode, false);
    }

    public DesCipher() {
        this(Algorithm.DES, "ECB", false);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isPadding() {
        return padding;
    }

    public void setPadding(boolean padding) {
        this.padding = padding;
    }
    
    public String encrypt(String key, String text, Encode encode) {
        StringBuilder sb= new StringBuilder();
        byte[] cipherText= null;
        
        try {
            Cipher cipher= this.getCipher(key, Cipher.ENCRYPT_MODE);
            
            cipherText= cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            
            switch (encode) {
                case BASE64:
                    sb.append(Base64.getMimeEncoder().encodeToString(cipherText));
                    
                    break;
                    
                case BINARY:
                    this.binaryEncode(cipherText, sb);
                    
                    break;
                    
                case HEXADECIMAL:
                    this.hexEncode(cipherText, sb);
                    
                    break;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | 
                NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | 
                BadPaddingException ex) {
            Logger.getLogger(DesCipher.class.getName()).log(Level.SEVERE, null, ex); 
        }
        
        return sb.toString();
    }
    
    public String decrypt(String key, String text, Encode encode) {
        String sb= "";
        byte[] decode= null;
        
        switch (encode) {
            case BASE64:
                decode= Base64.getMimeDecoder().decode(text);
                
                break;
                
            case BINARY:
                decode= this.binaryDecode(text).getBytes(StandardCharsets.UTF_8);
                
                break;
                
            case HEXADECIMAL:
                decode= this.hexDecode(text).getBytes(StandardCharsets.UTF_8);
                
                break;
        }
        
        try {
            Cipher cipher= this.getCipher(key, Cipher.DECRYPT_MODE);
                        
            sb= new String(cipher.doFinal(decode));
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | 
                NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | 
                BadPaddingException ex) {
            Logger.getLogger(DesCipher.class.getName()).log(Level.SEVERE, null, ex); 
        }
        
        return sb;
    }
    
    // section of private methods
    
    /**
     * Este metodo se encarga de codificar un arreglo de bytes en texto hexadecimal.
     * 
     * @param buf
     *                  Arreglo que contiene los datos a codificar.
     * 
     * @param sb
     *                  Instancia a la cual se le concatenaran los datos codificados en hexadecimal.
     * 
     * @return 
     *          Retorna una instancia que implementa {@link Appendable}, la cual contendra todo el texto codificado
     *          en hexadecimal.
     */
    private Appendable hexEncode(byte buf[], Appendable sb)    {   
        
        for (int i = 0; i < buf.length; i++) {   
            try {
                int low = buf[i] & 0xF;
                int high = (buf[i] >> 8) & 0xF;

                sb.append(Character.forDigit(high, 16)).
                        append(Character.forDigit(low, 16)).
                        append(" ");
            } catch (IOException ex) {
                Logger.getLogger(DesCipher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    
        return sb;   
    }
    
    /**
     * Este metodo se encarga de codificar los datos contendios en un arreglo de bytes a su representacionsb
     * binaria.
     * 
     * @param buffred
     *                  Arreglo que contiene los datos a codificar.
     * 
     * @param sb
     *                  Instancia a la cual se le concatenaran los datos codificados en binario.
     * 
     * @return 
     *          Retorna una instancia que implementa {@link Appendable}, la cual contendra todo el texto codificado
     *          en binaria.
     */
    private Appendable binaryEncode(byte[] buffred, Appendable sb) {
        
        try {
            for (byte b : buffred)  {
                int val = b;
                
                for (int i = 0; i < 8; i++) {
                    sb.append((val & 128) == 0 ? "0" : "1");
                    val <<= 1;
                }
                    
                sb.append(" ");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DesCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sb;
    }
    
    private String hexDecode(String hexValue) {
        StringBuilder output = new StringBuilder();
        
        hexValue= hexValue.replaceAll(" ", "");
        
        for (int i = 0; i < hexValue.length(); i += 2) 
           output.append((char) Integer.parseInt(hexValue.substring(i, i + 2), 16));
        
        return output.toString();
    }
    
    private String binaryDecode(String binaryValue) {
        StringBuilder result= new StringBuilder();
        List<Integer> numbers= Stream.of(binaryValue.split(" ")).map(block -> {
            int[] mapping= {128, 64, 32, 16, 8, 4, 2, 1};
            
            return IntStream.range(0, block.length()).reduce(0, (o, n) -> {
                if (block.charAt(n) == '1')
                    o += mapping[n];
                
                return o;
            });
        }).collect(Collectors.toList());
        
        if (numbers != null && numbers.size() > 0)
            numbers.forEach(x -> result.append(Character.toChars(x)));
        
        return result.toString();
    }
    
    private SecretKey getSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] keyBytes= key.getBytes(StandardCharsets.UTF_8);
        SecretKeyFactory keyFactory= SecretKeyFactory.getInstance(this.algorithm.getAlgorithm());
        
        if (this.algorithm == Algorithm.DES)
            return keyFactory.generateSecret(new DESKeySpec(keyBytes));
        
        return keyFactory.generateSecret(new DESedeKeySpec(keyBytes));
    }
    
    private String getTransformation() {
        if (this.padding)
            return String.format("%s/%s/%s", this.algorithm.getAlgorithm(), this.mode, 
                    "PKCS5Padding");
        
        return String.format("%s/%s", this.algorithm.getAlgorithm(), this.mode);
    }
    
    /**
     * Este metodo se encarga de crear e inicialiar una instancia de tipo {@link Cipher} sengun 
     * sus parametros y atributos de esta clase.
     * 
     * @param key
     *                  Instancia que contien los caracteres que conforman la contraceña a usar para cifrar el texto plano.
     * 
     * @param mode
     *                  Indica si la operacion que se desea realizar es encriptar o desencriptar.
     * 
     * @return
     *          Retorna una instancia de tipo {@link Cipher} y lo inicializa de acuerdo con sus parametros y atributos 
     *          de esta clase.
     * 
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException 
     */
    private Cipher getCipher(String key, int mode) throws NoSuchAlgorithmException, InvalidKeySpecException, 
            InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        SecretKey secretKey= this.getSecretKey(key);
        Cipher cipher= Cipher.getInstance(this.getTransformation());

        if (this.mode.equals("ECB"))
            cipher.init(mode, secretKey);

        else 
            cipher.init(mode, secretKey, new IvParameterSpec(IV));
        
        return cipher;
    }
}