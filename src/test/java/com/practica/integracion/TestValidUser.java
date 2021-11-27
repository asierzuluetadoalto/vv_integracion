package com.practica.integracion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	@Mock
	private AuthDAO authDao;
	@Mock
	private GenericDAO genericDao;
	
	private User usuario;
	private Object remote;
	private SystemManager systemManager;
	
	@BeforeEach
	void setUp() throws Exception {
		usuario = new User("10","nombre","apellido","dir",new ArrayList<Object>());
		remote = new Object();
		
		systemManager = new SystemManager(authDao,genericDao);
	}
	
	
	@Test
	public void testAddRemoteSystem() throws OperationNotSupportedException, SystemManagerException{		
		when(authDao.getAuthData("10")).thenReturn(usuario);
		when(genericDao.updateSomeData(usuario, remote)).thenReturn(true);
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		systemManager.addRemoteSystem("10", remote);
		
		verify(authDao, times(1)).getAuthData("10");	
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).updateSomeData(usuario, remote);
	}
	
	@Test
	public void testStartRemoteSystem() throws OperationNotSupportedException, SystemManagerException{		
		when(authDao.getAuthData("10")).thenReturn(usuario);
		when(genericDao.getSomeData(usuario,"where id=remId")).thenReturn(new HashSet());
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		systemManager.startRemoteSystem("10", "remId");
		
		verify(authDao, times(1)).getAuthData("10");
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).getSomeData(usuario, "where id=remId");
	}
	
	@Test
	public void testStopRemoteSystem() throws OperationNotSupportedException, SystemManagerException{
		when(authDao.getAuthData("10")).thenReturn(usuario);
		when(genericDao.getSomeData(usuario,"where id=remId")).thenReturn(new HashSet());
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		systemManager.stopRemoteSystem("10", "remId");
		
		verify(authDao, times(1)).getAuthData("10");
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).getSomeData(usuario, "where id=remId");
	}
	
	@Test
	public void testDeleteRemoteSystem() throws OperationNotSupportedException, SystemManagerException{
		when(authDao.getAuthData("10")).thenReturn(usuario);
		when(genericDao.deleteSomeData(Mockito.any(User.class), Mockito.anyString())).thenReturn(true);
		
		systemManager.deleteRemoteSystem("10", "remId");
		
		verify(authDao, times(1)).getAuthData("10");	
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).deleteSomeData(Mockito.any(User.class), Mockito.anyString());
		
	}
		

}
