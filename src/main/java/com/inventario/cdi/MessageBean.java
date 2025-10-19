package com.inventario.cdi;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("messageBean")
@SessionScoped
public class MessageBean implements Serializable {

    private String mensaje;
    private String tipo; // "success" o "error"

    // ✅ Métodos de acceso
    public String getMensaje() {
        return mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    // ✅ Métodos para establecer mensajes amigables
    public void setTextoInfo(String texto) {
        this.mensaje = texto;
        this.tipo = "success";
    }

    public void setTextoError(String texto) {
        this.mensaje = texto;
        this.tipo = "error";
    }

    // ✅ Limpia los mensajes (por ejemplo, después de mostrarlos)
    public void limpiar() {
        this.mensaje = null;
        this.tipo = null;
    }

    /**
     * ✅ Método opcional “consumeMensaje” para mostrar una vez y limpiar
     */
    public String consumeMensaje() {
        String temp = this.mensaje;
        this.limpiar();
        return temp;
    }
}
