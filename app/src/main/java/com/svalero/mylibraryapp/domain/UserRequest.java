package com.svalero.mylibraryapp.domain;

/**
 * Clase utilizada únicamente para enviar datos al backend al registrar un nuevo usuario.
 * Contiene únicamente los campos que la API espera (según OpenAPI).
 *
 * Esto evita errores de "500 Internal Server Error" por enviar campos que el backend no acepta.
 */
public class UserRequest {

    private String name;       // Nombre del usuario
    private String username;   // Nombre de usuario (único)
    private String email;      // Correo electrónico válido
    private String password;   // Contraseña del usuario (mínimo 6 caracteres)

    /**
     * Constructor completo. Este es el que usaremos para construir un UserRequest antes de enviarlo.
     */
    public UserRequest(String name, String username, String email, String password) {
        this.name       = name;
        this.username   = username;
        this.email      = email;
        this.password   = password;
    }

    // Getters y setters clásicos. No inventes aquí.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Este método es útil para depuración. Si haces un Log.d() de este objeto, verás algo decente.
     */
    @Override
    public String toString() {
        return "UserRequest{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
