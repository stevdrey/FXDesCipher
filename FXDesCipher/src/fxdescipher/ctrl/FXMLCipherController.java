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
package fxdescipher.ctrl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fxdescipher.DesCipher;
import fxdescipher.DialogUtil;
import fxdescipher.enu.Algorithm;
import fxdescipher.enu.Encode;
import fxdescipher.xml.CipherParamsXml;
import fxdescipher.xml.CipherXml;
import fxdescipher.xml.ManageXml;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

/**
 * Esta clase funciona como controlador para la vista (archio xml llamdo: FXMLCipher.xml), el cual se encarga
 * de la interaccion entre la aplicacion y el usuario.
 *
 * @author srey
 */
public class FXMLCipherController implements Initializable {
    
    @FXML
    private GridPane gpn_formContainer;
    @FXML
    private Label lbl_key;
    @FXML
    private Label lbl_mode;
    @FXML
    private Label lbl_function;
    @FXML
    private Label lbl_text;
    @FXML
    private Label lbl_path;
    @FXML
    private TextField txt_key;
    @FXML
    private Pane pn_modeContainer;
    @FXML
    private RadioButton rb_ecb;
    @FXML
    private ToggleGroup tgrb_mode;
    @FXML
    private Tooltip tip_ecb;
    @FXML
    private RadioButton rb_cbc;
    @FXML
    private Tooltip tip_cbc;
    @FXML
    private RadioButton rb_cfb;
    @FXML
    private Tooltip tip_cfb;
    @FXML
    private Pane pn_function;
    @FXML
    private RadioButton rb_encrypt;
    @FXML
    private ToggleGroup tgrb_function;
    @FXML
    private RadioButton rb_decrypt;
    @FXML
    private Label lbl_algorithm;
    @FXML
    private Pane pn_algorithmContainer;
    @FXML
    private RadioButton rb_des;
    @FXML
    private ToggleGroup tgrb_algorithm;
    @FXML
    private RadioButton rb_tripleDes;
    @FXML
    private TextArea txta_text;
    @FXML
    private TextField txt_path;
    @FXML
    private Button btn_generateKey;
    @FXML
    private Button btn_explorer;
    @FXML
    private CheckBox ckb_text;
    @FXML
    private Button btn_explorerConfig;
    @FXML
    private TextArea txta_result;
    @FXML
    private Button btn_start;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_save;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.btn_clear.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ERASER));
        this.btn_explorer.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FILE));
        this.btn_explorerConfig.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FILE));
        this.btn_generateKey.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
        this.btn_save.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SAVE));
        this.btn_start.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.LOCK));
        
        this.ckb_text.selectedProperty().addListener((objs, oldValue, newValue) -> {
            this.txt_path.setDisable(newValue);
            this.btn_explorer.setDisable(newValue);
            
            this.txta_text.setDisable(!newValue);
        });
        
        this.rb_decrypt.selectedProperty().addListener((objs, oldValue, newValue) -> {
            this.setDisableEncryptControlls(newValue);
            
            this.btn_explorerConfig.setVisible(newValue);
        });
    }    
    
    // section of private methods
    
    /**
     * Este metodo se encarga de generar una clave criptograficamente aleatoria para el usuario.
     * 
     * @return 
     *      Retorna una cadena de caracteres de forma criptograficamente aleatorea, de modo que funcione comoo contraceña
     *      para cifrar el texto plano.
     */
    private String getRandomKey() {
        StringBuilder randomKey= new StringBuilder(10);
        SecureRandom random= new SecureRandom();
        
        int keySize= this.rb_des.isSelected() ? 10 : 24;
        char[] characters= {
            'a', 'b', 'c', 'd', 'e', 'f', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'J', 'K', 'L', 'M', 'N', 'O', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '!', '@', '#', '$', '%', '&', '/', '(', ')', '¡', '?', ';', '.', '*', '_', '+', '-', '<', '>', ','
        };
        
        for (int i=0; i < keySize; i++ )
            randomKey.append(characters[random.nextInt(characters.length)]);
        
        return randomKey.toString();
    }
    
    /**
     * Este metodo selecciona los valores por defecto dentro de los parametros que tiene el usuario 
     * para realizar la encripcion o desencripcion.
     */
    private void defaultValues() {
        this.rb_ecb.setSelected(true);
        this.rb_encrypt.setSelected(true);
        this.rb_des.setSelected(true);
        
        this.ckb_text.setSelected(true);
    }
    
    /**
     * Este metodo se encraga de limpiar uno o todos los controles de la pantalla, 
     * segun se especifique en el parametro.
     * 
     * @param all 
     *                  Indica si se desea limpiar todos los controles o no.
     */
    private void clearControlls(boolean all) {
        if (all) {
            this.defaultValues();
            
            this.txt_key.clear();
            this.txt_path.clear();
            this.txta_text.clear();
        }
        
        this.txta_result.clear();
    }
    
    /**
     * Este metodo se encraga de habilitar o deshabilitar los controles de los parametros requeridos
     * para poder cifrar un texto plano.
     * 
     * @param disable 
     *                  Indica si se desea habilitar o deshabilitar los controles.
     */
    private void setDisableEncryptControlls(boolean disable) {
        this.txt_key.setDisable(disable);
        
        this.btn_generateKey.setDisable(disable);
        
        this.rb_cbc.setDisable(disable);
        this.rb_cfb.setDisable(disable);
        this.rb_des.setDisable(disable);
        this.rb_ecb.setDisable(disable);
        this.rb_tripleDes.setDisable(disable);
    }
    
    /**
     * Este metodo se encarga de validar los campos requeridos para poder realizar el cifrado del texto palno.
     * 
     * @return 
     *      Retorna un {@code true} en caso que la validación sea satisfactoria, en caso contrario retorna un {@code false}.
     */
    private boolean validateInpust() {
        if (this.txt_key.getText().isEmpty()) {
            DialogUtil.showWarning("Falta la clave", 
                    "Debe de ingresar una clave o bien puede pulsar sobre el botón de generar una clave");
            
            this.txt_key.requestFocus();
            return false;
            
        } else if (this.ckb_text.isSelected() && this.txta_text.getText().isEmpty()) {
            DialogUtil.showWarning("Falta el texto a Cifrar", 
                    "Debe de ingresar un texto cualquiera que desee cifrar con alguno de los algortitmos seleccionados");
            
            this.txta_text.requestFocus();
            return false;
            
        } else if  ((!this.ckb_text.isSelected() && this.txt_path.getText().isEmpty()) ||
                (!this.ckb_text.isSelected() && !Files.exists(Paths.get(this.txt_path.getText()), LinkOption.NOFOLLOW_LINKS))){
            DialogUtil.showWarning("Falta la ruta del archivo", 
                    "Debe de ingresar una ruta valida, para un archivo que contiene el texto que se desea cifrar");
            
            this.txt_path.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Este metodo se encarga de retornar el tipo de modo para el cifrado segun lo 
     * seleccionado por el usuario.
     * 
     * @return 
     *      Retorna el nombre (abrebiatura) del modo para el cifrado seleccionado por el usuario.
     */
    private String getModeCipher() {
        if (this.rb_cbc.isSelected())
            return this.rb_cbc.getText();
        
        else if (this.rb_cfb.isSelected())
            return this.rb_cfb.getText();
        
        return this.rb_ecb.getText();
    }
    
    /**
     * Este metodo se encarga de retornar el tipo de algoritmo que desea usar el usario para encriptar 
     * el texto plano.
     * 
     * @return 
     *      Retorna una instancia de tipo {@link Algorithm} que contiene el nombre del algoritmo 
     *      seleccionado por el usuario.
     */
    private Algorithm getAlgorithmCipher() {
        if (this.rb_des.isSelected())
            return Algorithm.DES;
        
        return Algorithm.TRES_DES;
    }
    
    /**
     * Este metodo se encarga de retornar el tipo de codificación para el texto cifrado, con el algoritmo 
     * seleccionado por el usuario.
     * 
     * @return 
     *      En esta version siemre retorna {@link Encode#BASE64}.
     */
    private Encode getEncodeCipher() {
        return Encode.BASE64;
    }
    
    /**
     * Inidca si se desea o no aplicar rellono al algoritmo seleccionado.
     * 
     * @return 
     *      Retorna un {@code true} en esta version, para que siempre se aplique el rellenado.
     */
    private boolean requirePadding() {
        return true;
    }
    
    /**
     * Este metodo se encarga de obtener el texto plano correcto que el usuario desea encriptar
     * posteriormente.
     * 
     * @return 
     *      Retorna una instancia de tipo {@link String} que contiene el texto plano a cifrar.
     */
    private String getPlainText() {
        if (this.ckb_text.isSelected())
            return this.txta_text.getText();
        
        StringBuilder sb= new StringBuilder();
        
        try {
            Files.readAllLines(Paths.get(this.txt_path.getText())).
                    forEach(line -> 
                        sb.append(line).append(System.getProperty("line.separator"))
                    );
        } catch (IOException ex) {
            Logger.getLogger(FXMLCipherController.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
        
        return sb.toString();
    }
    
    @FXML
    private void handleBtn_generateKey(ActionEvent evt) {
        this.txt_key.clear();
        this.txt_key.setText(this.getRandomKey());
    }
    
    @FXML
    private void handleBtn_clear(ActionEvent evt) {
        this.clearControlls(true);
    }
    
    @FXML
    private void handleBtn_start(ActionEvent evt) {
        this.clearControlls(false);
        
        if (this.validateInpust()) {
            DesCipher desCipher= new DesCipher(this.getAlgorithmCipher(), this.getModeCipher(), 
                    this.requirePadding());
            
            if (this.rb_encrypt.isSelected())
                this.txta_result.setText(desCipher.encrypt(this.txt_key.getText(), this.getPlainText(), 
                        this.getEncodeCipher()));
            
            else
                this.txta_result.setText(desCipher.decrypt(this.txt_key.getText(), this.getPlainText(), 
                        this.getEncodeCipher()));
        }
    }
    
    @FXML
    private void handleBtn_path(ActionEvent evt) {
        FileChooser fc= new FileChooser();
        
        fc.setTitle("Abrir archivo de Texto");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Text (*.txt)", "*.txt"));
        
        File selected= fc.showOpenDialog(this.lbl_path.getScene().getWindow());
        
        if (selected != null && selected.isFile())
            this.txt_path.setText(selected.toPath().toString());
    }
    
    @FXML
    private void handleBtn_save(ActionEvent evt) {
        FileChooser fc= new FileChooser();
        
        fc.setTitle("Guardar Texto Cifrado");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de XML (*.xml)", "*.xml"));
                
        File selected= fc.showSaveDialog(this.lbl_path.getScene().getWindow());
        
        if (selected != null) {
            CipherXml xml= new CipherXml(new CipherParamsXml(this.getAlgorithmCipher().getAlgorithm(), this.txt_key.getText(), 
                    this.getModeCipher()), this.txta_result.getText());
            
            ManageXml.saveFile(xml, selected.toPath());
        }
    }
    
    @FXML
    private void handleBtn_explorerConfig(ActionEvent evt) {
        FileChooser fc= new FileChooser();
        
        fc.setTitle("Abrir archivo de Texto Cifrado");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de XML (*.xml)", "*.xml"));
        
        File selected= fc.showOpenDialog(this.lbl_path.getScene().getWindow());
        
        if (selected != null && Files.exists(selected.toPath(), LinkOption.NOFOLLOW_LINKS)) {
            CipherXml xml= ManageXml.loadFile(selected.toPath());
            
            if (xml != null) {
                this.clearControlls(true);
                
                this.rb_decrypt.setSelected(true);
                this.txta_text.setText(xml.getText());
                
                // set algorithm
                switch (Algorithm.fromName(xml.getParams().getAlgorithm())) {
                    case DES:
                        this.rb_des.setSelected(true);
                        
                        break;
                        
                    case TRES_DES:
                        this.rb_tripleDes.setSelected(true);
                        
                        break;
                }
                
                // set mode
                String mode= xml.getParams().getMode();
                
                if (mode.equalsIgnoreCase("cbc"))
                    this.rb_cbc.setSelected(true);
                
                else if (mode.equalsIgnoreCase("ecb"))
                    this.rb_ecb.setSelected(true);
                
                else if (mode.equalsIgnoreCase("cfb"))
                    this.rb_cfb.setSelected(true);
                
                // set key
                this.txt_key.setText(xml.getParams().getKey());
                
            } else 
                DialogUtil.showWarning("Error al cargar archivo", 
                        "Ocurrio un erro al cargar un archivo, favor intentar nuevamente o comprobar el contenido del archivo que se desea cargar");
        }
    }
}