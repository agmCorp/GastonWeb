package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IUsuarioServicesLocal {
	public String getNombreCompleto(String email) throws NegocioException;

	public String getNombreCompleto(String nombre, String apellido);

	public void registrarUsuario(Usuario usuario, List<String> roles, String idActivacion) throws NegocioException;

	public Usuario activarUsuario(String idActivacion) throws NegocioException;

	public void guardar(Usuario usuario) throws NegocioException;

	public Usuario encontrar(String email) throws NegocioException;

	public List<GrupoJaas> obtenerGruposJaas(String email) throws NegocioException;

	public String getRandomPassword();

	public List<Usuario> obtenerUsuarios() throws NegocioException;
}
