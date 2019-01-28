/**
 * 
 */
package br.com.curso.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.curso.model.Usuario;
import br.com.curso.utils.JPAUtils;

/**
 * @author carlosbarbosagomesfilho
 *
 */
public class UsuarioDaoImpl implements IUsuarioDao {

	EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
	EntityManager factory = null;

	public void save(Usuario usuario) throws Exception  {

		this.factory = emf.createEntityManager();

		try {
			factory.getTransaction().begin();
			if (usuario.getId() == null) {
				usuario.setAtivo(true);
				factory.persist(usuario);
				
			} else {
				factory.merge(usuario);
			}
			factory.getTransaction().commit();

		} catch (Exception e) {
			e.getMessage();
		} finally {
			factory.close();
		}
	}
	
	@Override
	public Usuario ativarDesativar(Long id) throws Exception {
		Usuario usuario = this.findById(id);
		if(usuario.isAtivo()) {
			usuario.setAtivo(false);
		}else {
			usuario.setAtivo(true);
		}
		this.save(usuario);
		return usuario;
	}
	

	public Usuario findById(Long id) throws Exception  {
		this.factory = emf.createEntityManager();
		Usuario usuario = new Usuario();
		try {
			usuario = factory.find(Usuario.class, id);
			return usuario;

		} catch (Exception e) {
			e.getMessage();

		} finally {
			factory.close();
		}
		return null;
	}

	@Override
	public void deleteById(Long id) throws Exception {
		this.factory = emf.createEntityManager();
		Usuario usuario = new Usuario();

		try {

			usuario = factory.find(Usuario.class, id);
			factory.getTransaction().begin();
			factory.remove(usuario);
			factory.getTransaction().commit();

		} catch (Exception e) {
			e.getMessage();

		} finally {
			factory.close();
		}

	}

	@Override
	public void update(Usuario usuario) throws Exception {
		this.factory = emf.createEntityManager();

		try {
			factory.getTransaction().begin();
			factory.merge(usuario);
			factory.getTransaction().commit();

		} catch (Exception e) {
			e.getMessage();
		} finally {
			factory.close();
		}
	}

	@Override
	public Collection<Usuario> listAll() throws Exception {
		this.factory = emf.createEntityManager();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		try {
			factory.getTransaction().begin();
			TypedQuery<Usuario> query = factory.createNamedQuery("Usuario.findAll", Usuario.class);
			usuarios = query.getResultList();
			factory.getTransaction().commit();

		} catch (Exception e) {
			e.getMessage();
		} finally {
			factory.close();
		}

		return usuarios;
	}

	@Override
	public Usuario acess(String login, String senha) throws Exception {
		
		Usuario usuario = null;
		this.factory = emf.createEntityManager();
		
		try {
			
			Query query = this.factory.createNamedQuery("Usuario.loginUsuario");
			query.setParameter("login", login);
			query.setParameter("senha", senha);
			
			usuario = (Usuario) query.getSingleResult();
			
			return usuario;
					
		} catch (Exception e) {
			e.getMessage();
		}
		
		return usuario;
	}

}